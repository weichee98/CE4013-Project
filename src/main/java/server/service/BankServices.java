package main.java.server.service;

import main.java.server.database.AccountsList;
import main.java.server.entity.AccountInfo;
import main.java.shared.request.CloseAccountRequest;
import main.java.shared.request.OpenAccountRequest;
import main.java.shared.request.SubscribeRequest;
import main.java.shared.response.CloseAccountResponse;
import main.java.shared.response.OpenAccountResponse;
import main.java.shared.response.SubscribeStatusResponse;

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
                    String.format("Opened account %s", accountInfo.toString())
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

}
