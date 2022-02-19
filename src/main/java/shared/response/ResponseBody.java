package main.java.shared.response;

public abstract class ResponseBody {
    String errorMessage;
    boolean status;

    public ResponseBody(boolean status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isStatus() {
        return status;
    }

    public abstract byte[] toBytes();
    public abstract int getBodyLength();
}
