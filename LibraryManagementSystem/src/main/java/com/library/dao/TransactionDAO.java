package com.library.dao;

import com.library.model.BorrowTransaction;
import com.library.exception.BookNotFoundException;
import com.library.exception.BookUnavailableException;
import com.library.exception.UserNotFoundException;

import java.util.List;

/**
 * Data Access Object interface for {@link BorrowTransaction} entities.
 *
 * <p>Handles all borrow / return persistence operations and provides
 * history query methods. All implementations must use {@code PreparedStatement}.
 *
 * @author  Library System
 * @version 1.0
 */
public interface TransactionDAO {

    /**
     * Records a new borrow transaction and marks the book as {@code "Borrowed"}.
     *
     * <p>The implementation must:
     * <ol>
     *   <li>Verify the user exists.</li>
     *   <li>Verify the book exists and is available.</li>
     *   <li>Insert the transaction row with today's date.</li>
     *   <li>Update the book's status to {@code "Borrowed"}.</li>
     * </ol>
     *
     * @param userId the ID of the user borrowing the book
     * @param bookId the ID of the book to borrow
     * @throws UserNotFoundException    if the user does not exist
     * @throws BookNotFoundException    if the book does not exist
     * @throws BookUnavailableException if the book is already borrowed
     */
    void borrowBook(int userId, int bookId)
            throws UserNotFoundException, BookNotFoundException, BookUnavailableException;

    /**
     * Records the return of a book by updating the open transaction and
     * marking the book as {@code "Available"}.
     *
     * <p>The implementation must:
     * <ol>
     *   <li>Find the open (un-returned) transaction for the given book.</li>
     *   <li>Set its {@code return_date} to today's date.</li>
     *   <li>Update the book's status to {@code "Available"}.</li>
     * </ol>
     *
     * @param bookId the ID of the book being returned
     * @throws BookNotFoundException if no open transaction exists for that book
     */
    void returnBook(int bookId) throws BookNotFoundException;

    /**
     * Returns the complete borrow history for all users.
     *
     * @return list of all {@link BorrowTransaction} records (may be empty)
     */
    List<BorrowTransaction> getBorrowHistory();

    /**
     * Returns the borrow history for a specific user.
     *
     * @param userId the ID of the user whose history to retrieve
     * @return list of that user's transactions (may be empty)
     */
    List<BorrowTransaction> getUserBorrowHistory(int userId);
}
