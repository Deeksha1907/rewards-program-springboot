package com.example.rewards.exception;

/**
 * Thrown when the provided start date is after the end date.
 */
public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
