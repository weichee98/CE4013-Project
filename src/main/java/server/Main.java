package main.java.server;

import main.java.server.router.Router;
import main.java.server.service.BankServices;
import main.java.server.service.Subscription;
import main.java.shared.udp.UDPClient;
import main.java.shared.udp.UDPMessage;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws SocketException {
        final String host = "0.0.0.0";
        final int port = 12740;
        final int bufferSize = 1024;
        final float requestPacketLossRate = (float) 0.1;
        final float responsePacketLossRate = (float) 0.1;
        final boolean atMostOnce = true;

        final InetSocketAddress serverAddress = new InetSocketAddress(host, port);
        UDPClient udpClient = new UDPClient(
                new DatagramSocket(serverAddress),
                bufferSize
        );
        LOGGER.info(String.format("Listening on udp:/%s", serverAddress));

        Subscription sub = new Subscription(udpClient);
        BankServices bs = new BankServices(sub);
        Router router = new Router(bs, atMostOnce? 1024 : 0);

        for (; ; ) {
            try (UDPMessage req = udpClient.receive()) {
                if (Math.random() < requestPacketLossRate) {
                    LOGGER.info(String.format("Dropped a request from %s", req.getAddress()));
                    continue;
                } else {
                    LOGGER.info(String.format("Received a request from %s", req.getAddress()));
                }
                UDPMessage resp = router.route(req);
                if (Math.random() < responsePacketLossRate) {
                    LOGGER.info(String.format("Dropped a response to %s", resp.getAddress()));
                } else {
                    udpClient.send(resp);
                    LOGGER.info(String.format("Sent a response to %s", resp.getAddress()));
                }
            } catch (Exception e) {
                LOGGER.severe(e.getMessage());
            }
        }
    }
}
