package main.java.shared.request;

import main.java.shared.interfaces.Header;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class RequestHeader extends Header {
    public static final int totalBytes = 21;
    private final RequestType requestType;

    public RequestHeader(UUID uuid, RequestType requestType, int bodyLength) {
        super(uuid, bodyLength);
        this.requestType = requestType;
    }

    public static RequestHeader fromBytes(byte[] bytes) {
        UUID uuid = Header.getUUIDFromBytes(Arrays.copyOfRange(bytes, 0, 16));
        RequestType requestType = RequestType.get(bytes[16]);
        int bodyLength = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 17, 21)).getInt();
        return new RequestHeader(uuid, requestType, bodyLength);
    }

    public static void main(String[] args) {
        RequestHeader header = new RequestHeader(UUID.randomUUID(), RequestType.OPEN_ACCOUNT, 78);
        System.out.println(header);
        byte[] bytes = header.toBytes();
        RequestHeader newHeader = RequestHeader.fromBytes(bytes);
        System.out.println(newHeader);
    }

    public RequestType getRequestType() {
        return this.requestType;
    }

    @Override
    public byte[] toBytes() {
        byte[] uuidBytes = this.getUUIDBytes();
        byte requestTypeByte = this.requestType.toByte();
        byte[] bodyLengthBytes = ByteBuffer.allocate(4).putInt(this.getBodyLength()).array();
        byte[] bytes = new byte[totalBytes];
        System.arraycopy(uuidBytes, 0, bytes, 0, 16);
        bytes[16] = requestTypeByte;
        System.arraycopy(bodyLengthBytes, 0, bytes, 17, 4);
        return bytes;
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "requestType=" + requestType + ", " +
                "uuid=" + this.getUUID() + ", " +
                "bodyLength=" + this.getBodyLength() +
                '}';
    }
}
