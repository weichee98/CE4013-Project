package main.java.client;

import main.java.shared.request.RequestBody;
import main.java.shared.request.RequestType;
import main.java.shared.request.SubscribeRequest;
import main.java.shared.response.Response;
import main.java.shared.response.SubscribeStatusResponse;

import java.time.Duration;

public class BankServicesUserInterface {
    private final Client client;

    public BankServicesUserInterface(Client client) {
        this.client = client;
    }

    public void runSubscribeUI() {
        int interval = SafeScanner.readInt("Subscribe interval (s) = ");

        Response resp = client.request(RequestType.SUBSCRIBE, new SubscribeRequest(interval));
        SubscribeStatusResponse respBody = SubscribeStatusResponse.fromBytes(resp.getRespBodyBytes());
        boolean success = respBody.isStatus();

        if (success) {
            System.out.println("Subscribed successfully, waiting for update messages...");
            client.poll(interval, update -> {
                if (update.isStatus())
                    System.out.println("Update: " + update.getSubscribeMessage());
                else
                    System.out.println("Error: " + update.getErrorMessage());
            });
            System.out.println("Subscription ended");
        } else {
            System.out.println("Failed to subscribe: " + respBody.getErrorMessage());
        }
    }
}
