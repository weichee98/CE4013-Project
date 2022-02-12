package main.java.shared.interfaces;

import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class Header {
    private final UUID uuid;

    public Header(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public byte[] getUUIDBytes() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(this.uuid.getMostSignificantBits());
        bb.putLong(this.uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }

    public abstract byte[] toBytes();
}
