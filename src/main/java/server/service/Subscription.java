package main.java.server.service;

import main.java.shared.response.Response;
import main.java.shared.response.ResponseHeader;
import main.java.shared.response.ResponseType;
import main.java.shared.response.SubscribeMessageResponse;
import main.java.shared.udp.UDPClient;
import main.java.shared.udp.UDPMessage;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class Subscription {
    private static final Logger LOGGER = Logger.getLogger(Subscription.class.getName());
    private final Map<SocketAddress, Instant> subscribers = new HashMap<>();
    private final UDPClient udpClient;

    public Subscription(UDPClient udpClient) {
        this.udpClient = udpClient;
    }

    public void subscribe(SocketAddress address, long interval) throws Exception {
        if (interval <= 0) {
            throw new Exception("interval cannot be less than or equals to 0");
        }
        subscribers.put(address, Instant.now().plusSeconds(interval));
        this.broadcastMessage(String.format("Added subscriber: %s", address));
        LOGGER.info(String.format("Added subscriber: %s", address));
    }

    private void removeExpiredSubscribers() {
        Instant currentTime = Instant.now();
        subscribers.entrySet().removeIf(x -> {
            boolean expired = x.getValue().isBefore(currentTime);
            if (expired) {
                LOGGER.info(String.format("Removed subscriber: %s", x.getKey()));
            }
            return expired;
        });
    }

    public void broadcastMessage(String message) {
        SubscribeMessageResponse respBody = new SubscribeMessageResponse(message);
        byte[] respBodyBytes = respBody.toBytes();
        ResponseHeader header = new ResponseHeader(
                UUID.randomUUID(), ResponseType.SUBSCRIBE_UPDATE, respBodyBytes.length
        );
        Response resp = new Response(header, respBodyBytes);
        byte[] responseBytes = resp.toBytes();

        removeExpiredSubscribers();
        subscribers.forEach((socketAddress, x) -> {
            try {
                this.udpClient.send(new UDPMessage(
                        socketAddress, responseBytes
                ));
            } catch (RuntimeException e) {
            }
        });
    }
}
