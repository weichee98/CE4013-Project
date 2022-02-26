package main.java.server.database;

import main.java.server.entity.AccountInfo;
import main.java.shared.entity.Currency;

import java.util.HashMap;

public class AccountsList {
    private static final int minAccountNum = 10000;
    private static final int maxAccountNum = 99999;
    private final HashMap<Integer, AccountInfo> db = new HashMap<>();

    public static void main(String[] args) throws Exception {
        AccountsList acl = new AccountsList();
        int i1 = acl.generateAccountNum();
        int i2 = acl.generateAccountNum();
        AccountInfo aci = new AccountInfo(
                i1, "abc", "12345678", Currency.valueOf("USD"), 10.0F
        );
        AccountInfo aci2 = new AccountInfo(
                i2, "abc", "12345678", Currency.valueOf("USD"), 10.0F
        );
        acl.add(i1, aci);
        acl.add(i2, aci2);
        System.out.println(acl);
        acl.delete(aci.getAccountNum(), "12345678", "abc");
        System.out.println(acl);
        System.out.println(acl.getAccountInfo(1));
    }

    public int generateAccountNum() {
        while (true) {
            int i = (int) ((Math.random() * (maxAccountNum - minAccountNum)) + minAccountNum);
            if (db.get(i) == null) return i;
        }
    }

    public void add(int accountNum, AccountInfo accountInfo) {
        this.db.put(accountNum, accountInfo);
    }

    public AccountInfo delete(int accountNum, String password, String holderName) throws Exception {
        AccountInfo accountInfo = this.getAccountInfo(accountNum);
        accountInfo.validatePassword(password);
        accountInfo.validateHolderName(holderName);
        this.db.remove(accountNum);
        return accountInfo;
    }

    public AccountInfo getAccountInfo(int accountNum) throws Exception {
        AccountInfo aci = this.db.getOrDefault(accountNum, null);
        if (aci == null) throw new Exception(String.format("Account %d does not exist", accountNum));
        else return aci;
    }

    @Override
    public String toString() {
        return "AccountsList{" +
                "db=" + db +
                '}';
    }
}
