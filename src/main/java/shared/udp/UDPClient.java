package main.java.shared.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClient {
    private final DatagramSocket socket;
    private final int bufferSize;

    public UDPClient(DatagramSocket socket, int bufferSize) {
        this.socket = socket;
        this.bufferSize = bufferSize;
    }

    public UDPMessage receive() {
        byte[] bytes = new byte[this.bufferSize];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        try {
            this.socket.receive(packet);
            return new UDPMessage(packet.getSocketAddress(), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(UDPMessage message) throws RuntimeException {
        try {
            byte[] bytes = message.getBytes();
            socket.send(new DatagramPacket(bytes, bytes.length, message.getAddress()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
