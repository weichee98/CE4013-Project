package main.java.shared.utils;

public class Tools {
    public static int findEndOfString(byte[] bytes, int start) {
        for (int i = start; i < bytes.length; i++) {
            if (bytes[i] == (byte) '\0') return i;
        }
        return -1;
    }
}
