package main.java.shared.response;

import java.util.Arrays;

public class Response {
    private final ResponseHeader header;
    private final ResponseBody body;

    public Response(ResponseHeader header, ResponseBody body) {
        this.header = header;
        this.body = body;
    }

    public ResponseHeader getHeader() {
        return this.header;
    }

    public ResponseBody getBody() {
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

    public static ResponseHeader getHeaderFromBytes(byte[] bytes) {
        return ResponseHeader.fromBytes(Arrays.copyOfRange(bytes, 0, ResponseHeader.totalBytes));
    }

    public static byte[] getResponseBodyBytes(byte[] bytes) {
        return Arrays.copyOfRange(bytes, ResponseHeader.totalBytes, bytes.length);
    }
}
