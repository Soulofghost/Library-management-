package com.library.model;

import java.time.LocalDate;

/**
 * Represents a single borrowing transaction in the library system.
 *
 * <p>A transaction tracks which user borrowed which book, when the book
 * was borrowed, and when (if ever) it was returned.  A {@code null}
 * {@code returnDate} indicates the book has not yet been returned.
 *
 * @author  Library System
 * @version 1.0
 */
public class BorrowTransaction {

    // ---------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------

    /** Database primary-key identifier. */
    private int transactionId;

    /** Foreign key referencing the borrowing user. */
    private int userId;

    /** Foreign key referencing the borrowed book. */
    private int bookId;

    /** Date the book was checked out. */
    private LocalDate borrowDate;

    /**
     * Date the book was returned; {@code null} if still on loan.
     */
    private LocalDate returnDate;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    /**
     * Default no-arg constructor.
     */
    public BorrowTransaction() {}

    /**
     * Full constructor (used when loading records from the database).
     *
     * @param transactionId unique transaction identifier
     * @param userId        borrowing user's ID
     * @param bookId        borrowed book's ID
     * @param borrowDate    date the book was checked out
     * @param returnDate    date the book was returned, or {@code null}
     */
    public BorrowTransaction(int transactionId, int userId, int bookId,
                             LocalDate borrowDate, LocalDate returnDate) {
        this.transactionId = transactionId;
        this.userId        = userId;
        this.bookId        = bookId;
        this.borrowDate    = borrowDate;
        this.returnDate    = returnDate;
    }

    /**
     * Constructor used when creating a new borrow record (no ID yet).
     *
     * @param userId     borrowing user's ID
     * @param bookId     borrowed book's ID
     * @param borrowDate date the book was checked out
     */
    public BorrowTransaction(int userId, int bookId, LocalDate borrowDate) {
        this(0, userId, bookId, borrowDate, null);
    }

    // ---------------------------------------------------------------
    // Display
    // ---------------------------------------------------------------

    /**
     * Prints a formatted summary of this transaction to {@code System.out}.
     */
    public void displayTransaction() {
        System.out.println("+-------------------------------------------+");
        System.out.printf("| %-15s: %-26d |%n", "Transaction ID", transactionId);
        System.out.printf("| %-15s: %-26d |%n", "User ID",        userId);
        System.out.printf("| %-15s: %-26d |%n", "Book ID",        bookId);
        System.out.printf("| %-15s: %-26s |%n", "Borrow Date",    borrowDate);
        System.out.printf("| %-15s: %-26s |%n", "Return Date",
                returnDate != null ? returnDate.toString() : "Not returned yet");
        System.out.println("+-------------------------------------------+");
    }

    // ---------------------------------------------------------------
    // Getters & Setters
    // ---------------------------------------------------------------

    /** @return the transactionId */
    public int getTransactionId()                         { return transactionId; }
    /** @param transactionId new value */
    public void setTransactionId(int transactionId)       { this.transactionId = transactionId; }

    /** @return the userId */
    public int getUserId()                                { return userId; }
    /** @param userId new value */
    public void setUserId(int userId)                     { this.userId = userId; }

    /** @return the bookId */
    public int getBookId()                                { return bookId; }
    /** @param bookId new value */
    public void setBookId(int bookId)                     { this.bookId = bookId; }

    /** @return the borrowDate */
    public LocalDate getBorrowDate()                      { return borrowDate; }
    /** @param borrowDate new value */
    public void setBorrowDate(LocalDate borrowDate)       { this.borrowDate = borrowDate; }

    /** @return the returnDate, or {@code null} if not yet returned */
    public LocalDate getReturnDate()                      { return returnDate; }
    /** @param returnDate new value (may be {@code null}) */
    public void setReturnDate(LocalDate returnDate)       { this.returnDate = returnDate; }

    /**
     * Returns a compact string representation of this transaction.
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        return String.format(
                "BorrowTransaction{id=%d, userId=%d, bookId=%d, borrow=%s, return=%s}",
                transactionId, userId, bookId, borrowDate, returnDate);
    }
}
