package main.java.shared.response;

import main.java.shared.entity.Currency;
import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DepositAndWithdrawResponse extends ResponseBody {
    private final int accountNumber;
    private final String holderName;
    private final Currency currency;
    private final float balance;

    public DepositAndWithdrawResponse(boolean status, String errorMessage, int accountNumber, String holderName, Currency currency, float balance) {
        super(status, errorMessage);
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.currency = currency;
        this.balance = balance;
    }

    public static DepositAndWithdrawResponse error(String errorMessage) {
        return new DepositAndWithdrawResponse(false, errorMessage, 0, "", Currency.ZZZ, 0F);
    }

    public static DepositAndWithdrawResponse success(int accountNumber, String holderName, String password, Currency currency, float balance) {
        return new DepositAndWithdrawResponse(true, "", accountNumber, holderName, currency, balance);
    }

    public static DepositAndWithdrawResponse fromBytes(byte[] bytes) {
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

        Currency currency = Currency.fromByte(bytes[ptr]);
        ptr += 1;

        float balance = ByteBuffer.wrap(Arrays.copyOfRange(bytes, ptr, ptr + 4)).getFloat();

        return new DepositAndWithdrawResponse(status, errorMessage, accountNumber, holderName, currency, balance);
    }

    public static void main(String[] args) {
        DepositAndWithdrawResponse response = new DepositAndWithdrawResponse(true, "errorMessage", 1234, "abc", Currency.USD, -103.34F);
        byte[] bytes = response.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(DepositAndWithdrawResponse.fromBytes(bytes));
    }

    @Override
    public String toString() {
        return "DepositAndWithdrawResponse{" +
                "accountNumber=" + accountNumber +
                ", holderName='" + holderName + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                ", errorMessage='" + errorMessage + '\'' +
                ", status=" + status +
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

    public float getBalance() {
        return balance;
    }

    @Override
    public byte[] toBytes() {
        byte statusByte = (byte) (this.isStatus() ? 1 : 0);
        byte[] errorMessageByte = (this.getErrorMessage() + "\0").getBytes();
        byte[] accountNumberByte = ByteBuffer.allocate(4).putInt(this.getAccountNumber()).array();
        byte[] holderNameByte = (this.getHolderName() + "\0").getBytes();
        byte currencyByte = this.getCurrency().toByte();
        byte[] balanceByte = ByteBuffer.allocate(4).putFloat(this.getBalance()).array();

        final int totalBytes = 1 + errorMessageByte.length + accountNumberByte.length + holderNameByte.length + 1 + balanceByte.length;
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
        bytes[ptr] = currencyByte;
        ptr += 1;
        System.arraycopy(balanceByte, 0, bytes, ptr, 4);

        return bytes;
    }

}
