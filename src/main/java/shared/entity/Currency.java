package main.java.shared.entity;

import java.util.Arrays;

public enum Currency {
    USD("USD"),
    SGD("SGD"),
    RMB("RMB");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static void main(String[] args) {
        System.out.println(Currency.valueOf("USD"));
        Currency[] allCurrencies = Currency.values();
        System.out.println(Arrays.toString(allCurrencies));
    }
}
