package main.java.shared.entity;

import java.util.Arrays;

public enum Currency {
    //    For error or invalid currency
    ZZZ("ZZZ"),
    //    Valid currency
    USD("USD"),
    SGD("SGD"),
    RMB("RMB");


    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public static Currency[] getValidCurrencies() {
        Currency[] allCurrencies = Currency.values();
        return Arrays.copyOfRange(allCurrencies, 1, allCurrencies.length);
    }

    public static Currency fromByte(byte currencyByte) {
        try {
            return Currency.values()[currencyByte];
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(Currency.valueOf("USD"));
        Currency[] allCurrencies = Currency.values();
        System.out.println(Arrays.toString(allCurrencies));
    }

    public byte toByte() {
        return (byte) this.ordinal();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
