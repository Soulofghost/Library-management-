package com.library.config;

import com.library.exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton responsible for creating and managing the MySQL JDBC connection.
 *
 * <p>Usage:
 * <pre>
 *     Connection conn = DatabaseConnection.getInstance().getConnection();
 * </pre>
 *
 * <p>The connection parameters are read from constants in this class.
 * In a production system these would be externalised to a properties file
 * or environment variables.
 *
 * @author  Library System
 * @version 1.0
 */
public class DatabaseConnection {

    // ---------------------------------------------------------------
    // Connection constants – adjust to your environment
    // ---------------------------------------------------------------

    /** JDBC URL pointing to the library_db schema. */
    private static final String URL      = "jdbc:mysql://localhost:3306/library_db"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /** Database username. */
    private static final String USERNAME = "root";

    /** Database password. */
    private static final String PASSWORD = "password";

    // ---------------------------------------------------------------
    // Singleton state
    // ---------------------------------------------------------------

    /** The single shared instance of this class. */
    private static DatabaseConnection instance;

    /** The underlying JDBC connection object. */
    private Connection connection;

    // ---------------------------------------------------------------
    // Constructor (private – Singleton pattern)
    // ---------------------------------------------------------------

    /**
     * Private constructor: loads the MySQL driver and opens the connection.
     *
     * @throws DatabaseConnectionException if the driver cannot be loaded or
     *                                      the connection cannot be established
     */
    private DatabaseConnection() throws DatabaseConnectionException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("[DB] Connection established to library_db.");
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException(
                    "MySQL JDBC driver not found. Add mysql-connector-j to the classpath.", e);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Failed to connect to MySQL: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------

    /**
     * Returns the singleton {@link DatabaseConnection} instance, creating it
     * on the first call.
     *
     * @return the singleton instance
     * @throws DatabaseConnectionException if the connection cannot be established
     */
    public static synchronized DatabaseConnection getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Returns a live, validated JDBC {@link Connection}.
     *
     * <p>If the existing connection has been closed or become invalid,
     * a new connection is opened transparently.
     *
     * @return a valid {@link Connection} object
     * @throws DatabaseConnectionException if reconnection fails
     */
    public Connection getConnection() throws DatabaseConnectionException {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                System.out.println("[DB] Connection lost – reconnecting...");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Failed to validate / reopen the database connection: " + e.getMessage(), e);
        }
        return connection;
    }

    /**
     * Closes the underlying JDBC connection and resets the singleton so that
     * the next call to {@link #getInstance()} will open a fresh connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Warning: error while closing connection – " + e.getMessage());
        } finally {
            instance = null;
        }
    }
}
