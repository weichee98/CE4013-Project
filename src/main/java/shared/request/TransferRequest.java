package main.java.shared.request;

import main.java.shared.entity.Currency;
import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TransferRequest extends RequestBody {
    private final int accountNumber;
    private final String holderName;
    private final String password;
    private final int targetAccountNumber;
    private final String targetHolderName;
    private final Currency currency;
    private final float amount;


    public TransferRequest(int accountNumber, String holderName, String password, int targetAccountNumber, String targetHolderName, Currency currency, float amount) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.password = password;
        this.currency = currency;
        this.amount = amount;
        this.targetAccountNumber = targetAccountNumber;
        this.targetHolderName = targetHolderName;
    }

    public static TransferRequest fromBytes(byte[] bytes) {
        int ptr = 0;

        int accountNumber = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getInt();
        ptr += 4;

        int holderNameEnd = Tools.findEndOfString(bytes, ptr);
        String holderName = new String(Arrays.copyOfRange(bytes, ptr, holderNameEnd));
        ptr = holderNameEnd + 1;

        int passwordEnd = Tools.findEndOfString(bytes, ptr);
        String password = new String(Arrays.copyOfRange(bytes, ptr, passwordEnd));
        ptr = passwordEnd + 1;

        int targetAccountNumber = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getInt();
        ptr += 4;

        int targetHolderNameEnd = Tools.findEndOfString(bytes, ptr);
        String targetHolderName = new String(Arrays.copyOfRange(bytes, ptr, targetHolderNameEnd));
        ptr = targetHolderNameEnd + 1;

        Currency currency = Currency.fromByte(bytes[ptr]);
        ptr += 1;

        float amount = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getFloat();

        return new TransferRequest(accountNumber, holderName, password, targetAccountNumber, targetHolderName, currency, amount);
    }

    public static void main(String[] args) {
        TransferRequest request = new TransferRequest(1234, "abc", "password", 453, "def", Currency.USD, 103.34F);
        byte[] bytes = request.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(TransferRequest.fromBytes(bytes));
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                ", password='" + password + '\'' +
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

    public String getPassword() {
        return password;
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
        byte[] accountNumberByte = ByteBuffer.allocate(4).putInt(this.getAccountNumber()).array();
        byte[] holderNameByte = (this.getHolderName() + "\0").getBytes();
        byte[] passwordByte = (this.getPassword() + "\0").getBytes();
        byte[] targetAccountNumberByte = ByteBuffer.allocate(4).putInt(this.getTargetAccountNumber()).array();
        byte[] targetHolderNameByte = (this.getTargetHolderName() + "\0").getBytes();
        byte currencyByte = this.getCurrency().toByte();
        byte[] amountByte = ByteBuffer.allocate(4).putFloat(this.getAmount()).array();

        final int totalBytes = accountNumberByte.length + holderNameByte.length + passwordByte.length
                + targetAccountNumberByte.length + targetHolderNameByte.length
                + 1 + amountByte.length;
        byte[] bytes = new byte[totalBytes];
        int ptr = 0;
        System.arraycopy(accountNumberByte, 0, bytes, ptr, 4);
        ptr += 4;
        System.arraycopy(holderNameByte, 0, bytes, ptr, holderNameByte.length);
        ptr += holderNameByte.length;
        System.arraycopy(passwordByte, 0, bytes, ptr, passwordByte.length);
        ptr += passwordByte.length;
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
