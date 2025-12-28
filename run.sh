#!/bin/bash
# Run Script for ATM Banking System

echo "==============================================="
echo "     Starting ATM Banking System...           "
echo "==============================================="
echo ""

# Check if compiled
if [ ! -d "bin" ] || [ -z "$(ls -A bin 2>/dev/null)" ]; then
    echo "Application not compiled. Running compilation..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        echo "Compilation failed. Exiting..."
        exit 1
    fi
fi

# Run the application
java -cp bin com.atm.ATMApplication
