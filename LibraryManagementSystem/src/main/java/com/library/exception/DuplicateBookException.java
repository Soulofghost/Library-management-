package com.library.exception;

/**
 * Thrown when an attempt is made to add a book whose ISBN already exists in the catalogue.
 *
 * @author  Library System
 * @version 1.0
 */
public class DuplicateBookException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message human-readable description identifying the duplicate ISBN
     */
    public DuplicateBookException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message human-readable description
     * @param cause   the underlying cause
     */
    public DuplicateBookException(String message, Throwable cause) {
        super(message, cause);
    }
}
