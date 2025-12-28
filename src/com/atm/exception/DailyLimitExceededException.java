package com.atm.exception;

/**
 * Exception thrown when daily withdrawal limit is exceeded
 */
public class DailyLimitExceededException extends ATMException {
    public DailyLimitExceededException(String message) {
        super(message);
    }
}
