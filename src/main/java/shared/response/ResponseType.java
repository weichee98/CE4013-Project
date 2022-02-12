package main.java.shared.response;

public enum ResponseType {
    OPEN_ACCOUNT,
    CLOSE_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    MONITOR,
    UPDATES;

    public static ResponseType get(int id) {
        try {
            return ResponseType.values()[id];
        } catch (Exception e) {
            return null;
        }
    }

    public byte toByte() {
        return (byte) this.ordinal();
    }

    public static ResponseType fromByte(byte idByte) {
        return ResponseType.get(idByte);
    }

    public static void main(String[] args) {
        System.out.println(ResponseType.get(3));
        System.out.println(ResponseType.get(10));
    }
}
