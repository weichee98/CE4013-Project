package main.java.server.entity;

import main.java.shared.entity.Currency;

public class AccountInfo {
    private static final int PASSWORDLENGTH = 8;
    private int accountNum;
    private String holderName;
    private String password;
    private Currency currency;
    private float balance;

    public AccountInfo(int accountNum, String holderName, String password, Currency currency, float balance) throws Exception {
        this.accountNum = accountNum;
        this.holderName = holderName;
        this.password = checkPassword(password);
        this.currency = currency;
        this.balance = balance;
    }

    public static void main(String[] args) throws Exception {
        AccountInfo aci = new AccountInfo(123, "abc", "12345678", Currency.valueOf("USD"), 10.0F);
        System.out.println(aci);
    }

    public int getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        this.password = checkPassword(password);
    }

    private String checkPassword(String password) throws Exception {
        if (password.length() != PASSWORDLENGTH)
            throw new Exception(String.format("The password length must be %d. Input password is of length %d", PASSWORDLENGTH, password.length()));
        else
            return password;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "accountNum = " + accountNum +
                ", holderName = '" + holderName + '\'' +
                ", password = '" + password + '\'' +
                ", currency = " + currency +
                ", balance = " + balance +
                '}';
    }
}
