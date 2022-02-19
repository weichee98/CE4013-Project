package main.java.shared.response;

import main.java.shared.entity.Currency;
import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TransferResponse extends ResponseBody {
    private final int accountNumber;
    private final String holderName;
    private final int targetAccountNumber;
    private final String targetHolderName;
    private final Currency currency;
    private final float amount;


    public TransferResponse(boolean status, String errorMessage, int accountNumber, String holderName, int targetAccountNumber, String targetHolderName, Currency currency, float amount) {
        super(status, errorMessage);
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.targetAccountNumber = targetAccountNumber;
        this.targetHolderName = targetHolderName;
        this.currency = currency;
        this.amount = amount;
    }

    public static TransferResponse error(String errorMessage) {
        return new TransferResponse(false, errorMessage, 0, "", 0, "", Currency.ZZZ, -1.0F);
    }

    public static TransferResponse success(int accountNumber, String holderName, int targetAccountNumber, String targetHolderName, Currency currency, float amount) {
        return new TransferResponse(true, "", accountNumber, holderName, targetAccountNumber, targetHolderName, currency, amount);
    }

    public static TransferResponse fromBytes(byte[] bytes) {
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
        ptr = holderNameEnd + 1;

        int targetAccountNumber = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getInt();
        ptr += 4;

        int targetHolderNameEnd = Tools.findEndOfString(bytes, ptr);
        String targetHolderName = new String(Arrays.copyOfRange(bytes, ptr, targetHolderNameEnd));
        ptr = targetHolderNameEnd + 1;

        Currency currency = Currency.fromByte(bytes[ptr]);
        ptr += 1;

        float amount = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getFloat();

        return new TransferResponse(status, errorMessage, accountNumber, holderName, targetAccountNumber, targetHolderName, currency, amount);
    }

    public static void main(String[] args) {
        TransferResponse request = new TransferResponse(true, "errorMessage", 1234, "abc", 453, "def", Currency.USD, 103.34F);
        byte[] bytes = request.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(TransferResponse.fromBytes(bytes));
    }

    @Override
    public String toString() {
        return "TransferResponse{" +
                "errorMessage='" + errorMessage + '\'' +
                ", status=" + status +
                ", accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                ", targetAccountNumber=" + targetAccountNumber +
                ", targetHolderName='" + targetHolderName + '\'' +
                ", currency=" + currency +
                ", amount=" + amount +
                '}';
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public float getAmount() {
        return amount;
    }

    public int getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public String getTargetHolderName() {
        return targetHolderName;
    }

    @Override
    public byte[] toBytes() {
        byte statusByte = (byte) (this.isStatus() ? 1 : 0);
        byte[] errorMessageByte = (this.getErrorMessage() + "\0").getBytes();
        byte[] accountNumberByte = ByteBuffer.allocate(4).putInt(this.getAccountNumber()).array();
        byte[] holderNameByte = (this.getHolderName() + "\0").getBytes();
        byte[] targetAccountNumberByte = ByteBuffer.allocate(4).putInt(this.getTargetAccountNumber()).array();
        byte[] targetHolderNameByte = (this.getTargetHolderName() + "\0").getBytes();
        byte currencyByte = this.getCurrency().toByte();
        byte[] amountByte = ByteBuffer.allocate(4).putFloat(this.getAmount()).array();

        final int totalBytes = 1 + errorMessageByte.length
                + accountNumberByte.length + holderNameByte.length
                + targetAccountNumberByte.length + targetHolderNameByte.length
                + 1 + amountByte.length;
        byte[] bytes = new byte[totalBytes];
        int ptr = 0;
        bytes[ptr] = statusByte;
        ptr += 1;
        System.arraycopy(errorMessageByte, 0, bytes, ptr, errorMessageByte.length);
        ptr += errorMessageByte.length;
        System.arraycopy(accountNumberByte, 0, bytes, ptr, 4);
        ptr += 4;
        System.arraycopy(holderNameByte, 0, bytes, ptr, holderNameByte.length);
        ptr += holderNameByte.length;
        System.arraycopy(targetAccountNumberByte, 0, bytes, ptr, 4);
        ptr += 4;
        System.arraycopy(targetHolderNameByte, 0, bytes, ptr, targetHolderNameByte.length);
        ptr += targetHolderNameByte.length;
        bytes[ptr] = currencyByte;
        ptr += 1;
        System.arraycopy(amountByte, 0, bytes, ptr, 4);

        return bytes;
    }

}
