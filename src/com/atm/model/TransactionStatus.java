package com.atm.model;

/**
 * Transaction Status Enumeration
 */
public enum TransactionStatus {
    COMPLETED("Completed"),
    PENDING("Pending"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REVERSED("Reversed");

    private final String displayName;

    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
