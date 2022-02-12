package main.java.shared.udp;

import java.net.SocketAddress;

public class UDPMessage {
    private final SocketAddress address;
    private final byte[] bytes;

    public UDPMessage(SocketAddress address, byte[] bytes) {
        this.address = address;
        this.bytes = bytes;
    }

    public SocketAddress getAddress() {
        return this.address;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
