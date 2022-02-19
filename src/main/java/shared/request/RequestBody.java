package main.java.shared.request;

public abstract class RequestBody {
    public abstract byte[] toBytes();
    public abstract int getBodyLength();
}
