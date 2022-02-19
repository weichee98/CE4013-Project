package main.java.shared.response;

public class InvalidResponse extends ResponseBody {

    public InvalidResponse(String errorMessage) {
        super(false, errorMessage);
    }

    @Override
    public byte[] toBytes() {
        // TODO: Implement
        return new byte[0];
    }
}
