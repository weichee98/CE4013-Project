package main.java.client;

import main.java.shared.entity.Currency;
import main.java.shared.request.*;
import main.java.shared.response.*;

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

    private String askName(String label) {
        return SafeScanner.readLine(String.format("%s = ", label));
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

    private int askAccountNumber(String label) {
        return SafeScanner.readInt(String.format("%s = ", label));
    }

    private Currency askCurrency() {
        System.out.println(
                String.format("Your currency choices: %s", Arrays.toString(Currency.getValidCurrencies()))
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

    public void runCloseAccountUI() {
        this.printHeader("Close Account");
        int accountNumber = this.askAccountNumber();
        String holderName = this.askName();
        String password = this.askPassword();
        Response resp = client.request(
                RequestType.CLOSE_ACCOUNT,
                new CloseAccountRequest(accountNumber, holderName, password)
        );
        CloseAccountResponse respBody = CloseAccountResponse.fromBytes(resp.getRespBodyBytes());
        if (respBody.isStatus()) {
            System.out.println(
                    String.format("Successfully closed bank account %d", respBody.getAccountNumber())
            );
        } else {
            System.out.println(
                    String.format("Failed to close bank account: %s", respBody.getErrorMessage())
            );
        }
    }

    public void runDepositUI() {
        this.printHeader("Deposit");
        int accountNumber = this.askAccountNumber();
        String holderName = this.askName();
        String password = this.askPassword();
        Currency currency = this.askCurrency();
        float amount = this.askAmount();
        Response resp = client.request(
                RequestType.DEPOSIT,
                new DepositAndWithdrawRequest(accountNumber, holderName, password, currency, amount)
        );
        DepositAndWithdrawResponse respBody = DepositAndWithdrawResponse.fromBytes(resp.getRespBodyBytes());
        if (respBody.isStatus()) {
            System.out.println(
                    String.format(
                            "Successfully deposited %s%.2f to bank account %d\nBalance: %s%.2f",
                            respBody.getCurrency(), amount, respBody.getAccountNumber(),
                            respBody.getCurrency(), respBody.getBalance()
                    )
            );
        } else {
            System.out.println(
                    String.format("Failed to deposit: %s", respBody.getErrorMessage())
            );
        }
    }

    public void runWithdrawUI() {
        this.printHeader("Withdraw");
        int accountNumber = this.askAccountNumber();
        String holderName = this.askName();
        String password = this.askPassword();
        Currency currency = this.askCurrency();
        float amount = this.askAmount();
        Response resp = client.request(
                RequestType.WITHDRAW,
                new DepositAndWithdrawRequest(accountNumber, holderName, password, currency, -amount)
        );
        DepositAndWithdrawResponse respBody = DepositAndWithdrawResponse.fromBytes(resp.getRespBodyBytes());
        if (respBody.isStatus()) {
            System.out.println(
                    String.format(
                            "Successfully withdraw %s%.2f from bank account %d\nBalance: %s%.2f",
                            respBody.getCurrency(), amount, respBody.getAccountNumber(),
                            respBody.getCurrency(), respBody.getBalance()
                    )
            );
        } else {
            System.out.println(
                    String.format("Failed to withdraw: %s", respBody.getErrorMessage())
            );
        }
    }

    public void runTransferUI() {
        this.printHeader("Transfer");
        int targetAccountNumber = this.askAccountNumber("Enter recepient account number");
        String targetHolderName = this.askName("Enter recepient name");
        int accountNumber = this.askAccountNumber("Enter sender account number");
        String holderName = this.askName("Enter sender name");
        String password = this.askPassword();
        Currency currency = this.askCurrency();
        float amount = this.askAmount();
        Response resp = client.request(
                RequestType.TRANSFER,
                new TransferRequest(
                        accountNumber, holderName, password, targetAccountNumber, targetHolderName, currency, amount
                )
        );
        TransferResponse respBody = TransferResponse.fromBytes(resp.getRespBodyBytes());
        if (respBody.isStatus()) {
            System.out.println(
                    String.format(
                            "Successfully transfer %s%.2f from bank account %d to %d",
                            respBody.getCurrency(), respBody.getAmount(),
                            respBody.getAccountNumber(), respBody.getTargetAccountNumber()
                    )
            );
        } else {
            System.out.println(
                    String.format("Failed to transfer: %s", respBody.getErrorMessage())
            );
        }
    }

    public void runQueryAccountUI() {
        this.printHeader("Query Account");
        int accountNumber = this.askAccountNumber();
        String holderName = this.askName();
        String password = this.askPassword();
        Response resp = client.request(
                RequestType.QUERY_ACCOUNT,
                new QueryAccountRequest(
                        accountNumber, holderName, password
                )
        );
        QueryAccountResponse respBody = QueryAccountResponse.fromBytes(resp.getRespBodyBytes());
        if (respBody.isStatus()) {
            System.out.println(
                    String.format(
                            "Query Result:\n\tAccount Number: %d\n\tHolder Name: %s\n\tBalance: %s%.2f",
                            respBody.getAccountNumber(), respBody.getHolderName(),
                            respBody.getCurrency(), respBody.getBalance()
                    )
            );
        } else {
            System.out.println(
                    String.format("Failed to query account: %s", respBody.getErrorMessage())
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
