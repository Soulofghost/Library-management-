package com.library.daoimpl;

import com.library.config.DatabaseConnection;
import com.library.dao.TransactionDAO;
import com.library.exception.BookNotFoundException;
import com.library.exception.BookUnavailableException;
import com.library.exception.DatabaseConnectionException;
import com.library.exception.UserNotFoundException;
import com.library.model.BorrowTransaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link TransactionDAO}.
 *
 * <p>Borrow and return operations are executed in a single database transaction
 * (via manual commit control) to ensure atomicity – a power cut mid-borrow will
 * not leave data in an inconsistent state.
 *
 * @author  Library System
 * @version 1.0
 */
public class TransactionDAOImpl implements TransactionDAO {

    // ---------------------------------------------------------------
    // SQL Statements
    // ---------------------------------------------------------------

    private static final String INSERT_TRANSACTION =
            "INSERT INTO borrow_transactions (user_id, book_id, borrow_date) VALUES (?, ?, ?)";

    private static final String UPDATE_BOOK_STATUS =
            "UPDATE books SET status=? WHERE book_id=?";

    private static final String SELECT_BOOK_STATUS =
            "SELECT status FROM books WHERE book_id=?";

    private static final String SELECT_USER_EXISTS =
            "SELECT COUNT(*) FROM users WHERE user_id=?";

    private static final String CLOSE_TRANSACTION =
            "UPDATE borrow_transactions SET return_date=? "
          + "WHERE book_id=? AND return_date IS NULL";

    private static final String SELECT_ALL_TRANSACTIONS =
            "SELECT * FROM borrow_transactions ORDER BY transaction_id";

    private static final String SELECT_USER_TRANSACTIONS =
            "SELECT * FROM borrow_transactions WHERE user_id=? ORDER BY transaction_id";

    private static final String SELECT_OPEN_TRANSACTION =
            "SELECT COUNT(*) FROM borrow_transactions WHERE book_id=? AND return_date IS NULL";

    // ---------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------

    /**
     * Returns a validated JDBC connection.
     */
    private Connection getConn() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
        }
    }

    /**
     * Maps the current row of the given {@link ResultSet} to a {@link BorrowTransaction}.
     */
    private BorrowTransaction mapRow(ResultSet rs) throws SQLException {
        Date returnSql = rs.getDate("return_date");
        return new BorrowTransaction(
                rs.getInt("transaction_id"),
                rs.getInt("user_id"),
                rs.getInt("book_id"),
                rs.getDate("borrow_date").toLocalDate(),
                returnSql != null ? returnSql.toLocalDate() : null
        );
    }

    // ---------------------------------------------------------------
    // TransactionDAO Implementation
    // ---------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * <p>Executes atomically: inserts the transaction row and updates book status
     * in a single JDBC transaction with manual commit.
     */
    @Override
    public void borrowBook(int userId, int bookId)
            throws UserNotFoundException, BookNotFoundException, BookUnavailableException {

        Connection conn = getConn();

        // 1. Validate user
        try (PreparedStatement userCheck = conn.prepareStatement(SELECT_USER_EXISTS)) {
            userCheck.setInt(1, userId);
            ResultSet rs = userCheck.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking user: " + e.getMessage(), e);
        }

        // 2. Validate book and check availability
        String currentStatus;
        try (PreparedStatement bookCheck = conn.prepareStatement(SELECT_BOOK_STATUS)) {
            bookCheck.setInt(1, bookId);
            ResultSet rs = bookCheck.executeQuery();
            if (!rs.next()) {
                throw new BookNotFoundException("Book not found with ID: " + bookId);
            }
            currentStatus = rs.getString("status");
        } catch (SQLException e) {
            throw new RuntimeException("Error checking book status: " + e.getMessage(), e);
        }

        if ("Borrowed".equalsIgnoreCase(currentStatus)) {
            throw new BookUnavailableException(
                    "Book (ID " + bookId + ") is currently borrowed and not available.");
        }

        // 3. Atomic: insert transaction + update book status
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement txStmt = conn.prepareStatement(INSERT_TRANSACTION)) {
                txStmt.setInt(1, userId);
                txStmt.setInt(2, bookId);
                txStmt.setDate(3, Date.valueOf(LocalDate.now()));
                txStmt.executeUpdate();
            }

            try (PreparedStatement statusStmt = conn.prepareStatement(UPDATE_BOOK_STATUS)) {
                statusStmt.setString(1, "Borrowed");
                statusStmt.setInt(2, bookId);
                statusStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("[TransactionDAO] Book " + bookId + " borrowed by user " + userId);

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            throw new RuntimeException("Error during borrow transaction: " + e.getMessage(), e);
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Executes atomically: closes the open transaction row and marks the book
     * as available in a single JDBC transaction.
     */
    @Override
    public void returnBook(int bookId) throws BookNotFoundException {

        Connection conn = getConn();

        // Verify there is an open transaction for this book
        try (PreparedStatement check = conn.prepareStatement(SELECT_OPEN_TRANSACTION)) {
            check.setInt(1, bookId);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new BookNotFoundException(
                        "No active borrow transaction found for book ID: " + bookId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking open transaction: " + e.getMessage(), e);
        }

        // Atomic: close transaction + mark book available
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement closeStmt = conn.prepareStatement(CLOSE_TRANSACTION)) {
                closeStmt.setDate(1, Date.valueOf(LocalDate.now()));
                closeStmt.setInt(2, bookId);
                closeStmt.executeUpdate();
            }

            try (PreparedStatement statusStmt = conn.prepareStatement(UPDATE_BOOK_STATUS)) {
                statusStmt.setString(1, "Available");
                statusStmt.setInt(2, bookId);
                statusStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("[TransactionDAO] Book " + bookId + " returned successfully.");

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            throw new RuntimeException("Error during return transaction: " + e.getMessage(), e);
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BorrowTransaction> getBorrowHistory() {
        List<BorrowTransaction> txList = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_ALL_TRANSACTIONS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) txList.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching borrow history: " + e.getMessage(), e);
        }
        return txList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BorrowTransaction> getUserBorrowHistory(int userId) {
        List<BorrowTransaction> txList = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_USER_TRANSACTIONS)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) txList.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user borrow history: " + e.getMessage(), e);
        }
        return txList;
    }
}
