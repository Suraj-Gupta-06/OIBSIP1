# 🚀 Installation & Setup Guide

## Prerequisites

Before running this ATM Banking System, ensure you have:

1. **Java Development Kit (JDK)**
   - Version: JDK 11 or higher
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Or use OpenJDK: https://openjdk.org/

2. **Terminal/Command Prompt**
   - Windows: Command Prompt or PowerShell
   - Mac/Linux: Terminal

3. **Text Editor or IDE** (Optional but recommended)
   - IntelliJ IDEA (Community Edition is free)
   - Eclipse
   - VS Code with Java extensions
   - NetBeans

---

## ✅ Verify Java Installation

Open terminal/command prompt and run:

```bash
java -version
javac -version
```

You should see something like:
```
java version "21.0.x" or "17.0.x" or "11.0.x"
javac 21.0.x or 17.0.x or 11.0.x
```

If not installed, follow the installation guide for your operating system below.

---

## 📥 Java Installation

### Windows

1. Download JDK from Oracle or Adoptium
2. Run the installer
3. Add Java to PATH:
   - Right-click "This PC" → Properties
   - Advanced System Settings → Environment Variables
   - Add `JAVA_HOME` pointing to JDK installation directory
   - Add `%JAVA_HOME%\bin` to PATH

### macOS

Using Homebrew:
```bash
brew install openjdk@21
```

Or download from Oracle website.

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install default-jdk
```

---

## 🏃 Running the Application

### Option 1: Using Scripts (Linux/Mac)

```bash
cd atm-banking-system

# Compile
./compile.sh

# Run
./run.sh
```

### Option 2: Using Scripts (Windows)

Create `compile.bat`:
```batch
@echo off
echo Compiling ATM Banking System...
if not exist bin mkdir bin
javac -d bin src\com\atm\*.java src\com\atm\*\*.java
echo Compilation complete!
pause
```

Create `run.bat`:
```batch
@echo off
echo Starting ATM Banking System...
java -cp bin com.atm.ATMApplication
pause
```

Then run:
```cmd
compile.bat
run.bat
```

### Option 3: Manual Compilation (All Systems)

```bash
# Navigate to project directory
cd atm-banking-system

# Create bin directory
mkdir bin

# Compile (Linux/Mac)
javac -d bin src/com/atm/*.java src/com/atm/*/*.java

# Compile (Windows)
javac -d bin src\com\atm\*.java src\com\atm\*\*.java

# Run (All systems)
java -cp bin com.atm.ATMApplication
```

### Option 4: Using IDE

#### IntelliJ IDEA
1. Open IntelliJ IDEA
2. File → Open → Select `atm-banking-system` folder
3. Right-click on `ATMApplication.java`
4. Select "Run 'ATMApplication.main()'"

#### Eclipse
1. Open Eclipse
2. File → Import → Existing Projects into Workspace
3. Select `atm-banking-system` folder
4. Right-click on `ATMApplication.java`
5. Run As → Java Application

#### VS Code
1. Open VS Code
2. Install "Extension Pack for Java"
3. Open `atm-banking-system` folder
4. Open `ATMApplication.java`
5. Click "Run" button above main method

---

## 📁 Project Structure Verification

After extraction, your directory should look like:

```
atm-banking-system/
├── src/
│   └── com/
│       └── atm/
│           ├── ATMApplication.java
│           ├── model/
│           │   ├── Account.java
│           │   ├── Transaction.java
│           │   └── ... (5 more files)
│           ├── service/
│           │   └── ATMService.java
│           ├── repository/
│           │   └── AccountRepository.java
│           ├── exception/
│           │   └── ... (5 files)
│           └── util/
│               └── ... (2 files)
├── bin/ (created after compilation)
├── README.md
├── FEATURES.md
├── INSTALLATION.md
├── compile.sh
└── run.sh
```

---

## 🧪 Testing the Application

### Sample Credentials

Use these test accounts:

| User ID | PIN  | Balance       |
|---------|------|---------------|
| user1   | 1234 | ₹50,000.00    |
| user2   | 5678 | ₹75,000.00    |
| user3   | 9012 | ₹25,000.50    |
| admin   | 0000 | ₹10,00,000.00 |

### Quick Test Scenarios

1. **Login Test**
   - User ID: `user1`
   - PIN: `1234`
   - Should login successfully

2. **Check Balance**
   - Select option 5 (Account Summary)
   - View balance and account details

3. **Withdrawal Test**
   - Select option 2 (Withdraw)
   - Try withdrawing ₹1000
   - Verify balance update

4. **Transfer Test**
   - Select option 4 (Transfer)
   - Recipient: `ACC1002`
   - Amount: ₹500
   - Verify transaction

5. **Transaction History**
   - Select option 1
   - View all transactions

---

## 🐛 Troubleshooting

### Issue: "javac: command not found"
**Solution**: Java is not installed or not in PATH
- Install JDK (see above)
- Add Java to system PATH

### Issue: "Error: Could not find or load main class"
**Solution**: Incorrect classpath
- Make sure you're in the correct directory
- Run: `java -cp bin com.atm.ATMApplication`

### Issue: "UnsupportedClassVersionError"
**Solution**: Compiled with newer Java version
- Recompile with your current Java version
- Or upgrade your Java runtime

### Issue: Console not clearing properly
**Solution**: Terminal doesn't support ANSI codes
- This is cosmetic only
- Application will still work fine

### Issue: "Package does not exist" during compilation
**Solution**: Wrong directory or incomplete files
- Make sure all files are present
- Compile from project root directory

---

## 🎨 Customization

### Changing Sample Accounts

Edit `AccountRepository.java` → `initializeSampleAccounts()` method:

```java
Account newAccount = new Account(
    "ACC1006",           // Account Number
    "myuser",            // User ID
    "5555",              // PIN
    "My Name",           // Account Holder Name
    new BigDecimal("100000.00")  // Initial Balance
);
saveAccount(newAccount);
```

### Changing Limits

Edit `ATMService.java` constants:

```java
private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("500.00");
private static final BigDecimal MINIMUM_WITHDRAWAL = new BigDecimal("100.00");
private static final BigDecimal MAXIMUM_WITHDRAWAL_PER_TRANSACTION = new BigDecimal("40000.00");
```

### Changing Daily Limit

Edit `Account.java` constructor:

```java
this.dailyWithdrawalLimit = new BigDecimal("50000");  // Change this value
```

---

## 📊 Performance Notes

- **Startup Time**: < 1 second
- **Memory Usage**: ~50-100 MB
- **Transaction Speed**: Instant (in-memory)
- **Concurrent Users**: Single user (console-based)

---

## 🔄 Updating the Application

To update after code changes:

1. Save your modifications
2. Recompile:
   ```bash
   ./compile.sh  # or javac command
   ```
3. Run:
   ```bash
   ./run.sh  # or java command
   ```

---

## 🎓 Learning Path

If you're new to this project:

1. **Start Here**: Run the application and explore features
2. **Read**: README.md for overview
3. **Deep Dive**: FEATURES.md for detailed features
4. **Code Review**: Start with `ATMApplication.java` → `ATMService.java` → `Account.java`
5. **Experiment**: Modify and add features

---

## 📞 Support

For issues or questions:
- Check troubleshooting section above
- Review error messages carefully
- Ensure all prerequisites are met
- Verify file integrity and structure

---

## ✨ Next Steps

After successful installation:
1. ✅ Run the application
2. ✅ Test all features
3. ✅ Review the code
4. ✅ Customize if needed
5. ✅ Add to your resume/portfolio!

---

**Happy Banking! 🏦**
