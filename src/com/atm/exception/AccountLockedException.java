package com.atm.exception;

/**
 * Exception thrown when account is locked due to security reasons
 */
public class AccountLockedException extends ATMException {
    public AccountLockedException(String message) {
        super(message);
    }
}
