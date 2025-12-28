package com.atm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Transaction Model - Represents a single banking transaction
 * Complete audit trail for all operations
 */
public class Transaction {
    private final String transactionId;
    private final String accountNumber;
    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final LocalDateTime timestamp;
    private final String description;
    private String recipientAccountNumber; // For transfers
    private TransactionStatus status;

    public Transaction(String accountNumber, TransactionType type, 
                      BigDecimal amount, BigDecimal balanceAfter, String description) {
        this.transactionId = generateTransactionId();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.status = TransactionStatus.COMPLETED;
    }

    public Transaction(String accountNumber, TransactionType type, 
                      BigDecimal amount, BigDecimal balanceAfter, 
                      String description, String recipientAccountNumber) {
        this(accountNumber, type, amount, balanceAfter, description);
        this.recipientAccountNumber = recipientAccountNumber;
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public String getRecipientAccountNumber() { return recipientAccountNumber; }
    public TransactionStatus getStatus() { return status; }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        String recipientInfo = recipientAccountNumber != null 
            ? " | To: " + recipientAccountNumber : "";
        return String.format("%-20s | %-12s | %-10s | ₹%,12.2f | Bal: ₹%,12.2f%s",
            getFormattedTimestamp(), transactionId, type, amount, balanceAfter, recipientInfo);
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append(String.format("Transaction ID    : %s\n", transactionId));
        sb.append(String.format("Type              : %s\n", type));
        sb.append(String.format("Amount            : ₹%,.2f\n", amount));
        sb.append(String.format("Balance After     : ₹%,.2f\n", balanceAfter));
        sb.append(String.format("Date & Time       : %s\n", getFormattedTimestamp()));
        sb.append(String.format("Status            : %s\n", status));
        if (recipientAccountNumber != null) {
            sb.append(String.format("Recipient Account : %s\n", recipientAccountNumber));
        }
        sb.append(String.format("Description       : %s\n", description));
        sb.append("═══════════════════════════════════════════════════════════");
        return sb.toString();
    }
}
