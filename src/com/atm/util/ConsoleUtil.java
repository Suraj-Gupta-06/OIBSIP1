package com.atm.util;

import java.util.Scanner;

/**
 * Console Utility Class
 * Provides methods for better console UI and user interaction
 */
public class ConsoleUtil {
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Clear console (works on most terminals)
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing fails, just add some newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Print header with styling
     */
    public static void printHeader(String title) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║" + centerText(title, 59) + "║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }

    /**
     * Print section divider
     */
    public static void printDivider() {
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    /**
     * Print success message
     */
    public static void printSuccess(String message) {
        System.out.println("\n✓ SUCCESS: " + message);
    }

    /**
     * Print error message
     */
    public static void printError(String message) {
        System.out.println("\n✗ ERROR: " + message);
    }

    /**
     * Print warning message
     */
    public static void printWarning(String message) {
        System.out.println("\n⚠ WARNING: " + message);
    }

    /**
     * Print info message
     */
    public static void printInfo(String message) {
        System.out.println("\nℹ INFO: " + message);
    }

    /**
     * Center text within a given width
     */
    public static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    /**
     * Print menu options
     */
    public static void printMenu(String[] options) {
        System.out.println();
        for (int i = 0; i < options.length; i++) {
            System.out.printf("  [%d] %s\n", i + 1, options[i]);
        }
        System.out.println();
    }

    /**
     * Read string input
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Read password (masked input)
     */
    public static String readPassword(String prompt) {
        System.out.print(prompt);
        // In a real application, use Console.readPassword() for security
        // For this demo, we'll use regular input
        String password = scanner.nextLine().trim();
        return password;
    }

    /**
     * Read integer input with validation
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Confirm action
     */
    public static boolean confirm(String message) {
        System.out.print(message + " (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }

    /**
     * Pause until user presses enter
     */
    public static void pause() {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
    }

    /**
     * Get scanner instance
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Print loading animation
     */
    public static void printLoading(String message) {
        System.out.print(message);
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(300);
                System.out.print(".");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    /**
     * Print box around text
     */
    public static void printBox(String text) {
        int length = text.length() + 4;
        String border = "─".repeat(length);
        
        System.out.println("┌" + border + "┐");
        System.out.println("│  " + text + "  │");
        System.out.println("└" + border + "┘");
    }
}
