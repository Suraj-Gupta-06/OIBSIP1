package com.atm.model;

/**
 * Account Type Enumeration
 */
public enum AccountType {
    SAVINGS("Savings Account"),
    CURRENT("Current Account"),
    SALARY("Salary Account"),
    FIXED_DEPOSIT("Fixed Deposit Account");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
