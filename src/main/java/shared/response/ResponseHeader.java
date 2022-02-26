package main.java.shared.response;

import main.java.shared.interfaces.Header;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class ResponseHeader extends Header {
    public static final int totalBytes = 21;
    private final ResponseType responseType;

    public ResponseHeader(UUID uuid, ResponseType responseType, int bodyLength) {
        super(uuid, bodyLength);
        this.responseType = responseType;
    }

    public static ResponseHeader fromBytes(byte[] bytes) {
        UUID uuid = Header.getUUIDFromBytes(Arrays.copyOfRange(bytes, 0, 16));
        ResponseType responseType = ResponseType.get(bytes[16]);
        int bodyLength = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 17, 21)).getInt();
        return new ResponseHeader(uuid, responseType, bodyLength);
    }

    public static void main(String[] args) {
        ResponseHeader header = new ResponseHeader(UUID.randomUUID(), ResponseType.OPEN_ACCOUNT, 78);
        System.out.println(header);
        byte[] bytes = header.toBytes();
        ResponseHeader newHeader = ResponseHeader.fromBytes(bytes);
        System.out.println(newHeader);
    }

    public ResponseType getResponseType() {
        return this.responseType;
    }

    @Override
    public byte[] toBytes() {
        byte[] uuidBytes = this.getUUIDBytes();
        byte responseTypeByte = this.responseType.toByte();
        byte[] bodyLengthBytes = ByteBuffer.allocate(4).putInt(this.getBodyLength()).array();
        byte[] bytes = new byte[totalBytes];
        System.arraycopy(uuidBytes, 0, bytes, 0, 16);
        bytes[16] = responseTypeByte;
        System.arraycopy(bodyLengthBytes, 0, bytes, 17, 4);
        return bytes;
    }

    @Override
    public String toString() {
        return "ResponseHeader{" +
                "requestType=" + responseType + ", " +
                "uuid=" + this.getUUID() + ", " +
                "bodyLength=" + this.getBodyLength() +
                '}';
    }
}
