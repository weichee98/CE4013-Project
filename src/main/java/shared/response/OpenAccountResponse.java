package main.java.shared.response;

import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class OpenAccountResponse extends ResponseBody {
    private final int accountNumber;

    private OpenAccountResponse(boolean status, String errorMessage, int accountNumber) {
        super(status, errorMessage);
        this.accountNumber = accountNumber;
    }

    public static OpenAccountResponse error(String errorMessage) {
        return new OpenAccountResponse(true, errorMessage, 0);
    }

    public static OpenAccountResponse success(int accountNumber) {
        return new OpenAccountResponse(false, "", accountNumber);
    }

    public static OpenAccountResponse fromBytes(byte[] bytes) {
        int ptr = 0;
        boolean status = bytes[0] == 1;
        ptr += 1;

        int errorMessageEnd = Tools.findEndOfString(bytes, ptr);
        String errorMessage = new String(Arrays.copyOfRange(bytes, ptr, errorMessageEnd));
        ptr = errorMessageEnd + 1;

        int accountNumber = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getInt();

        return new OpenAccountResponse(status, errorMessage, accountNumber);
    }

    public static void main(String[] args) {
        OpenAccountResponse c = new OpenAccountResponse(true, "errorMedssage", 2334);
        byte[] bytes = c.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(OpenAccountResponse.fromBytes(bytes));
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return "OpenAccountResponse{" +
                "accountNumber=" + accountNumber +
                ", errorMessage='" + errorMessage + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public byte[] toBytes() {
        byte statusByte = (byte) (this.isStatus() ? 1 : 0);
        byte[] errorMessageByte = (this.getErrorMessage() + "\0").getBytes();
        byte[] accountNumberByte = ByteBuffer.allocate(4).putInt(this.getAccountNumber()).array();

        final int totalBytes = 1 + errorMessageByte.length + accountNumberByte.length;
        byte[] bytes = new byte[totalBytes];
        int ptr = 0;
        bytes[ptr] = statusByte;
        ptr += 1;
        System.arraycopy(errorMessageByte, 0, bytes, ptr, errorMessageByte.length);
        ptr += errorMessageByte.length;
        System.arraycopy(accountNumberByte, 0, bytes, ptr, 4);

        return bytes;
    }


}
