package main.java.shared.request;

import java.util.Arrays;

public class Request {
    private final RequestHeader header;
    private final byte[] reqBodyBytes;

    public Request(RequestHeader header, byte[] reqBodyBytes) {
        this.header = header;
        this.reqBodyBytes = reqBodyBytes;
    }

    public static Request fromBytes(byte[] bytes) {
        RequestHeader header = RequestHeader.fromBytes(
                Arrays.copyOfRange(bytes, 0, RequestHeader.totalBytes)
        );
        byte[] reqBodyBytes = Arrays.copyOfRange(
                bytes, RequestHeader.totalBytes, RequestHeader.totalBytes + header.getBodyLength()
        );
        return new Request(header, reqBodyBytes);
    }

    public RequestHeader getHeader() {
        return this.header;
    }

    public byte[] getReqBodyBytes() {
        return this.reqBodyBytes;
    }

    public byte[] toBytes() {
        byte[] headerBytes = this.header.toBytes();
        byte[] bytes = new byte[headerBytes.length + this.reqBodyBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(this.reqBodyBytes, 0, bytes, headerBytes.length, this.reqBodyBytes.length);
        return bytes;
    }
}
