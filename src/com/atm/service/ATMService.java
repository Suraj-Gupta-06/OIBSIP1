package com.atm.service;

import com.atm.exception.*;
import com.atm.model.*;
import com.atm.repository.AccountRepository;
import com.atm.util.ValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ATM Service - Business Logic Layer
 * Handles all ATM operations with comprehensive validation and error handling
 */
public class ATMService {
    private final AccountRepository accountRepository;
    private Account currentAccount;
    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("500.00");
    private static final BigDecimal MINIMUM_WITHDRAWAL = new BigDecimal("100.00");
    private static final BigDecimal MAXIMUM_WITHDRAWAL_PER_TRANSACTION = new BigDecimal("40000.00");

    public ATMService() {
        this.accountRepository = AccountRepository.getInstance();
    }

    /**
     * Authenticate user and set current session
     */
    public boolean login(String userId, String pin) throws ATMException {
        // Input validation
        if (!ValidationUtil.isValidUserId(userId)) {
            throw new InvalidAccountException("Invalid User ID format");
        }
        
        if (!ValidationUtil.isValidPin(pin)) {
            throw new ATMException("Invalid PIN format");
        }

        // Authenticate
        var accountOpt = accountRepository.authenticate(userId, pin);
        
        if (accountOpt.isEmpty()) {
            var account = accountRepository.findByUserId(userId);
            if (account.isPresent() && account.get().isLocked()) {
                throw new AccountLockedException(
                    "Account is locked due to multiple failed login attempts. Please contact bank.");
            }
            return false;
        }

        currentAccount = accountOpt.get();
        
        // Check account status
        if (currentAccount.getStatus() != AccountStatus.ACTIVE) {
            currentAccount = null;
            throw new ATMException("Account is " + currentAccount.getStatus().getDisplayName() + 
                                   ". Please contact bank.");
        }

        // Update last access time
        currentAccount.setLastAccessDate(LocalDateTime.now());
        
        // Reset daily withdrawal limit if new day
        checkAndResetDailyLimit();
        
        return true;
    }

    /**
     * Logout current user
     */
    public void logout() {
        currentAccount = null;
    }

    /**
     * Get current account
     */
    public Account getCurrentAccount() {
        return currentAccount;
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentAccount != null;
    }

    /**
     * Get transaction history
     */
    public List<Transaction> getTransactionHistory() throws ATMException {
        validateSession();
        return currentAccount.getTransactionHistory();
    }

    /**
     * Get transaction history with limit
     */
    public List<Transaction> getTransactionHistory(int limit) throws ATMException {
        validateSession();
        List<Transaction> allTransactions = currentAccount.getTransactionHistory();
        int size = allTransactions.size();
        if (size <= limit) {
            return allTransactions;
        }
        return allTransactions.subList(size - limit, size);
    }

    /**
     * Withdraw money from account
     */
    public Transaction withdraw(BigDecimal amount) throws ATMException {
        validateSession();
        
        // Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ATMException("Withdrawal amount must be positive");
        }
        
        if (amount.compareTo(MINIMUM_WITHDRAWAL) < 0) {
            throw new ATMException(String.format(
                "Minimum withdrawal amount is ₹%.2f", MINIMUM_WITHDRAWAL));
        }
        
        if (amount.compareTo(MAXIMUM_WITHDRAWAL_PER_TRANSACTION) > 0) {
            throw new ATMException(String.format(
                "Maximum withdrawal per transaction is ₹%.2f", MAXIMUM_WITHDRAWAL_PER_TRANSACTION));
        }
        
        // Check if amount is in valid denominations (100s)
        if (amount.remainder(new BigDecimal("100")).compareTo(BigDecimal.ZERO) != 0) {
            throw new ATMException("Amount must be in multiples of ₹100");
        }

        // Check daily limit
        checkAndResetDailyLimit();
        BigDecimal totalWithdrawalToday = currentAccount.getDailyWithdrawnAmount().add(amount);
        if (totalWithdrawalToday.compareTo(currentAccount.getDailyWithdrawalLimit()) > 0) {
            throw new DailyLimitExceededException(String.format(
                "Daily withdrawal limit exceeded. Limit: ₹%.2f, Already withdrawn: ₹%.2f",
                currentAccount.getDailyWithdrawalLimit(), 
                currentAccount.getDailyWithdrawnAmount()));
        }

