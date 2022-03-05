package main.java.server;

import main.java.server.router.Router;
import main.java.server.service.BankServices;
import main.java.server.service.Subscription;
import main.java.shared.udp.UDPClient;
import main.java.shared.udp.UDPMessage;

import java.net.*;
import java.util.logging.Logger;

public class ServerRunner {
    private static final Logger LOGGER = Logger.getLogger(ServerRunner.class.getName());

    public ServerRunner (float requestPacketLossRate, float responsePacketLossRate, boolean atMostOnce) throws SocketException, UnknownHostException {
        final String host = "0.0.0.0";
        final int port = 12740;
        final int bufferSize = 1024;

        final InetSocketAddress serverAddress = new InetSocketAddress(host, port);
        UDPClient udpClient = new UDPClient(
                new DatagramSocket(serverAddress),
                bufferSize
        );
        LOGGER.info(String.format("Listening on udp://%s:%s", InetAddress.getLocalHost().getHostAddress(), port));

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
