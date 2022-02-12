package main.java.shared.response;

public class CloseAccountResponse extends ResponseBody {
    private final int accountNumber;
    private final String holderName;
    private final boolean status;
    private final String errorMessage;


    private CloseAccountResponse(int accountNumber, boolean status, String errorMessage, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static CloseAccountResponse error(String errorMessage) {
        return new CloseAccountResponse(0, false, errorMessage, "");
    }

    public static CloseAccountResponse success(int accountNumber, String holderName){
        return new CloseAccountResponse(accountNumber, true, "", holderName);
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