        // Check balance
        BigDecimal newBalance = currentAccount.getBalance().subtract(amount);
        if (newBalance.compareTo(MINIMUM_BALANCE) < 0) {
            throw new InsufficientFundsException(String.format(
                "Insufficient funds. Available balance: ₹%.2f (Minimum balance: ₹%.2f required)",
                currentAccount.getBalance(), MINIMUM_BALANCE));
        }

        // Process withdrawal
        currentAccount.setBalance(newBalance);
        currentAccount.addToDailyWithdrawnAmount(amount);

        // Create transaction record
        Transaction transaction = new Transaction(
            currentAccount.getAccountNumber(),
            TransactionType.WITHDRAWAL,
            amount,
            newBalance,
            "ATM Withdrawal"
        );
        
        currentAccount.addTransaction(transaction);
        accountRepository.saveAccount(currentAccount);

        return transaction;
    }

    /**
     * Deposit money to account
     */
    public Transaction deposit(BigDecimal amount) throws ATMException {
        validateSession();
        
        // Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ATMException("Deposit amount must be positive");
        }
        
        // Maximum single deposit check (prevent money laundering)
        BigDecimal maxDeposit = new BigDecimal("200000.00");
        if (amount.compareTo(maxDeposit) > 0) {
            throw new ATMException(String.format(
                "Single deposit cannot exceed ₹%.2f. Please visit branch for larger deposits.", 
                maxDeposit));
        }

        // Process deposit
        BigDecimal newBalance = currentAccount.getBalance().add(amount);
        currentAccount.setBalance(newBalance);

        // Create transaction record
        Transaction transaction = new Transaction(
            currentAccount.getAccountNumber(),
            TransactionType.DEPOSIT,
            amount,
            newBalance,
            "ATM Deposit"
        );
        
        currentAccount.addTransaction(transaction);
        accountRepository.saveAccount(currentAccount);

        return transaction;
    }

    /**
     * Transfer money to another account
     */
    public Transaction transfer(String recipientAccountNumber, BigDecimal amount) 
            throws ATMException {
        validateSession();
        
        // Validate recipient account
        if (!ValidationUtil.isValidAccountNumber(recipientAccountNumber)) {
            throw new InvalidAccountException("Invalid recipient account number format");
        }
        
        if (recipientAccountNumber.equals(currentAccount.getAccountNumber())) {
            throw new ATMException("Cannot transfer to same account");
        }
        
        var recipientOpt = accountRepository.findByAccountNumber(recipientAccountNumber);
        if (recipientOpt.isEmpty()) {
            throw new InvalidAccountException("Recipient account not found: " + recipientAccountNumber);
        }
        
        Account recipientAccount = recipientOpt.get();
        
        // Check recipient account status
        if (recipientAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new ATMException("Recipient account is not active");
        }

        // Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ATMException("Transfer amount must be positive");
        }
        
        BigDecimal minTransfer = new BigDecimal("1.00");
        if (amount.compareTo(minTransfer) < 0) {
            throw new ATMException("Minimum transfer amount is ₹1.00");
        }
        
        // Maximum transfer limit per transaction
        BigDecimal maxTransfer = new BigDecimal("100000.00");
        if (amount.compareTo(maxTransfer) > 0) {
            throw new ATMException(String.format(
                "Maximum transfer per transaction is ₹%.2f", maxTransfer));
        }

        // Check sender balance
        BigDecimal newSenderBalance = currentAccount.getBalance().subtract(amount);
        if (newSenderBalance.compareTo(MINIMUM_BALANCE) < 0) {
            throw new InsufficientFundsException(String.format(
                "Insufficient funds. Available balance: ₹%.2f (Minimum balance: ₹%.2f required)",
                currentAccount.getBalance(), MINIMUM_BALANCE));
        }

        // Process transfer (atomic operation)
        try {
            // Debit from sender
            currentAccount.setBalance(newSenderBalance);
            Transaction senderTransaction = new Transaction(
                currentAccount.getAccountNumber(),
                TransactionType.TRANSFER_OUT,
                amount,
                newSenderBalance,
                "Transfer to " + recipientAccountNumber,
                recipientAccountNumber
            );
            currentAccount.addTransaction(senderTransaction);

            // Credit to recipient
            BigDecimal newRecipientBalance = recipientAccount.getBalance().add(amount);
            recipientAccount.setBalance(newRecipientBalance);
            Transaction recipientTransaction = new Transaction(
                recipientAccount.getAccountNumber(),
                TransactionType.TRANSFER_IN,
                amount,
                newRecipientBalance,
                "Transfer from " + currentAccount.getAccountNumber(),
                currentAccount.getAccountNumber()
            );
            recipientAccount.addTransaction(recipientTransaction);

            // Save both accounts
            accountRepository.saveAccount(currentAccount);
            accountRepository.saveAccount(recipientAccount);

            return senderTransaction;
            
        } catch (Exception e) {
            // Rollback in case of error
            throw new ATMException("Transfer failed: " + e.getMessage());
        }
    }

    /**
     * Check account balance
     */
    public BigDecimal checkBalance() throws ATMException {
        validateSession();
        return currentAccount.getBalance();
    }

    /**
     * Change PIN
     */
    public void changePin(String oldPin, String newPin, String confirmPin) throws ATMException {
        validateSession();
        
        // Validate old PIN
        if (!currentAccount.getPin().equals(oldPin)) {
            throw new ATMException("Current PIN is incorrect");
        }
        
        // Validate new PIN
        if (!ValidationUtil.isValidPin(newPin)) {
            throw new ATMException("New PIN must be 4 digits");
        }
        
        if (newPin.equals(oldPin)) {
            throw new ATMException("New PIN must be different from current PIN");
        }
        
        if (!newPin.equals(confirmPin)) {
            throw new ATMException("New PIN and confirmation PIN do not match");
        }
        
        // Check for weak PINs
        if (ValidationUtil.isWeakPin(newPin)) {
            throw new ATMException(
                "Weak PIN detected. Avoid sequential numbers (1234) or repeated digits (1111)");
        }

        // Update PIN
        currentAccount.setPin(newPin);
        
        // Create transaction record
        Transaction transaction = new Transaction(
            currentAccount.getAccountNumber(),
            TransactionType.PIN_CHANGE,
            BigDecimal.ZERO,
            currentAccount.getBalance(),
            "PIN Changed Successfully"
        );
        currentAccount.addTransaction(transaction);
        
        accountRepository.saveAccount(currentAccount);
    }

    /**
     * Get account summary
     */
    public String getAccountSummary() throws ATMException {
        validateSession();
        
        StringBuilder summary = new StringBuilder();
        summary.append("═══════════════════════════════════════════════════════════\n");
        summary.append("                    ACCOUNT SUMMARY                         \n");
        summary.append("═══════════════════════════════════════════════════════════\n");
        summary.append(String.format("Account Number    : %s\n", currentAccount.getAccountNumber()));
        summary.append(String.format("Account Holder    : %s\n", currentAccount.getAccountHolderName()));
        summary.append(String.format("Account Type      : %s\n", currentAccount.getAccountType().getDisplayName()));
        summary.append(String.format("Current Balance   : ₹%,.2f\n", currentAccount.getBalance()));
        summary.append(String.format("Available Balance : ₹%,.2f\n", 
            currentAccount.getBalance().subtract(MINIMUM_BALANCE)));
        summary.append(String.format("Daily Limit       : ₹%,.2f\n", currentAccount.getDailyWithdrawalLimit()));
        summary.append(String.format("Withdrawn Today   : ₹%,.2f\n", currentAccount.getDailyWithdrawnAmount()));
        summary.append(String.format("Remaining Limit   : ₹%,.2f\n", 
            currentAccount.getDailyWithdrawalLimit().subtract(currentAccount.getDailyWithdrawnAmount())));
        summary.append("═══════════════════════════════════════════════════════════");
        
        return summary.toString();
    }

    /**
     * Validate current session
     */
    private void validateSession() throws ATMException {
        if (currentAccount == null) {
            throw new ATMException("No active session. Please login first.");
        }
    }

    /**
     * Check and reset daily withdrawal limit if new day
     */
    private void checkAndResetDailyLimit() {
        LocalDateTime lastReset = currentAccount.getLastWithdrawalResetDate();
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        if (lastReset.toLocalDate().isBefore(today.toLocalDate())) {
            currentAccount.resetDailyWithdrawalAmount();
            accountRepository.saveAccount(currentAccount);
        }
    }

    /**
     * Get minimum balance requirement
     */
    public BigDecimal getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}
