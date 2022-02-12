package main.java.shared.interfaces;

import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class Header {
    private final UUID uuid;
    private final int bodyLength;

    public Header(UUID uuid, int bodyLength) {
        this.uuid = uuid;
        this.bodyLength = bodyLength;
    }

    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public int getBodyLength() {
        return this.bodyLength;
    }

    public byte[] getUUIDBytes() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(this.uuid.getMostSignificantBits());
        bb.putLong(this.uuid.getLeastSignificantBits());
        return bb.array();
    }

    public abstract byte[] toBytes();
}
