package main.java.server.service;

import main.java.server.database.AccountsList;
import main.java.server.entity.AccountInfo;
import main.java.shared.request.CloseAccountRequest;
import main.java.shared.request.OpenAccountRequest;
import main.java.shared.response.CloseAccountResponse;
import main.java.shared.response.OpenAccountResponse;

public class BankServices {
    private final AccountsList db = new AccountsList();

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
        } catch (Exception e) {
            return OpenAccountResponse.error(e.getMessage());
        };
        return OpenAccountResponse.success(accountNumber);
    }

    public CloseAccountResponse closeAccount(CloseAccountRequest req) {
        int accountNumber = req.getAccountNumber();
        String holderName = req.getHolderName();
        String password = req.getPassword();
        try {
            db.delete(accountNumber, password, holderName);
        } catch (Exception e) {
            return CloseAccountResponse.error(e.getMessage());
        };
        return CloseAccountResponse.success(accountNumber, holderName);
    }

}
