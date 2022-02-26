package main.java.shared.request;

import java.nio.ByteBuffer;

public class SubscribeRequest extends RequestBody {
    private final int interval;

    public SubscribeRequest(int interval) {
        this.interval = interval;
    }

    public static SubscribeRequest fromBytes(byte[] bytes) {
        int interval = ByteBuffer.wrap(bytes).getInt();
        return new SubscribeRequest(interval);
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public byte[] toBytes() {
        return ByteBuffer.allocate(4).putInt(this.interval).array();
    }
}
