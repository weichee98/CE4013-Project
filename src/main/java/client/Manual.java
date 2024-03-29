package main.java.client;

public enum Manual {
    STOP("Stop the client"),
    OPEN_ACCOUNT("Open a new bank account"),
    CLOSE_ACCOUNT("Close a bank account"),
    DEPOSIT("Deposit to a bank account"),
    WITHDRAW("Withdraw from a bank account"),
    TRANSFER("Transfer money to another bank account"),
    QUERY_ACCOUNT("Query information about a bank account"),
    SUBSCRIBE("Subscribe to updates form all accounts");

    private final String description;

    Manual(String description) {
        this.description = description;
    }

    public static Manual get(int id) {
        try {
            return Manual.values()[id];
        } catch (Exception e) {
            return null;
        }
    }

    public static void printManual() {
        final String divider = "=".repeat(64);
        Manual[] manuals = Manual.values();

        System.out.println();
        System.out.println(divider);
        System.out.println(
                String.format(
                        "Please choose a service by typing [%d-%d]:",
                        1, manuals.length - 1
                )
        );
        for (int i = 0; i < manuals.length; i++) {
            Manual manual = manuals[i];
            System.out.println(
                    String.format("%d: %s", i, manual.getDescription())
            );
        }
        System.out.println(divider);
    }

    public static void main(String[] args) {
        printManual();
    }

    public String getDescription() {
        return description;
    }

}
