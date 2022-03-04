package main.java.client;

import main.java.shared.request.Request;
import main.java.shared.request.RequestBody;
import main.java.shared.request.RequestHeader;
import main.java.shared.request.RequestType;
import main.java.shared.response.Response;
import main.java.shared.response.ResponseHeader;
import main.java.shared.response.ResponseType;
import main.java.shared.response.SubscribeMessageResponse;
import main.java.shared.udp.UDPClient;
import main.java.shared.udp.UDPMessage;

import java.io.InterruptedIOException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

public class Client {
    private final UDPClient udpClient;
    private final SocketAddress serverAddress;
    private final int maxAttempts;

    public Client(UDPClient udpClient, SocketAddress serverAddress, int maxAttempts) {
        this.udpClient = udpClient;
        this.serverAddress = serverAddress;
        this.maxAttempts = maxAttempts;
    }

    public Response request(RequestType requestType, RequestBody requestBody) throws RuntimeException {
        byte[] bytes = requestBody.toBytes();
        RequestHeader reqHeader = new RequestHeader(UUID.randomUUID(), requestType, bytes.length);
        Request req = new Request(reqHeader, bytes);
        UDPMessage rawReqMessage = new UDPMessage(this.serverAddress, req.toBytes());

        for (int i = this.maxAttempts; i > 0; i--) {
            try {
                this.udpClient.send(rawReqMessage);
                try (UDPMessage rawResp = this.udpClient.receive()) {
                    Response resp = Response.fromBytes(rawResp.getBytes());
                    ResponseHeader respHeader = resp.getHeader();
                    if (!respHeader.getUUID().equals(reqHeader.getUUID())) {
                        continue;
                    }
                    if (respHeader.getResponseType() == ResponseType.INVALID) {
                        throw new RuntimeException("Invalid response received");
                    }
                    return resp;
                }
            } catch (RuntimeException e) {
                if (e.getCause() instanceof SocketTimeoutException) {
                    System.out.println("Socket timeout, retrying...");
                    continue;
                }
                throw e;
            }
        }
        throw new RuntimeException("No response received");
    }

    public void poll(int interval, Consumer<SubscribeMessageResponse> callback) {
        Instant end = Instant.now().plusSeconds(interval);

        Thread pollingThread = new Thread(() -> {
            for (; ; ) {
                if (Instant.now().isAfter(end)) {
                    return;
                }
                try (UDPMessage msg = udpClient.receive()) {
                    Response resp = Response.fromBytes(msg.getBytes());
                    SubscribeMessageResponse respBody = SubscribeMessageResponse.fromBytes(
                            resp.getRespBodyBytes()
                    );
                    callback.accept(respBody);
                } catch (RuntimeException e) {
                    if (e.getCause() instanceof SocketTimeoutException) {
                        continue;
                    }
                    if (e.getCause() instanceof InterruptedIOException) {
                        return;
                    }
                }
            }
        });
        pollingThread.start();

        try {
            pollingThread.join(Duration.ofSeconds(interval).toMillis());
        } catch (InterruptedException e) {
        }
        pollingThread.interrupt();
    }

}
