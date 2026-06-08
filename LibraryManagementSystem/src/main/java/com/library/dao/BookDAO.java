package com.library.dao;

import com.library.model.Book;
import com.library.exception.BookNotFoundException;
import com.library.exception.DuplicateBookException;

import java.util.List;

/**
 * Data Access Object interface for {@link Book} entities.
 *
 * <p>Defines the full CRUD contract for book persistence.
 * All implementations must use {@code PreparedStatement} to prevent SQL injection.
 *
 * @author  Library System
 * @version 1.0
 */
public interface BookDAO {

    /**
     * Persists a new book to the database.
     *
     * @param book the book to add (bookId is ignored; assigned by DB)
     * @throws DuplicateBookException if a book with the same ISBN already exists
     */
    void addBook(Book book) throws DuplicateBookException;

    /**
     * Updates an existing book record.
     *
     * @param book the book with updated field values (bookId must be valid)
     * @throws BookNotFoundException if no book with that ID exists
     */
    void updateBook(Book book) throws BookNotFoundException;

    /**
     * Removes a book from the database by its ID.
     *
     * @param bookId the primary key of the book to delete
     * @throws BookNotFoundException if no book with that ID exists
     */
    void deleteBook(int bookId) throws BookNotFoundException;

    /**
     * Finds a book by its primary key.
     *
     * @param bookId the primary key to search for
     * @return the matching {@link Book}
     * @throws BookNotFoundException if not found
     */
    Book searchBookById(int bookId) throws BookNotFoundException;

    /**
     * Finds all books whose title contains the given keyword (case-insensitive).
     *
     * @param title the title keyword to search for
     * @return list of matching books (may be empty)
     */
    List<Book> searchBookByTitle(String title);

    /**
     * Finds all books whose author name contains the given keyword (case-insensitive).
     *
     * @param author the author keyword to search for
     * @return list of matching books (may be empty)
     */
    List<Book> searchBookByAuthor(String author);

    /**
     * Finds a book by its exact ISBN.
     *
     * @param isbn the ISBN to search for
     * @return the matching {@link Book}
     * @throws BookNotFoundException if not found
     */
    Book searchBookByISBN(String isbn) throws BookNotFoundException;

    /**
     * Finds all books whose genre matches the given keyword (case-insensitive).
     *
     * @param genre the genre keyword to search for
     * @return list of matching books (may be empty)
     */
    List<Book> searchBookByGenre(String genre);

    /**
     * Returns every book in the catalogue.
     *
     * @return list of all books (may be empty)
     */
    List<Book> getAllBooks();

    /**
     * Returns only books with status {@code "Available"}.
     *
     * @return list of available books (may be empty)
     */
    List<Book> getAvailableBooks();

    /**
     * Returns only books with status {@code "Borrowed"}.
     *
     * @return list of borrowed books (may be empty)
     */
    List<Book> getBorrowedBooks();
}
