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

    public boolean delete(Integer accountNum, String password){
        if (db.get(accountNum) == null){
            System.out.printf("Account %d does not exist%n", accountNum);
            return false;
        }
        else if (!Objects.equals(db.get(accountNum).getPassword(), password)){
            System.out.println("Wrong password");
            return false;
        }
        this.db.remove(accountNum);
        return true;
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
        acl.delete(aci.getAccountNum(), "12345678");
        acl.delete(aci2.getAccountNum(), "12443");
        System.out.println(acl);
    }
}
