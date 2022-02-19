package main.java.shared.request;

import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class CloseAccountRequest extends RequestBody {
    private final int accountNumber;
    private final String holderName;
    private final String password;


    public CloseAccountRequest(int accountNumber, String holderName, String password) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.password = password;
    }

    public static CloseAccountRequest fromBytes(byte[] bytes) {
        int accountNumber = ByteBuffer.wrap(
                Arrays.copyOfRange(bytes, 0, 4)
        ).getInt();

        int ptr = 4;
        int holderNameEnd = Tools.findEndOfString(bytes, ptr);
        String holderName = new String(
                Arrays.copyOfRange(bytes, ptr, holderNameEnd)
        );
        ptr = holderNameEnd + 1;

        int passwordEnd = Tools.findEndOfString(bytes, ptr);
        String password = new String(
                Arrays.copyOfRange(bytes, ptr, passwordEnd)
        );
        ptr = passwordEnd + 1;

        return new CloseAccountRequest(
                accountNumber,
                holderName,
                password
        );
    }

    public static void main(String[] args) {
        CloseAccountRequest c = new CloseAccountRequest(
                8014374,
                "lsad",
                "asdliadasa"
        );
        byte[] bytes = c.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(CloseAccountRequest.fromBytes(bytes));
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
        byte[] accountNumberByte = ByteBuffer.allocate(4)
                .putInt(this.accountNumber).array();
        byte[] holderNameByte = (this.holderName + "\0").getBytes();
        byte[] passwordByte = (this.password + "\0").getBytes();

        final int totalBytes = accountNumberByte.length +
                holderNameByte.length + passwordByte.length;
        byte[] bytes = new byte[totalBytes];

        System.arraycopy(
                accountNumberByte, 0, bytes, 0, accountNumberByte.length
        );
        int ptr = accountNumberByte.length;

        System.arraycopy(
                holderNameByte, 0, bytes, ptr, holderNameByte.length
        );
        ptr += holderNameByte.length;

        System.arraycopy(
                passwordByte, 0, bytes, ptr, passwordByte.length
        );
        ptr += passwordByte.length;

        return bytes;
    }

    @Override
    public String toString() {
        return "CloseAccountRequest{" +
                "accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
