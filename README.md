# 🏦 Secure Bank ATM System

## Project Overview
A professional, industry-level ATM (Automated Teller Machine) banking system built with Java. This console-based application demonstrates enterprise-grade software development practices including proper architecture, security measures, error handling, and user experience design.

## 🎯 Key Features

### Core Banking Operations
1. **💳 Transaction History**
   - View complete transaction history
   - Detailed transaction information
   - Transaction ID tracking
   - Timestamp recording

2. **💸 Withdraw Cash**
   - Quick withdrawal options (₹500, ₹1000, ₹2000, ₹5000, ₹10000)
   - Custom amount withdrawal
   - Daily withdrawal limits
   - Minimum balance enforcement
   - Denomination validation (multiples of ₹100)

3. **💵 Deposit Cash**
   - Cash deposit functionality
   - Maximum single deposit limits
   - Instant balance update
   - Transaction receipt generation

4. **🔄 Transfer Money**
   - Account-to-account transfers
   - Recipient account validation
   - Transfer limits and security
   - Atomic transactions (all-or-nothing)

5. **📊 Account Summary**
   - Complete account information
   - Available balance calculation
   - Daily limit tracking
   - Account status display

6. **🔐 Change PIN**
   - Secure PIN modification
   - PIN strength validation
   - Confirmation required
   - Auto-logout after change

## 🏗️ Architecture & Design

### Design Patterns Used
- **Singleton Pattern**: AccountRepository (single data source)
- **Model-View-Controller (MVC)**: Separation of concerns
- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic isolation
- **Exception Handling Pattern**: Custom exception hierarchy

### Project Structure
```
atm-banking-system/
├── src/
│   └── com/
│       └── atm/
│           ├── ATMApplication.java         # Main application entry point
│           ├── model/                      # Domain models
│           │   ├── Account.java
│           │   ├── Transaction.java
│           │   ├── AccountType.java
│           │   ├── AccountStatus.java
│           │   ├── TransactionType.java
│           │   └── TransactionStatus.java
│           ├── service/                    # Business logic
│           │   └── ATMService.java
│           ├── repository/                 # Data access
│           │   └── AccountRepository.java
│           ├── exception/                  # Custom exceptions
│           │   ├── ATMException.java
│           │   ├── InsufficientFundsException.java
│           │   ├── InvalidAccountException.java
│           │   ├── AccountLockedException.java
│           │   └── DailyLimitExceededException.java
│           └── util/                       # Utility classes
│               ├── ValidationUtil.java
│               └── ConsoleUtil.java
└── README.md
```

## 🔒 Security Features

1. **Authentication & Authorization**
   - User ID and PIN validation
   - Failed login attempt tracking
   - Account locking after 3 failed attempts
   - Session management

2. **Transaction Security**
   - PIN strength validation (prevents weak PINs like 1234, 1111)
   - Transaction confirmation required
   - Minimum balance enforcement
   - Daily withdrawal limits

3. **Input Validation**
   - Comprehensive input sanitization
   - Format validation for all inputs
   - SQL injection prevention
   - Cross-site scripting (XSS) prevention

4. **Data Protection**
   - Account number masking
   - PIN masking in display
   - Secure transaction logging

## 💡 Edge Cases Handled

1. **Account Management**
   - Locked accounts
   - Inactive/suspended accounts
   - Insufficient funds
   - Minimum balance violations

2. **Transaction Validation**
   - Invalid amount formats
   - Zero or negative amounts
   - Exceeding daily limits
   - Transfer to same account
   - Non-existent recipient accounts
   - Invalid denominations

3. **Session Management**
   - Session expiry
   - Concurrent access prevention
   - Logout security

4. **Error Handling**
   - Network timeouts (simulated)
   - Data corruption handling
   - Graceful degradation

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or terminal

### Compilation
```bash
# Navigate to the project directory
cd atm-banking-system/src

# Compile all Java files
javac com/atm/*.java com/atm/*/*.java

# Or compile from root
javac -d bin src/com/atm/*.java src/com/atm/*/*.java
```

### Running the Application
```bash
# From src directory
java com.atm.ATMApplication

# Or from root with classpath
java -cp bin com.atm.ATMApplication
```

## 👥 Sample Test Accounts

