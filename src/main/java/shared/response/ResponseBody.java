package main.java.shared.response;

public abstract class ResponseBody {
    String errorMessage;
    boolean status;
    public abstract byte[] toBytes();

}
