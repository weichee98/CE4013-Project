package main.java.shared.response;

import main.java.shared.utils.Tools;

import java.util.Arrays;

public class SubscribeStatusResponse extends ResponseBody {
    private SubscribeStatusResponse(boolean status, String errorMessage) {
        super(status, errorMessage);
    }

    public static SubscribeStatusResponse error(String errorMessage) {
        return new SubscribeStatusResponse(false, errorMessage);
    }

    public static SubscribeStatusResponse success() {
        return new SubscribeStatusResponse(true, "");
    }

    public static SubscribeStatusResponse fromBytes(byte[] bytes) {
        boolean status = bytes[0] == 1;
        int errorMessageEnd = Tools.findEndOfString(bytes, 1);
        String errorMessage = new String(Arrays.copyOfRange(bytes, 1, errorMessageEnd));
        return new SubscribeStatusResponse(status, errorMessage);
    }

    @Override
    public byte[] toBytes() {
        byte statusByte = (byte) (this.isStatus() ? 1 : 0);
        byte[] errorMessageByte = (this.getErrorMessage() + "\0").getBytes();

        final int totalBytes = 1 + errorMessageByte.length;
        byte[] bytes = new byte[totalBytes];

        bytes[0] = statusByte;
        System.arraycopy(errorMessageByte, 0, bytes, 1, errorMessageByte.length);
        return bytes;
    }
}
