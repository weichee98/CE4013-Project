package main.java.shared.request;

public enum RequestType {
    INVALID,
    OPEN_ACCOUNT,
    CLOSE_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    SUBSCRIBE;

    public static RequestType get(int id) {
        try {
            return RequestType.values()[id];
        } catch (Exception e) {
            return null;
        }
    }

    public static RequestType fromByte(byte idByte) {
        return RequestType.get(idByte);
    }

    public static void main(String[] args) {
        System.out.println(RequestType.get(3));
        System.out.println(RequestType.get(10));
    }

    public byte toByte() {
        return (byte) this.ordinal();
    }
}
