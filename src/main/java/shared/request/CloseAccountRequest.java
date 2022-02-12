package main.java.shared.request;

public class CloseAccountRequest extends RequestBody{
    private final int accountNumber;
    private final String holderName;
    private final String password;


    public CloseAccountRequest(String holderName, String password, int accountNumber) {
        this.holderName = holderName;
        this.password = password;
        this.accountNumber = accountNumber;
    }

    @Override
    public byte[] toBytes() {
//        TODO - toBytes
        return new byte[0];
    }


}
