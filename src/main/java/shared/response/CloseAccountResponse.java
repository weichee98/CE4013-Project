package main.java.shared.response;

import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class CloseAccountResponse extends ResponseBody {
    private final int accountNumber;
    private final String holderName;


    private CloseAccountResponse(boolean status, String errorMessage, int accountNumber, String holderName) {
        super(status, errorMessage);
        this.accountNumber = accountNumber;
        this.holderName = holderName;
    }

    public static CloseAccountResponse error(String errorMessage) {
        return new CloseAccountResponse(false, errorMessage, 0, "");
    }

    public static CloseAccountResponse success(int accountNumber, String holderName) {
        return new CloseAccountResponse(true, "", accountNumber, holderName);
    }

    public static CloseAccountResponse fromBytes(byte[] bytes) {
        int ptr = 0;
        boolean status = bytes[0] == 1;
        ptr += 1;

        int errorMessageEnd = Tools.findEndOfString(bytes, ptr);
        String errorMessage = new String(Arrays.copyOfRange(bytes, ptr, errorMessageEnd));
        ptr = errorMessageEnd + 1;

        int accountNumber = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getInt();
        ptr += 4;

        int holderNameEnd = Tools.findEndOfString(bytes, ptr);
        String holderName = new String(Arrays.copyOfRange(bytes, ptr, holderNameEnd));

        return new CloseAccountResponse(status, errorMessage, accountNumber, holderName);
    }

    public static void main(String[] args) {
        CloseAccountResponse c = new CloseAccountResponse(true, "fjdifjdff", 123, "abc");
        byte[] bytes = c.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(CloseAccountResponse.fromBytes(bytes));
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    @Override
    public String toString() {
        return "CloseAccountResponse{" +
                "accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                ", status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    @Override
    public byte[] toBytes() {
        byte statusByte = (byte) (this.isStatus() ? 1 : 0);
        byte[] errorMessageByte = (this.getErrorMessage() + "\0").getBytes();
        byte[] accountNumberByte = ByteBuffer.allocate(4).putInt(this.getAccountNumber()).array();
        byte[] holderNameByte = (this.getHolderName() + "\0").getBytes();

        final int totalBytes = 1 + errorMessageByte.length + accountNumberByte.length + holderNameByte.length;
        byte[] bytes = new byte[totalBytes];
        int ptr = 0;
        bytes[ptr] = statusByte;
        ptr += 1;
        System.arraycopy(errorMessageByte, 0, bytes, ptr, errorMessageByte.length);
        ptr += errorMessageByte.length;
        System.arraycopy(accountNumberByte, 0, bytes, ptr, 4);
        ptr += 4;
        System.arraycopy(holderNameByte, 0, bytes, ptr, holderNameByte.length);

        return bytes;
    }

}
