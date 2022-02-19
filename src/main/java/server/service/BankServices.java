package main.java.server.service;

import main.java.server.database.AccountsList;
import main.java.server.entity.AccountInfo;
import main.java.shared.entity.Currency;
import main.java.shared.request.*;
import main.java.shared.response.*;

import java.net.SocketAddress;

public class BankServices {
    private final AccountsList db = new AccountsList();
    private final Subscription subscription;

    public BankServices(Subscription subscription) {
        this.subscription = subscription;
    }

    public OpenAccountResponse openAccount(OpenAccountRequest req) {
        int accountNumber = db.generateAccountNum();
        try {
            AccountInfo accountInfo = new AccountInfo(
                    accountNumber,
                    req.getHolderName(),
                    req.getPassword(),
                    req.getCurrency(),
                    req.getBalance()
            );
            db.add(accountNumber, accountInfo);
            this.subscription.broadcastMessage(
                    String.format("Opened account %s", accountInfo)
            );
            return OpenAccountResponse.success(accountNumber);
        } catch (Exception e) {
            return OpenAccountResponse.error(e.getMessage());
        }
    }

    public CloseAccountResponse closeAccount(CloseAccountRequest req) {
        int accountNumber = req.getAccountNumber();
        String holderName = req.getHolderName();
        String password = req.getPassword();
        try {
            AccountInfo accountInfo = db.delete(accountNumber, password, holderName);
            this.subscription.broadcastMessage(
                    String.format("Closed account %s", accountInfo.toString())
            );
            return CloseAccountResponse.success(accountNumber, holderName);
        } catch (Exception e) {
            return CloseAccountResponse.error(e.getMessage());
        }
    }

    public SubscribeStatusResponse requestSubscription(SocketAddress address, SubscribeRequest req) {
        try {
            long interval = req.getInterval();
            this.subscription.subscribe(address, interval);
            return SubscribeStatusResponse.success();
        } catch (Exception e) {
            return SubscribeStatusResponse.error(e.getMessage());
        }
    }

    public DepositAndWithdrawResponse depositAndWithdraw(DepositAndWithdrawRequest req) {
        int accountNumber = req.getAccountNumber();
        String holderName = req.getHolderName();
        String password = req.getPassword();
        Currency currency = req.getCurrency();
        float amount = req.getAmount();

        try {
            AccountInfo accountInfo = db.getAccountInfo(accountNumber);
            accountInfo.validatePassword(password);
            accountInfo.validateHolderName(holderName);
            accountInfo.validateCurrency(currency);
            accountInfo.checkEnoughBalance(amount);
            float newBalance = accountInfo.getBalance() - amount;
            accountInfo.setBalance(newBalance);

            this.subscription.broadcastMessage(
                    String.format("New deposit/withdrawal %s", req)
            );
            return DepositAndWithdrawResponse.success(accountNumber, holderName, currency, newBalance);
        } catch (Exception e) {
            return DepositAndWithdrawResponse.error(e.getMessage());
        }
    }

    public QueryAccountResponse queryAccount(QueryAccountRequest req) {
        int accountNumber = req.getAccountNumber();
        String holderName = req.getHolderName();
        String password = req.getPassword();
        try {
            AccountInfo accountInfo = db.getAccountInfo(accountNumber);
            accountInfo.validatePassword(password);
            accountInfo.validateHolderName(holderName);

            this.subscription.broadcastMessage(
                    String.format("Account information %s", accountInfo)
            );
            return QueryAccountResponse.success(accountNumber, holderName, accountInfo.getCurrency(), accountInfo.getBalance());
        } catch (Exception e) {
            return QueryAccountResponse.error(e.getMessage());
        }
    }

    public TransferResponse transfer(TransferRequest req) {
        int accountNumber = req.getAccountNumber();
        String holderName = req.getHolderName();
        String password = req.getPassword();
        int targetAccountNumber = req.getTargetAccountNumber();
        String targetHolderName = req.getTargetHolderName();
        Currency currency = req.getCurrency();
        float amount = req.getAmount();

        try {
            if (amount <= 0) throw new Exception("Transfer amount must be greater than 0");

            AccountInfo accountInfo = db.getAccountInfo(accountNumber);
            accountInfo.validatePassword(password);
            accountInfo.validateHolderName(holderName);
            accountInfo.validateCurrency(currency);
            accountInfo.checkEnoughBalance(-amount);
            float newBalance = accountInfo.getBalance() - amount;
            accountInfo.setBalance(newBalance);

            AccountInfo targetAccountInfo = db.getAccountInfo(targetAccountNumber);
            targetAccountInfo.validateHolderName(targetHolderName);
            if (currency != targetAccountInfo.getCurrency())
                throw new Exception(String.format("Recipient does not have account in currency %s", currency));
            targetAccountInfo.setBalance(targetAccountInfo.getBalance() + amount);

            TransferResponse resp = TransferResponse.success(accountNumber, holderName, targetAccountNumber, targetHolderName, currency, amount);

            this.subscription.broadcastMessage(
                    String.format("Transaction completed %s", resp)
            );
            return resp;

        } catch (Exception e) {
            return TransferResponse.error(e.getMessage());
        }
    }
}
