package main.java.shared.request;

import java.util.Arrays;

public class Request {
    private final RequestHeader header;
    private final RequestBody body;

    public Request(RequestHeader header, RequestBody body) {
        this.header = header;
        this.body = body;
    }

    public RequestHeader getHeader() {
        return this.header;
    }

    public RequestBody getBody() {
        return this.body;
    }

    public byte[] toBytes() {
        byte[] headerBytes = this.header.toBytes();
        byte[] bodyBytes = this.body.toBytes();
        byte[] bytes = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, bytes, headerBytes.length, bodyBytes.length);
        return bytes;
    }

    public static RequestHeader getHeaderFromBytes(byte[] bytes) {
        return RequestHeader.fromBytes(Arrays.copyOfRange(bytes, 0, RequestHeader.totalBytes));
    }

    public static byte[] getRequestBodyBytes(byte[] bytes) {
        return Arrays.copyOfRange(bytes, RequestHeader.totalBytes, bytes.length);
    }
}
