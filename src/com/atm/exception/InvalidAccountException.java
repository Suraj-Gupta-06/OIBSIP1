package com.atm.exception;

/**
 * Exception thrown when account is not found or invalid
 */
public class InvalidAccountException extends ATMException {
    public InvalidAccountException(String message) {
        super(message);
    }
}
