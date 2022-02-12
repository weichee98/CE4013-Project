package main.java.shared.response;

public class OpenAccountResponse extends ResponseBody {
    private final int accountNumber;
    private final boolean status;
    private final String errorMessage;


    private OpenAccountResponse(int accountNumber, boolean status, String errorMessage) {
        this.accountNumber = accountNumber;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static OpenAccountResponse error(String errorMessage) {
        return new OpenAccountResponse(0, false, errorMessage);
    }

    public static OpenAccountResponse success(int accountNumber, boolean status){
        return new OpenAccountResponse(accountNumber, true, "");
    }

    @Override
    public String toString() {
        return "OpenAccountResponse{" +
                "accountNumber=" + accountNumber +
                ", status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    @Override
    public byte[] toBytes() {
//        TODO - toBytes
        return new byte[0];
    }

}
