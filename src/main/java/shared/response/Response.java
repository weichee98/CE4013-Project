package main.java.shared.response;

import java.util.Arrays;

public class Response {
    private final ResponseHeader header;
    private final byte[] respBodyBytes;

    public Response(ResponseHeader header, byte[] respBodyBytes) {
        this.header = header;
        this.respBodyBytes = respBodyBytes;
    }

    public static Response fromBytes(byte[] bytes) {
        ResponseHeader header = ResponseHeader.fromBytes(
                Arrays.copyOfRange(bytes, 0, ResponseHeader.totalBytes)
        );
        byte[] respBodyBytes = Arrays.copyOfRange(
                bytes, ResponseHeader.totalBytes, ResponseHeader.totalBytes + header.getBodyLength()
        );
        return new Response(header, respBodyBytes);
    }

    public ResponseHeader getHeader() {
        return this.header;
    }

    public byte[] getRespBodyBytes() {
        return this.respBodyBytes;
    }

    public byte[] toBytes() {
        byte[] headerBytes = this.header.toBytes();
        byte[] bytes = new byte[headerBytes.length + this.respBodyBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(this.respBodyBytes, 0, bytes, headerBytes.length, this.respBodyBytes.length);
        return bytes;
    }
}
