package com.library.exception;

/**
 * Thrown when a requested book cannot be found in the database.
 *
 * @author  Library System
 * @version 1.0
 */
public class BookNotFoundException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message human-readable description of why the book was not found
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message human-readable description
     * @param cause   the underlying cause
     */
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
