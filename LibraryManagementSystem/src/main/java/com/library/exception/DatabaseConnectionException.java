package com.library.exception;

/**
 * Thrown when the application cannot establish or maintain a connection to MySQL.
 *
 * <p>This is a checked exception so callers are forced to handle DB connectivity
 * failures explicitly rather than letting them propagate as generic
 * {@link RuntimeException}s.
 *
 * @author  Library System
 * @version 1.0
 */
public class DatabaseConnectionException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message human-readable description of the connection failure
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and root cause.
     *
     * @param message human-readable description
     * @param cause   the underlying SQL or class-not-found exception
     */
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
