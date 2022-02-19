package main.java.shared.request;

public class CloseAccountRequest extends RequestBody {
    private final int accountNumber;
    private final String holderName;
    private final String password;


    public CloseAccountRequest(String holderName, String password, int accountNumber) {
        this.holderName = holderName;
        this.password = password;
        this.accountNumber = accountNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public byte[] toBytes() {
//        TODO - toBytes
        return new byte[0];
    }


}
