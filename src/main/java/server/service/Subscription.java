package main.java.server.service;

import main.java.shared.udp.UDPClient;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Subscription {
    private Map<SocketAddress, Instant> subscribers = new HashMap<>();
    private final UDPClient udpClient;

    public Subscription(UDPClient udpClient) {
        this.udpClient = udpClient;
    }

    public void broadcastMessage(String message) {
        // TODO: Implement
    }
}
