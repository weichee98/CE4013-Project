package main.java.server.database;

import main.java.server.entity.AccountInfo;
import main.java.shared.entity.Currency;

import java.util.HashMap;
import java.util.Objects;

public class AccountsList {
    private HashMap<Integer, AccountInfo> db = new HashMap<Integer, AccountInfo>();
    private static final int minAccountNum = 10000;
    private static final int maxAccountNum = 99999;

    public int generateAccountNum() {
        while(true) {
            int i = (int) ((Math.random() * (maxAccountNum - minAccountNum)) + minAccountNum);
            if (db.get(i) == null) return i;
        }
    }

    public void add(Integer accountNum, AccountInfo accountInfo){
        this.db.put(accountNum, accountInfo);
    }

    public HashMap<Integer, AccountInfo> getDb() {
        return db;
    }

    public void delete(Integer accountNum, String password, String holderName) throws Exception {
        if (db.get(accountNum) == null){
            throw new Exception(String.format("Account %d does not exist%n", accountNum));
        }
        else if  (!Objects.equals(db.get(accountNum).getHolderName(), holderName)){
            throw new Exception("Name does not match account number");
        }
        else if (!Objects.equals(db.get(accountNum).getPassword(), password)){
            throw new Exception("Wrong password");
        }
        this.db.remove(accountNum);
    }

    @Override
    public String toString() {
        return "AccountsList{" +
                "db=" + db +
                '}';
    }

    public static void main(String[] args) throws Exception {
        AccountsList acl = new AccountsList();
        int i1 = acl.generateAccountNum();
        int i2 = acl.generateAccountNum();
        AccountInfo aci = new AccountInfo(i1, "abc", "12345678", Currency.valueOf("USD"), 10.0F);
        AccountInfo aci2 = new AccountInfo(i2, "abc", "12345678", Currency.valueOf("USD"), 10.0F);
        acl.add(i1, aci);
        acl.add(i2, aci2);
        System.out.println(acl);
        acl.delete(aci.getAccountNum(), "12345678", "abc");
        acl.delete(aci2.getAccountNum(), "12443", "abc");
        System.out.println(acl);
    }
}
