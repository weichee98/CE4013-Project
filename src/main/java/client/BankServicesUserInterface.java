package main.java.client;

import main.java.shared.entity.Currency;
import main.java.shared.request.OpenAccountRequest;
import main.java.shared.request.RequestType;
import main.java.shared.request.SubscribeRequest;
import main.java.shared.response.OpenAccountResponse;
import main.java.shared.response.Response;
import main.java.shared.response.SubscribeStatusResponse;

import java.util.Arrays;

public class BankServicesUserInterface {
    private final static int PASSWORD_LENGTH = 8;
    private final Client client;

    public BankServicesUserInterface(Client client) {
        this.client = client;
    }

    public void printHeader(String message) {
        System.out.println();
        System.out.println("=".repeat(message.length()));
        System.out.println(message);
        System.out.println("=".repeat(message.length()));
    }

    private String askName() {
        return SafeScanner.readLine("Enter name = ");
    }

    private String askPassword() {
        String password = SafeScanner.readLine(
                String.format("Enter password (%d characters) = ", PASSWORD_LENGTH)
        );
        if (password.length() != PASSWORD_LENGTH) {
            System.out.println(
                    String.format("Password must have exactly %d characters!", PASSWORD_LENGTH)
            );
            return this.askPassword();
        }
        return password;
    }

    private int askAccountNumber() {
        return SafeScanner.readInt("Enter account number = ");
    }

    private Currency askCurrency() {
        System.out.println(
                String.format("Your currency choices: %s", Arrays.toString(Currency.values()))
        );
        String currencyStr = SafeScanner.readLine("Enter currency = ").toUpperCase();
        try {
            Currency currency = Currency.valueOf(currencyStr);
            return currency;
        } catch (Exception e) {
            System.out.println("Invalid currency!");
            return this.askCurrency();
        }
    }

    private float askAmount() {
        float amount = SafeScanner.readFloat("Enter amount of money = ");
        if (amount < 0) {
            System.out.println("Amount cannot be negative!");
            return this.askAmount();
        }
        return amount;
    }

    private int askInterval() {
        int interval = SafeScanner.readInt("Enter subscribe interval (s) = ");
        if (interval < 0) {
            System.out.println("Interval cannot be negative!");
            return this.askInterval();
        }
        return interval;
    }

    public void runOpenAccountUI() {
        this.printHeader("Open Account");
        String holderName = this.askName();
        String password = this.askPassword();
        Currency currency = this.askCurrency();
        float balance = this.askAmount();
        Response resp = client.request(
                RequestType.OPEN_ACCOUNT,
                new OpenAccountRequest(holderName, password, currency, balance)
        );
        OpenAccountResponse respBody = OpenAccountResponse.fromBytes(resp.getRespBodyBytes());
        if (respBody.isStatus()) {
            System.out.println(
                    String.format("Successfully created bank account %d", respBody.getAccountNumber())
            );
        } else {
            System.out.println(
                    String.format("Failed to create bank account: %s", respBody.getErrorMessage())
            );
        }
    }

    public void runSubscribeUI() {
        this.printHeader("Subscribe to Updates");
        int interval = this.askInterval();

        Response resp = client.request(RequestType.SUBSCRIBE, new SubscribeRequest(interval));
        SubscribeStatusResponse respBody = SubscribeStatusResponse.fromBytes(resp.getRespBodyBytes());
        boolean success = respBody.isStatus();

        if (success) {
            System.out.println("Subscribed successfully, waiting for update messages...");
            client.poll(interval, update -> {
                if (update.isStatus())
                    System.out.println("Update: " + update.getSubscribeMessage());
                else
                    System.out.println("Error: " + update.getErrorMessage());
            });
            System.out.println("Subscription ended");
        } else {
            System.out.println("Failed to subscribe: " + respBody.getErrorMessage());
        }
    }
}
