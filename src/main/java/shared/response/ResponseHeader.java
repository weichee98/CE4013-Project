package main.java.shared.response;

import main.java.shared.interfaces.Header;

import java.util.Arrays;
import java.util.UUID;

public class ResponseHeader extends Header {
    public static final int totalBytes = 17;
    private final ResponseType responseType;

    public ResponseHeader(UUID uuid, ResponseType responseType) {
        super(uuid);
        this.responseType = responseType;
    }

    public static ResponseHeader fromBytes(byte[] bytes) {
        UUID uuid = Header.getUUIDFromBytes(Arrays.copyOfRange(bytes, 0, 16));
        ResponseType responseType = ResponseType.get(bytes[16]);
        return new ResponseHeader(uuid, responseType);
    }

    public ResponseType getResponseType() {
        return this.responseType;
    }

    @Override
    public byte[] toBytes() {
        byte[] uuidBytes = this.getUUIDBytes();
        byte responseTypeByte = this.responseType.toByte();
        byte[] bytes = new byte[totalBytes];
        System.arraycopy(uuidBytes, 0, bytes, 0, 16);
        bytes[16] = responseTypeByte;
        return bytes;
    }
}
