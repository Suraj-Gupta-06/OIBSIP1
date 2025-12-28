package com.atm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Account Model - Represents a bank account with complete banking details
 * Industry-level implementation with proper encapsulation and validation
 */
public class Account {
    private final String accountNumber;
    private final String userId;
    private String pin;
    private String accountHolderName;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;
    private final LocalDateTime createdDate;
    private LocalDateTime lastAccessDate;
    private int failedLoginAttempts;
    private boolean isLocked;
    private BigDecimal dailyWithdrawalLimit;
    private BigDecimal dailyWithdrawnAmount;
    private LocalDateTime lastWithdrawalResetDate;
    private List<Transaction> transactionHistory;

    public Account(String accountNumber, String userId, String pin, 
                   String accountHolderName, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.pin = pin;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.accountType = AccountType.SAVINGS;
        this.status = AccountStatus.ACTIVE;
        this.createdDate = LocalDateTime.now();
        this.lastAccessDate = LocalDateTime.now();
        this.failedLoginAttempts = 0;
        this.isLocked = false;
        this.dailyWithdrawalLimit = new BigDecimal("50000");
        this.dailyWithdrawnAmount = BigDecimal.ZERO;
        this.lastWithdrawalResetDate = LocalDateTime.now().toLocalDate().atStartOfDay();
        this.transactionHistory = new ArrayList<>();
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getUserId() { return userId; }
    public String getPin() { return pin; }
    public String getAccountHolderName() { return accountHolderName; }
    public BigDecimal getBalance() { return balance; }
    public AccountType getAccountType() { return accountType; }
    public AccountStatus getStatus() { return status; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getLastAccessDate() { return lastAccessDate; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public boolean isLocked() { return isLocked; }
    public BigDecimal getDailyWithdrawalLimit() { return dailyWithdrawalLimit; }
    public BigDecimal getDailyWithdrawnAmount() { return dailyWithdrawnAmount; }
    public LocalDateTime getLastWithdrawalResetDate() { return lastWithdrawalResetDate; }
    public List<Transaction> getTransactionHistory() { 
        return new ArrayList<>(transactionHistory); 
    }

    // Setters with validation
    public void setPin(String pin) {
        if (pin == null || pin.length() != 4 || !pin.matches("\\d+")) {
            throw new IllegalArgumentException("PIN must be 4 digits");
        }
        this.pin = pin;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setLastAccessDate(LocalDateTime lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 3) {
            this.isLocked = true;
        }
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.isLocked = false;
    }

    public void setDailyWithdrawalLimit(BigDecimal dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public void addToDailyWithdrawnAmount(BigDecimal amount) {
        this.dailyWithdrawnAmount = this.dailyWithdrawnAmount.add(amount);
    }

    public void resetDailyWithdrawalAmount() {
        this.dailyWithdrawnAmount = BigDecimal.ZERO;
        this.lastWithdrawalResetDate = LocalDateTime.now().toLocalDate().atStartOfDay();
    }

    public void addTransaction(Transaction transaction) {
        this.transactionHistory.add(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Account[%s] %s - Balance: ₹%.2f", 
            accountNumber, accountHolderName, balance);
    }
}
