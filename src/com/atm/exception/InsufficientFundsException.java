package com.atm.exception;

/**
 * Exception thrown when account has insufficient funds
 */
public class InsufficientFundsException extends ATMException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