| User ID | PIN  | Account Number | Balance      | Account Holder |
|---------|------|----------------|--------------|----------------|
| user1   | 1234 | ACC1001        | ₹50,000.00   | Suraj Gupta    |
| user2   | 5678 | ACC1002        | ₹75,000.00   | Virat Kohli    |
| user3   | 9012 | ACC1003        | ₹25,000.50   | Amit Patel     |
| user4   | 3456 | ACC1004        | ₹1,00,000.00 | Samrudhi Pitale|
| admin   | 0000 | ACC1005        | ₹10,00,000.00| System Admin   |

## 📊 Technical Specifications

### Business Rules
- **Minimum Balance**: ₹500
- **Minimum Withdrawal**: ₹100
- **Maximum Withdrawal per Transaction**: ₹40,000
- **Daily Withdrawal Limit**: ₹50,000
- **Maximum Deposit per Transaction**: ₹2,00,000
- **Maximum Transfer per Transaction**: ₹1,00,000
- **Withdrawal Denominations**: Multiples of ₹100
- **Failed Login Attempts**: 3 (then account locks)

### Technology Stack
- **Language**: Java 11+
- **Architecture**: Layered Architecture (MVC)
- **Design Patterns**: Singleton, Repository, Service Layer
- **Data Storage**: In-memory (HashMap) - can be easily migrated to database
- **Exception Handling**: Custom exception hierarchy

## 🔄 Future Enhancements

1. **Database Integration**
   - MySQL/PostgreSQL integration
   - JDBC/JPA implementation
   - Connection pooling

2. **Advanced Features**
   - Multi-currency support
   - Bill payment functionality
   - Mobile recharge
   - Mini statement printing
   - Account statements (PDF/Email)

3. **Security Enhancements**
   - Encryption for sensitive data
   - Two-factor authentication (2FA)
   - Biometric authentication simulation
   - Session timeout

4. **Reporting & Analytics**
   - Transaction analytics
   - Spending patterns
   - Export to CSV/Excel
   - Email notifications

5. **UI Improvements**
   - GUI implementation (Swing/JavaFX)
   - Web interface (Spring Boot)
   - Mobile app integration

## 🧪 Testing Scenarios

### Functional Testing
- Login with valid/invalid credentials
- Withdrawal with sufficient/insufficient funds
- Transfer to valid/invalid accounts
- PIN change with weak/strong PINs
- Daily limit exceeded scenarios

### Edge Case Testing
- Multiple failed login attempts
- Concurrent transaction simulation
- Boundary value testing (minimum/maximum amounts)
- Invalid input handling
- Account status validations

## 📝 Code Quality Features

1. **Clean Code Principles**
   - Meaningful variable and method names
   - Single Responsibility Principle
   - DRY (Don't Repeat Yourself)
   - SOLID principles

2. **Error Handling**
   - Try-catch blocks
   - Custom exceptions
   - User-friendly error messages
   - Logging (can be enhanced with Log4j)

3. **Documentation**
   - Comprehensive Javadoc comments
   - README documentation
   - Inline code comments
   - Architecture documentation

4. **Maintainability**
   - Modular design
   - Loosely coupled components
   - Easy to extend and modify
   - Configuration management

## 📈 Performance Considerations

- **Time Complexity**: O(1) for most operations (HashMap lookup)
- **Space Complexity**: O(n) where n is number of accounts
- **Scalability**: Can be easily migrated to database
- **Concurrency**: Ready for multi-threading implementation

## 🤝 Contributing

This is a portfolio/resume project, but suggestions are welcome:
1. Fork the repository
2. Create a feature branch
3. Implement your changes
4. Submit a pull request

## 📄 License

This project is created for educational and portfolio purposes.

## 👨‍💻 Author

**Suraj Gupta**
- LinkedIn: [https://www.linkedin.com/in/suraj-gupta-]
- GitHub: [https://github.com/Suraj-Gupta-06]
- Email: [suraj(955955@gamil.com)]

## 🙏 Acknowledgments

- Java Documentation
- Design Pattern best practices
- Banking domain knowledge
- Enterprise software development standards

---

**Note**: This is a demonstration project. In production environments, additional security measures, database integration, and compliance with banking regulations would be necessary.
