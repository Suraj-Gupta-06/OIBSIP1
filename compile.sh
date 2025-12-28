#!/bin/bash
# Compilation Script for ATM Banking System

echo "==============================================="
echo "   ATM Banking System - Compilation Script   "
echo "==============================================="
echo ""

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir -p bin
fi

# Compile all Java files
echo "Compiling Java source files..."
javac -d bin src/com/atm/*.java src/com/atm/*/*.java

# Check compilation status
if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Compilation successful!"
    echo ""
    echo "To run the application, use:"
    echo "  ./run.sh"
    echo ""
    echo "Or manually:"
    echo "  java -cp bin com.atm.ATMApplication"
    echo ""
else
    echo ""
    echo "✗ Compilation failed. Please check the errors above."
    echo ""
    exit 1
fi
