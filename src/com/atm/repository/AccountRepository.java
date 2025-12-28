package com.atm.repository;

import com.atm.model.Account;
import com.atm.exception.InvalidAccountException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Account Repository - Data Access Layer
 * In production, this would connect to a database
 * Currently using in-memory HashMap for demonstration
 */
public class AccountRepository {
    private final Map<String, Account> accountsByUserId;
    private final Map<String, Account> accountsByAccountNumber;
    private static AccountRepository instance;

    private AccountRepository() {
        this.accountsByUserId = new HashMap<>();
        this.accountsByAccountNumber = new HashMap<>();
        initializeSampleAccounts();
    }

    /**
     * Singleton pattern for repository
     */
    public static synchronized AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    /**
     * Initialize sample accounts for testing
     */
    private void initializeSampleAccounts() {
        // Sample accounts with different scenarios
        Account account1 = new Account(
            "ACC1001", "user1", "1234", 
            "Suraj Gupta", new BigDecimal("50000.00")
        );
        
        Account account2 = new Account(
            "ACC1002", "user2", "5678", 
            "Virat Kohli", new BigDecimal("75000.00")
        );
        
        Account account3 = new Account(
            "ACC1003", "user3", "9012", 
            "Amit Patel", new BigDecimal("25000.50")
        );
        
        Account account4 = new Account(
            "ACC1004", "user4", "3456", 
            "Samrudhi Pitale", new BigDecimal("100000.00")
        );
        
        Account account5 = new Account(
            "ACC1005", "admin", "0000", 
            "System Admin", new BigDecimal("1000000.00")
        );

        saveAccount(account1);
        saveAccount(account2);
        saveAccount(account3);
        saveAccount(account4);
        saveAccount(account5);
    }

    /**
     * Save or update an account
     */
    public void saveAccount(Account account) {
        accountsByUserId.put(account.getUserId(), account);
        accountsByAccountNumber.put(account.getAccountNumber(), account);
    }

    /**
     * Find account by user ID
     */
    public Optional<Account> findByUserId(String userId) {
        return Optional.ofNullable(accountsByUserId.get(userId));
    }

    /**
     * Find account by account number
     */
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accountsByAccountNumber.get(accountNumber));
    }

    /**
     * Authenticate user with userId and PIN
     */
    public Optional<Account> authenticate(String userId, String pin) {
        Optional<Account> accountOpt = findByUserId(userId);
        
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            
            // Check if account is locked
            if (account.isLocked()) {
                return Optional.empty();
            }
            
            // Verify PIN
            if (account.getPin().equals(pin)) {
                account.resetFailedLoginAttempts();
                return Optional.of(account);
            } else {
                account.incrementFailedLoginAttempts();
                return Optional.empty();
            }
        }
        
        return Optional.empty();
    }

    /**
     * Check if account exists
     */
    public boolean accountExists(String accountNumber) {
        return accountsByAccountNumber.containsKey(accountNumber);
    }

    /**
     * Update account balance
     */
    public void updateBalance(String accountNumber, BigDecimal newBalance) 
            throws InvalidAccountException {
        Account account = accountsByAccountNumber.get(accountNumber);
        if (account == null) {
            throw new InvalidAccountException("Account not found: " + accountNumber);
        }
        account.setBalance(newBalance);
    }

    /**
     * Get all accounts (for admin purposes)
     */
    public Map<String, Account> getAllAccounts() {
        return new HashMap<>(accountsByUserId);
    }

    /**
     * Reset repository (for testing)
     */
    public void reset() {
        accountsByUserId.clear();
        accountsByAccountNumber.clear();
        initializeSampleAccounts();
    }
}
