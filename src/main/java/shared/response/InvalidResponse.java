package main.java.shared.response;

public class InvalidResponse extends ResponseBody {

    public InvalidResponse(String errorMessage) {
        this.status = false;
        this.errorMessage = errorMessage;
    }

    @Override
    public byte[] toBytes() {
        // TODO: Implement
        return new byte[0];
    }
}
