package main.java.client;

import main.java.shared.udp.UDPClient;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.time.Duration;

public class Main {
    private static Manual askUserChoice() throws Exception {
        int id = SafeScanner.readInt("Your choice = ");
        Manual choice = Manual.get(id);
        if (choice == null) {
            throw new Exception(String.format("Invalid user choice %d", id));
        }
        return choice;
    }

    public static void main(String[] args) throws SocketException {
        final String clientHost = "0.0.0.0";
        final String serverHost = "127.0.0.1";
        final int clientPort = 12741;
        final int serverPort = 12740;
        final Duration timeout = Duration.ofSeconds(5);
        final int maxAttempts = 5;
        final int bufferSize = 1024;

        DatagramSocket socket = new DatagramSocket(new InetSocketAddress(clientHost, clientPort));
        socket.setSoTimeout((int) timeout.toMillis());
        UDPClient udpClient = new UDPClient(socket, bufferSize);
        InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

        Client client = new Client(udpClient, serverAddress, maxAttempts);
        BankServicesUserInterface ui = new BankServicesUserInterface(client);
        boolean shouldStop = false;

        while (!shouldStop) {
            Manual.printManual();
            try {
                Manual userChoice = askUserChoice();
                switch (userChoice) {
                    case OPEN_ACCOUNT:
                        ui.runOpenAccountUI();
                        break;
                    case CLOSE_ACCOUNT:
                        ui.runCloseAccountUI();
                        break;
                    case DEPOSIT:
                        ui.runDepositUI();
                        break;
                    case WITHDRAW:
                        ui.runWithdrawUI();
                        break;
                    case TRANSFER:
                        ui.runTransferUI();
                        break;
                    case QUERY_ACCOUNT:
                        ui.runQueryAccountUI();
                        break;
                    case SUBSCRIBE:
                        ui.runSubscribeUI();
                        break;
                    case STOP:
                        shouldStop = true;
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        SafeScanner.closeReader();
        System.out.println("Stopped client");
    }
}
