package com.atm;

import com.atm.exception.*;
import com.atm.model.Transaction;
import com.atm.service.ATMService;
import com.atm.util.ConsoleUtil;
import com.atm.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * ATM Application - Main Entry Point
 * Professional console-based ATM system with comprehensive features
 * 
 * @author Your Name
 * @version 2.0
 */
public class ATMApplication {
    private final ATMService atmService;
    private boolean isRunning;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public ATMApplication() {
        this.atmService = new ATMService();
        this.isRunning = true;
    }

    /**
     * Main method - Application entry point
     */
    public static void main(String[] args) {
        ATMApplication app = new ATMApplication();
        app.run();
    }

    /**
     * Run the ATM application
     */
    public void run() {
        displayWelcomeScreen();
        
        while (isRunning) {
            if (!atmService.isLoggedIn()) {
                if (!handleLogin()) {
                    continue;
                }
            }
            
            displayMainMenu();
        }
        
        displayGoodbyeScreen();
    }

    /**
     * Display welcome screen
     */
    private void displayWelcomeScreen() {
        ConsoleUtil.clearScreen();
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║              WELCOME TO SECURE BANK ATM                   ║");
        System.out.println("║                                                           ║");
        System.out.println("║            Your Trust, Our Commitment                     ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println("\n           Available 24/7 for Your Banking Needs");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        displaySampleCredentials();
        ConsoleUtil.pause();
    }

    /**
     * Display sample credentials for testing
     */
    private void displaySampleCredentials() {
        System.out.println("\n📋 SAMPLE ACCOUNTS FOR TESTING:");
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  User ID: user1  | PIN: 1234 | Balance: ₹50,000");
        System.out.println("  User ID: user2  | PIN: 5678 | Balance: ₹75,000");
        System.out.println("  User ID: user3  | PIN: 9012 | Balance: ₹25,000");
        System.out.println("  User ID: admin  | PIN: 0000 | Balance: ₹10,00,000");
        System.out.println("─────────────────────────────────────────────────────────");
    }

    /**
     * Handle user login
     */
    private boolean handleLogin() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("USER AUTHENTICATION");
        
        int attempts = 0;
        
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.println();
            String userId = ConsoleUtil.readString("Enter User ID: ");
            
            if (userId.equalsIgnoreCase("exit") || userId.equalsIgnoreCase("quit")) {
                isRunning = false;
                return false;
            }
            
            String pin = ConsoleUtil.readPassword("Enter PIN: ");
            
            try {
                ConsoleUtil.printLoading("Authenticating");
                
                if (atmService.login(userId, pin)) {
                    ConsoleUtil.printSuccess("Login successful!");
                    System.out.println("\nWelcome, " + atmService.getCurrentAccount().getAccountHolderName() + "!");
                    Thread.sleep(1500);
                    return true;
                } else {
                    attempts++;
                    int remaining = MAX_LOGIN_ATTEMPTS - attempts;
                    if (remaining > 0) {
                        ConsoleUtil.printError("Invalid credentials. " + remaining + " attempt(s) remaining.");
                        Thread.sleep(1000);
                    }
                }
            } catch (AccountLockedException e) {
                ConsoleUtil.printError(e.getMessage());
                ConsoleUtil.pause();
                return false;
            } catch (ATMException e) {
                ConsoleUtil.printError(e.getMessage());
                ConsoleUtil.pause();
                return false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        ConsoleUtil.printError("Maximum login attempts exceeded. Please try again later.");
        ConsoleUtil.pause();
        return false;
    }

    /**
     * Display main menu and handle user choice
     */
    private void displayMainMenu() {
        ConsoleUtil.clearScreen();
        
        try {
            ConsoleUtil.printHeader("MAIN MENU");
            
            // Display account summary
            System.out.println("\n👤 Account: " + atmService.getCurrentAccount().getAccountHolderName());
            System.out.println("💰 Balance: ₹" + String.format("%,.2f", atmService.checkBalance()));
            
            ConsoleUtil.printDivider();
            
            String[] menuOptions = {
                "💳 Transaction History",
                "💸 Withdraw Cash",
                "💵 Deposit Cash",
                "🔄 Transfer Money",
                "📊 Account Summary",
                "🔐 Change PIN",
                "🚪 Logout"
            };
            
            ConsoleUtil.printMenu(menuOptions);
            
            int choice = ConsoleUtil.readInt("Select an option (1-7): ", 1, 7);
            
            switch (choice) {
                case 1 -> handleTransactionHistory();
                case 2 -> handleWithdrawal();
                case 3 -> handleDeposit();
                case 4 -> handleTransfer();
                case 5 -> handleAccountSummary();
                case 6 -> handlePinChange();
                case 7 -> handleLogout();
            }
            
        } catch (ATMException e) {
            ConsoleUtil.printError(e.getMessage());
            ConsoleUtil.pause();
        }
    }

    /**
     * Handle transaction history display
     */
    private void handleTransactionHistory() {
        try {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("TRANSACTION HISTORY");
            
            List<Transaction> transactions = atmService.getTransactionHistory();
            
            if (transactions.isEmpty()) {
                ConsoleUtil.printInfo("No transactions found.");
            } else {
                System.out.println("\nShowing last " + Math.min(10, transactions.size()) + " transactions:");
                ConsoleUtil.printDivider();
                
                // Get last 10 transactions
                int start = Math.max(0, transactions.size() - 10);
                List<Transaction> recentTransactions = transactions.subList(start, transactions.size());
                
                // Reverse to show newest first
                for (int i = recentTransactions.size() - 1; i >= 0; i--) {
                    System.out.println(recentTransactions.get(i));
                }
                
                ConsoleUtil.printDivider();
                System.out.println("\nTotal Transactions: " + transactions.size());
            }
            
            ConsoleUtil.pause();
            
        } catch (ATMException e) {
            ConsoleUtil.printError(e.getMessage());
            ConsoleUtil.pause();
        }
    }

    /**
     * Handle cash withdrawal
     */
    private void handleWithdrawal() {
        try {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("CASH WITHDRAWAL");
            
            // Show available balance
            BigDecimal balance = atmService.checkBalance();
            BigDecimal minBalance = atmService.getMinimumBalance();
            BigDecimal available = balance.subtract(minBalance);
            
            System.out.println("\n💰 Available Balance: ₹" + String.format("%,.2f", available));
            System.out.println("ℹ️  Minimum Balance Required: ₹" + String.format("%,.2f", minBalance));
            
            // Quick withdrawal options
            System.out.println("\n🔢 Quick Withdrawal Options:");
            System.out.println("  [1] ₹500      [2] ₹1,000    [3] ₹2,000");
            System.out.println("  [4] ₹5,000    [5] ₹10,000   [6] Custom Amount");
            System.out.println("  [0] Cancel");
            
            int choice = ConsoleUtil.readInt("\nSelect option: ", 0, 6);
            
            if (choice == 0) {
                return;
            }
            
            BigDecimal amount;
            
            if (choice == 6) {
                String amountStr = ConsoleUtil.readString("\nEnter amount (multiples of ₹100): ₹");
                if (!ValidationUtil.isValidAmount(amountStr)) {
                    ConsoleUtil.printError("Invalid amount entered.");
                    ConsoleUtil.pause();
                    return;
                }
                amount = new BigDecimal(amountStr);
            } else {
                int[] amounts = {0, 500, 1000, 2000, 5000, 10000};
                amount = new BigDecimal(amounts[choice]);
            }
            
            // Confirm transaction
            System.out.println("\n📋 Transaction Summary:");
            System.out.println("  Withdrawal Amount: ₹" + String.format("%,.2f", amount));
            System.out.println("  Current Balance: ₹" + String.format("%,.2f", balance));
            System.out.println("  Balance After: ₹" + String.format("%,.2f", balance.subtract(amount)));
            
            if (!ConsoleUtil.confirm("\nConfirm withdrawal?")) {
                ConsoleUtil.printInfo("Transaction cancelled.");
                ConsoleUtil.pause();
                return;
            }
            
            ConsoleUtil.printLoading("\nProcessing withdrawal");
            
            Transaction transaction = atmService.withdraw(amount);
            
            ConsoleUtil.printSuccess("Withdrawal successful!");
            System.out.println("\n" + transaction.toDetailedString());
            
            ConsoleUtil.pause();
            
        } catch (ATMException e) {
            ConsoleUtil.printError(e.getMessage());
            ConsoleUtil.pause();
        }
    }

    /**
     * Handle cash deposit
     */
    private void handleDeposit() {
        try {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("CASH DEPOSIT");
            
            System.out.println("\n💰 Current Balance: ₹" + String.format("%,.2f", atmService.checkBalance()));
            
            String amountStr = ConsoleUtil.readString("\nEnter deposit amount: ₹");
            
            if (!ValidationUtil.isValidAmount(amountStr)) {
                ConsoleUtil.printError("Invalid amount entered.");
                ConsoleUtil.pause();
                return;
            }
            
            BigDecimal amount = new BigDecimal(amountStr);
            
            // Confirm transaction
            System.out.println("\n📋 Transaction Summary:");
            System.out.println("  Deposit Amount: ₹" + String.format("%,.2f", amount));
            System.out.println("  Balance After: ₹" + String.format("%,.2f", 
                atmService.checkBalance().add(amount)));
            
            if (!ConsoleUtil.confirm("\nConfirm deposit?")) {
                ConsoleUtil.printInfo("Transaction cancelled.");
                ConsoleUtil.pause();
                return;
            }
            
            ConsoleUtil.printLoading("\nProcessing deposit");
            
            Transaction transaction = atmService.deposit(amount);
            
            ConsoleUtil.printSuccess("Deposit successful!");
            System.out.println("\n" + transaction.toDetailedString());
            
            ConsoleUtil.pause();
            
        } catch (ATMException e) {
            ConsoleUtil.printError(e.getMessage());
            ConsoleUtil.pause();
        }
    }

    /**
     * Handle money transfer
     */
    private void handleTransfer() {
        try {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("TRANSFER MONEY");
            
            BigDecimal balance = atmService.checkBalance();
            BigDecimal minBalance = atmService.getMinimumBalance();
            BigDecimal available = balance.subtract(minBalance);
            
            System.out.println("\n💰 Available Balance: ₹" + String.format("%,.2f", available));
            
            String recipientAccount = ConsoleUtil.readString("\nEnter recipient account number: ");
            
            if (!ValidationUtil.isValidAccountNumber(recipientAccount)) {
                ConsoleUtil.printError("Invalid account number format.");
                ConsoleUtil.pause();
                return;
            }
            
            String amountStr = ConsoleUtil.readString("Enter transfer amount: ₹");
            
            if (!ValidationUtil.isValidAmount(amountStr)) {
                ConsoleUtil.printError("Invalid amount entered.");
                ConsoleUtil.pause();
                return;
            }
            
            BigDecimal amount = new BigDecimal(amountStr);
            
            // Confirm transaction
            System.out.println("\n📋 Transaction Summary:");
            System.out.println("  Recipient Account: " + recipientAccount);
            System.out.println("  Transfer Amount: ₹" + String.format("%,.2f", amount));
            System.out.println("  Current Balance: ₹" + String.format("%,.2f", balance));
            System.out.println("  Balance After: ₹" + String.format("%,.2f", balance.subtract(amount)));
            
            if (!ConsoleUtil.confirm("\nConfirm transfer?")) {
                ConsoleUtil.printInfo("Transaction cancelled.");
                ConsoleUtil.pause();
                return;
            }
            
            ConsoleUtil.printLoading("\nProcessing transfer");
            
            Transaction transaction = atmService.transfer(recipientAccount, amount);
            
            ConsoleUtil.printSuccess("Transfer successful!");
            System.out.println("\n" + transaction.toDetailedString());
            
            ConsoleUtil.pause();
            
        } catch (ATMException e) {
            ConsoleUtil.printError(e.getMessage());
            ConsoleUtil.pause();
        }
    }

    /**
     * Handle account summary display
     */
    private void handleAccountSummary() {
        try {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("ACCOUNT SUMMARY");
            
            System.out.println("\n" + atmService.getAccountSummary());
            
            ConsoleUtil.pause();
            
        } catch (ATMException e) {
            ConsoleUtil.printError(e.getMessage());
            ConsoleUtil.pause();
        }
    }

    /**
     * Handle PIN change
     */
    private void handlePinChange() {
        try {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("CHANGE PIN");
            
            System.out.println("\n🔐 PIN Requirements:");
            System.out.println("  • Must be 4 digits");
            System.out.println("  • Avoid sequential numbers (1234)");
            System.out.println("  • Avoid repeated digits (1111)");
            
            String oldPin = ConsoleUtil.readPassword("\nEnter current PIN: ");
            String newPin = ConsoleUtil.readPassword("Enter new PIN: ");
            String confirmPin = ConsoleUtil.readPassword("Confirm new PIN: ");
            
            ConsoleUtil.printLoading("\nUpdating PIN");
            
            atmService.changePin(oldPin, newPin, confirmPin);
            
            ConsoleUtil.printSuccess("PIN changed successfully!");
            ConsoleUtil.printInfo("Please remember your new PIN.");
            
            Thread.sleep(2000);
            
            // Auto logout after PIN change for security
            ConsoleUtil.printInfo("For security, you will be logged out.");
            Thread.sleep(1500);
            atmService.logout();
            
        } catch (ATMException e) {
            ConsoleUtil.printError(e.getMessage());
            ConsoleUtil.pause();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Handle logout
     */
    private void handleLogout() {
        if (ConsoleUtil.confirm("\nAre you sure you want to logout?")) {
            ConsoleUtil.printLoading("Logging out");
            atmService.logout();
            ConsoleUtil.printSuccess("Logged out successfully!");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Display goodbye screen
     */
    private void displayGoodbyeScreen() {
        ConsoleUtil.clearScreen();
        System.out.println("\n\n");
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║              THANK YOU FOR USING SECURE BANK              ║");
        System.out.println("║                                                           ║");
        System.out.println("║                Have a Great Day!                          ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println("\n           Please take your card and receipt\n");
    }
}
