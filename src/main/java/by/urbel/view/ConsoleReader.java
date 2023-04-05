package by.urbel.view;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleReader {
    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt() {
        while (true) {
            if (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                scanner.nextLine();
                return number;
            } else {
                scanner.nextLine();
                System.out.println("Invalid value. Enter number!");
            }
        }
    }

    public static long readLong() {
        while (true) {
            if (scanner.hasNextLong()) {
                long number = scanner.nextLong();
                scanner.nextLine();
                return number;
            } else {
                scanner.nextLine();
                System.out.println("Invalid value. Enter number!");
            }
        }
    }

    public static String readNextLine() {
        while (true) {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                System.out.println("Invalid value. Enter string!");
            }
        }
    }

    public static String readPassword() {
        if (System.console() != null) {
            return new String(System.console().readPassword());
        } else {
            while (true) {
                if (scanner.hasNextLine()) {
                    return scanner.nextLine();
                } else {
                    System.out.println("Invalid value. Enter string!");
                }
            }
        }
    }
}
