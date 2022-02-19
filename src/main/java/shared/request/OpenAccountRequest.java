package main.java.shared.request;

import main.java.shared.entity.Currency;
import main.java.shared.utils.Tools;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class OpenAccountRequest extends RequestBody {
    private final String holderName;
    private final String password;
    private final Currency currency;
    private final float balance;

    public OpenAccountRequest(String holderName, String password, Currency currency, float balance) {
        this.holderName = holderName;
        this.password = password;
        this.currency = currency;
        this.balance = balance;
    }

    public static OpenAccountRequest fromBytes(byte[] bytes) {
        int ptr = 0;

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

        Currency currency = Currency.fromByte(bytes[ptr]);
        ptr += 1;

        float balance = ByteBuffer.wrap(
                Arrays.copyOfRange(bytes, ptr, ptr + 4)
        ).getFloat();

        return new OpenAccountRequest(
                holderName,
                password,
                currency,
                balance
        );
    }

    public static void main(String[] args) {
        OpenAccountRequest o = new OpenAccountRequest(
                "abc",
                "asdagasgsk",
                Currency.USD,
                (float) 123.45678
        );
        byte[] bytes = o.toBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(OpenAccountRequest.fromBytes(bytes));
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

    public float getBalance() {
        return balance;
    }

    @Override
    public byte[] toBytes() {
        byte[] holderNameByte = (this.holderName + "\0").getBytes();
        byte[] passwordByte = (this.password + "\0").getBytes();
        byte currencyByte = this.currency.toByte();
        byte[] balanceByte = ByteBuffer.allocate(4).putFloat(this.balance).array();

        final int totalBytes = 1 + holderNameByte.length +
                passwordByte.length + balanceByte.length;
        byte[] bytes = new byte[totalBytes];

        int ptr = 0;
        System.arraycopy(
                holderNameByte, 0, bytes, ptr, holderNameByte.length
        );
        ptr += holderNameByte.length;

        System.arraycopy(
                passwordByte, 0, bytes, ptr, passwordByte.length
        );
        ptr += passwordByte.length;

        bytes[ptr] = currencyByte;
        ptr += 1;

        System.arraycopy(
                balanceByte, 0, bytes, ptr, balanceByte.length
        );
        return bytes;
    }

    @Override
    public String toString() {
        return "OpenAccountRequest{" +
                "holderName='" + holderName + '\'' +
                ", password='" + password + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                '}';
    }
}
