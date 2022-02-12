package main.java.shared.request;

import main.java.shared.entity.Currency;

public class OpenAccountRequest extends RequestBody{
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

    @Override
    public byte[] toBytes() {
//        TODO - toBytes
        return new byte[0];
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
