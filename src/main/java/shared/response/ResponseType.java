package main.java.shared.response;

public enum ResponseType {
    INVALID,
    OPEN_ACCOUNT,
    CLOSE_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    QUERY_ACCOUNT,
    SUBSCRIBE_STATUS,
    SUBSCRIBE_UPDATE;

    public static ResponseType get(int id) {
        try {
            return ResponseType.values()[id];
        } catch (Exception e) {
            return null;
        }
    }

    public static ResponseType fromByte(byte idByte) {
        return ResponseType.get(idByte);
    }

    public static void main(String[] args) {
        System.out.println(ResponseType.get(3));
        System.out.println(ResponseType.get(10));
    }

    public byte toByte() {
        return (byte) this.ordinal();
    }
}
