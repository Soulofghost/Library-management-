package com.library.exception;

/**
 * Thrown when a requested user cannot be found in the database.
 *
 * @author  Library System
 * @version 1.0
 */
public class UserNotFoundException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message human-readable description of why the user was not found
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message human-readable description
     * @param cause   the underlying cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
