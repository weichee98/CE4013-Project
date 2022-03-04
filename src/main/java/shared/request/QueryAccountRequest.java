package main.java.shared.request;

import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class QueryAccountRequest extends RequestBody {
    private final int accountNumber;
    private final String holderName;
    private final String password;


    public QueryAccountRequest(int accountNumber, String holderName, String password) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.password = password;
    }

    public static QueryAccountRequest fromBytes(byte[] bytes) {
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

        return new QueryAccountRequest(
                accountNumber,
                holderName,
                password
        );
    }

    public static void main(String[] args) {
        QueryAccountRequest c = new QueryAccountRequest(
                8014374,
                "lsad",
                "asdliadasa"
        );
        byte[] bytes = c.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(QueryAccountRequest.fromBytes(bytes));
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

        return bytes;
    }

    @Override
    public String toString() {
        return "CloseAccountRequest{" +
                "accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                '}';
    }
}
