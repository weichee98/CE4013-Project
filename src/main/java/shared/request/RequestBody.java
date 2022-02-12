package main.java.shared.request;

public abstract class RequestBody {
    String errorMessage;
    boolean status;
    public abstract byte[] toBytes();
}
