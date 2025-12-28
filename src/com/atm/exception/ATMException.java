package com.atm.exception;

/**
 * Base exception for all ATM-related errors
 */
public class ATMException extends Exception {
    public ATMException(String message) {
        super(message);
    }

    public ATMException(String message, Throwable cause) {
        super(message, cause);
    }
}
