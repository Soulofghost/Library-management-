package com.library.exception;

/**
 * Thrown when an attempt is made to borrow a book that is already on loan.
 *
 * @author  Library System
 * @version 1.0
 */
public class BookUnavailableException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message human-readable description of the availability issue
     */
    public BookUnavailableException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message human-readable description
     * @param cause   the underlying cause
     */
    public BookUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
