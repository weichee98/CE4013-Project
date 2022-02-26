package main.java.shared.response;

import main.java.shared.utils.Tools;

import java.util.Arrays;

public class SubscribeMessageResponse extends ResponseBody {
    private final String subscribeMessage;

    public SubscribeMessageResponse(String subscribeMessage) {
        super(true, "");
        this.subscribeMessage = subscribeMessage;
    }

    public static SubscribeMessageResponse fromBytes(byte[] bytes) {
        int subscribeMessageEnd = Tools.findEndOfString(bytes, 0);
        String subscribeMessage = new String(Arrays.copyOfRange(bytes, 0, subscribeMessageEnd));
        return new SubscribeMessageResponse(subscribeMessage);
    }

    public String getSubscribeMessage() {
        return subscribeMessage;
    }

    @Override
    public byte[] toBytes() {
        return (this.subscribeMessage + "\0").getBytes();
    }
}
