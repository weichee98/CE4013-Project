package main.java.client;

import java.util.Scanner;

public class SafeScanner {
    private static final Scanner reader = new Scanner(System.in);

    public static int readInt(String message) {
        try {
            System.out.print(message);
            return Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please input an integer!");
            return readInt(message);
        }
    }

    public static double readFloat(String message) {
        try {
            System.out.print(message);
            return Float.parseFloat(reader.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please input a number!");
            return readFloat(message);
        }
    }

    public static String readLine(String message) {
        System.out.print(message);
        return reader.nextLine();
    }

    public static void closeReader() {
        reader.close();
    }
}
