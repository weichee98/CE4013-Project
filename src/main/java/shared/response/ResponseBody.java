package main.java.shared.response;

public abstract class ResponseBody {
    String errorMessage;
    boolean status;

    public ResponseBody(boolean status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public abstract byte[] toBytes();
}
