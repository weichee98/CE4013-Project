package main.java.shared.request;

import main.java.shared.interfaces.Header;

import java.util.Arrays;
import java.util.UUID;

public class RequestHeader extends Header {
    public static final int totalBytes = 17;
    private final RequestType requestType;

    public RequestHeader(UUID uuid, RequestType requestType) {
        super(uuid);
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return this.requestType;
    }

    public byte[] toBytes() {
        byte[] uuidBytes = this.getUUIDBytes();
        byte requestTypeByte = this.requestType.toByte();
        byte[] bytes = new byte[totalBytes];
        System.arraycopy(uuidBytes, 0, bytes, 0, 16);
        bytes[16] = requestTypeByte;
        return bytes;
    }

    public static RequestHeader fromBytes(byte[] bytes) {
        UUID uuid = Header.getUUIDFromBytes(Arrays.copyOfRange(bytes, 0, 16));
        RequestType requestType = RequestType.get(bytes[16]);
        return new RequestHeader(uuid, requestType);
    }
}
