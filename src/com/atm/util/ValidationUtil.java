package com.atm.util;

/**
 * Validation Utility Class
 * Provides validation methods for various inputs
 */
public class ValidationUtil {
    
    /**
     * Validate User ID format
     */
    public static boolean isValidUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        // User ID should be alphanumeric and 3-20 characters
        return userId.matches("^[a-zA-Z0-9]{3,20}$");
    }

    /**
     * Validate PIN format
     */
    public static boolean isValidPin(String pin) {
        if (pin == null) {
            return false;
        }
        // PIN should be exactly 4 digits
        return pin.matches("^\\d{4}$");
    }

    /**
     * Check if PIN is weak (sequential or repeated digits)
     */
    public static boolean isWeakPin(String pin) {
        if (pin == null || pin.length() != 4) {
            return true;
        }
        
        // Check for repeated digits (1111, 2222, etc.)
        if (pin.matches("(\\d)\\1{3}")) {
            return true;
        }
        
        // Check for sequential ascending (1234, 2345, etc.)
        boolean isSequential = true;
        for (int i = 0; i < 3; i++) {
            if (pin.charAt(i + 1) - pin.charAt(i) != 1) {
                isSequential = false;
                break;
            }
        }
        if (isSequential) {
            return true;
        }
        
        // Check for sequential descending (4321, 3210, etc.)
        isSequential = true;
        for (int i = 0; i < 3; i++) {
            if (pin.charAt(i) - pin.charAt(i + 1) != 1) {
                isSequential = false;
                break;
            }
        }
        
        return isSequential;
    }

    /**
     * Validate Account Number format
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        // Account number should be alphanumeric and 5-20 characters
        return accountNumber.matches("^[A-Z0-9]{5,20}$");
    }

    /**
     * Validate amount string
     */
    public static boolean isValidAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return false;
        }
        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sanitize input to prevent injection attacks
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("[^a-zA-Z0-9._@-]", "");
    }

    /**
     * Mask account number for display (show only last 4 digits)
     */
    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "****";
        }
        int length = accountNumber.length();
        return "*".repeat(length - 4) + accountNumber.substring(length - 4);
    }

    /**
     * Mask PIN for display
     */
    public static String maskPin(String pin) {
        if (pin == null) {
            return "****";
        }
        return "****";
    }
}
