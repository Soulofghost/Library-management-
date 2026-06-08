package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.UserDAO;
import com.library.daoimpl.BookDAOImpl;
import com.library.daoimpl.TransactionDAOImpl;
import com.library.daoimpl.UserDAOImpl;
import com.library.exception.*;
import com.library.model.Book;
import com.library.model.BorrowTransaction;
import com.library.model.User;
import com.library.util.InputValidator;

import java.util.List;

/**
 * Central service layer that coordinates all library operations.
 *
 * <p>Acts as the single entry-point for the console UI ({@link com.library.Main}).
 * Delegates persistence to the DAO layer and applies business rules such as
 * availability checks before issuing books.
 *
 * <p>Demonstrates the <strong>Facade</strong> pattern: callers do not need to
 * know about DAOs, JDBC, or the database schema.
 *
 * @author  Library System
 * @version 1.0
 */
public class LibraryService {

    // ---------------------------------------------------------------
    // DAO dependencies
    // ---------------------------------------------------------------

    private final BookDAO        bookDAO;
    private final UserDAO        userDAO;
    private final TransactionDAO transactionDAO;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    /**
     * Creates a new {@code LibraryService} and wires up the DAO implementations.
     */
    public LibraryService() {
        this.bookDAO        = new BookDAOImpl();
        this.userDAO        = new UserDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
    }

    // ================================================================
    // BOOK OPERATIONS
    // ================================================================

    /**
     * Adds a new book to the catalogue after validating required fields.
     *
     * @param title  book title (must be non-blank)
     * @param author author name (must be non-blank)
     * @param isbn   ISBN (must be non-blank and unique)
     * @param genre  genre / category
     * @param status initial circulation status
     * @throws DuplicateBookException   if the ISBN already exists
     * @throws IllegalArgumentException if required fields are blank
     */
    public void addBook(String title, String author, String isbn, String genre, String status)
            throws DuplicateBookException {
        InputValidator.requireNonBlank(title,  "Title");
        InputValidator.requireNonBlank(author, "Author");
        InputValidator.requireNonBlank(isbn,   "ISBN");

        Book book = new Book(title.trim(), author.trim(), isbn.trim(),
                genre != null ? genre.trim() : "", status != null ? status.trim() : "Available");
        bookDAO.addBook(book);
        System.out.println("✔ Book added successfully with ID: " + book.getBookId());
    }

    /**
     * Updates every field of an existing book.
     *
     * @param bookId the book to update
     * @param title  new title
     * @param author new author
     * @param isbn   new ISBN
     * @param genre  new genre
     * @param status new status
     * @throws BookNotFoundException    if no book with {@code bookId} exists
     * @throws IllegalArgumentException if required fields are blank
     */
    public void updateBook(int bookId, String title, String author,
                           String isbn, String genre, String status)
            throws BookNotFoundException {
        InputValidator.requireNonBlank(title,  "Title");
        InputValidator.requireNonBlank(author, "Author");
        InputValidator.requireNonBlank(isbn,   "ISBN");

        Book book = new Book(bookId, title.trim(), author.trim(), isbn.trim(),
                genre != null ? genre.trim() : "",
                status != null ? status.trim() : "Available");
        bookDAO.updateBook(book);
        System.out.println("✔ Book updated successfully.");
    }

    /**
     * Deletes a book by its ID.
     *
     * @param bookId the ID of the book to delete
     * @throws BookNotFoundException if no book with that ID exists
     */
    public void deleteBook(int bookId) throws BookNotFoundException {
        bookDAO.deleteBook(bookId);
        System.out.println("✔ Book deleted successfully.");
    }

    /**
     * Searches for a book by ID and prints its details.
     *
     * @param bookId the ID to look up
     * @throws BookNotFoundException if not found
     */
    public void searchBookById(int bookId) throws BookNotFoundException {
        Book book = bookDAO.searchBookById(bookId);
        book.displayBook();
    }

    /**
     * Searches for books by title keyword and prints matching results.
     *
     * @param title the search keyword
     */
    public void searchBookByTitle(String title) {
        List<Book> books = bookDAO.searchBookByTitle(title);
        if (books.isEmpty()) {
            System.out.println("No books found matching title: " + title);
        } else {
            books.forEach(Book::displayBook);
        }
    }

    /**
     * Searches for books by author keyword and prints matching results.
     *
     * @param author the search keyword
     */
    public void searchBookByAuthor(String author) {
        List<Book> books = bookDAO.searchBookByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("No books found matching author: " + author);
        } else {
            books.forEach(Book::displayBook);
        }
    }

    /**
     * Searches for a book by exact ISBN and prints its details.
     *
     * @param isbn the ISBN to look up
     * @throws BookNotFoundException if not found
     */
    public void searchBookByISBN(String isbn) throws BookNotFoundException {
        bookDAO.searchBookByISBN(isbn).displayBook();
    }

    /**
     * Searches for books by genre keyword and prints matching results.
     *
     * @param genre the search keyword
     */
    public void searchBookByGenre(String genre) {
        List<Book> books = bookDAO.searchBookByGenre(genre);
        if (books.isEmpty()) {
            System.out.println("No books found in genre: " + genre);
        } else {
            books.forEach(Book::displayBook);
        }
    }

    /**
     * Prints all books in the catalogue.
     */
    public void viewAllBooks() {
        List<Book> books = bookDAO.getAllBooks();
        printBookList("ALL BOOKS", books);
    }

    /**
     * Prints all books currently available for borrowing.
     */
    public void viewAvailableBooks() {
        List<Book> books = bookDAO.getAvailableBooks();
        printBookList("AVAILABLE BOOKS", books);
    }

    /**
     * Prints all books currently on loan.
     */
    public void viewBorrowedBooks() {
        List<Book> books = bookDAO.getBorrowedBooks();
        printBookList("BORROWED BOOKS", books);
    }

    // ================================================================
    // USER OPERATIONS
    // ================================================================

    /**
     * Registers a new library member.
     *
     * @param name  member's full name (must be non-blank)
     * @param email member's email address (must be valid)
     * @param phone member's phone number
     * @throws IllegalArgumentException if name or email is invalid
     */
    public void registerUser(String name, String email, String phone) {
        InputValidator.requireNonBlank(name, "Name");
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
        User user = new User(name.trim(), email.trim(), phone != null ? phone.trim() : "");
        userDAO.registerUser(user);
        System.out.println("✔ User registered with ID: " + user.getUserId());
    }

    /**
     * Updates an existing user's details.
     *
     * @param userId the ID of the user to update
     * @param name   new full name
     * @param email  new email
     * @param phone  new phone
     * @throws UserNotFoundException    if no user with that ID exists
     * @throws IllegalArgumentException if name or email is invalid
     */
    public void updateUser(int userId, String name, String email, String phone)
            throws UserNotFoundException {
        InputValidator.requireNonBlank(name, "Name");
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
        User user = new User(userId, name.trim(), email.trim(), phone != null ? phone.trim() : "");
        userDAO.updateUser(user);
        System.out.println("✔ User updated successfully.");
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @throws UserNotFoundException if no user with that ID exists
     */
    public void deleteUser(int userId) throws UserNotFoundException {
        userDAO.deleteUser(userId);
        System.out.println("✔ User deleted successfully.");
    }

    /**
     * Searches for a user by ID and prints their details.
     *
     * @param userId the ID to look up
     * @throws UserNotFoundException if not found
     */
    public void searchUserById(int userId) throws UserNotFoundException {
        userDAO.searchUserById(userId).displayDetails();
    }

    /**
     * Searches for users by name keyword and prints matching results.
     *
     * @param name the search keyword
     */
    public void searchUserByName(String name) {
        List<User> users = userDAO.searchUserByName(name);
        if (users.isEmpty()) {
            System.out.println("No users found matching: " + name);
        } else {
            users.forEach(User::displayDetails);
        }
    }

    /**
     * Prints all registered users.
     */
    public void viewAllUsers() {
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        System.out.println("\n========== ALL USERS (" + users.size() + ") ==========");
        users.forEach(User::displayDetails);
    }

    // ================================================================
    // BORROW / RETURN OPERATIONS
    // ================================================================

    /**
     * Issues a book to a user, recording a borrow transaction.
     *
     * @param userId the ID of the borrowing user
     * @param bookId the ID of the book to borrow
     * @throws UserNotFoundException    if the user does not exist
     * @throws BookNotFoundException    if the book does not exist
     * @throws BookUnavailableException if the book is currently borrowed
     */
    public void borrowBook(int userId, int bookId)
            throws UserNotFoundException, BookNotFoundException, BookUnavailableException {
        transactionDAO.borrowBook(userId, bookId);
        System.out.println("✔ Book (ID " + bookId + ") successfully issued to user (ID " + userId + ").");
    }

    /**
     * Records the return of a book.
     *
     * @param bookId the ID of the book being returned
     * @throws BookNotFoundException if no open transaction exists for that book
     */
    public void returnBook(int bookId) throws BookNotFoundException {
        transactionDAO.returnBook(bookId);
        System.out.println("✔ Book (ID " + bookId + ") returned successfully.");
    }

    // ================================================================
    // REPORTS
    // ================================================================

    /**
     * Prints a complete catalogue report (all books).
     */
    public void reportAllBooks() {
        viewAllBooks();
    }

    /**
     * Prints a report of all available books.
     */
    public void reportAvailableBooks() {
        viewAvailableBooks();
    }

    /**
     * Prints a report of all borrowed books.
     */
    public void reportBorrowedBooks() {
        viewBorrowedBooks();
    }

    /**
     * Prints the full borrow history for all users.
     */
    public void reportBorrowHistory() {
        List<BorrowTransaction> txList = transactionDAO.getBorrowHistory();
        if (txList.isEmpty()) {
            System.out.println("No borrow history found.");
            return;
        }
        System.out.println("\n========== BORROW HISTORY (" + txList.size() + " records) ==========");
        txList.forEach(BorrowTransaction::displayTransaction);
    }

    /**
     * Prints the borrow history for a specific user.
     *
     * @param userId the user whose history to display
     */
    public void reportUserBorrowHistory(int userId) {
        List<BorrowTransaction> txList = transactionDAO.getUserBorrowHistory(userId);
        if (txList.isEmpty()) {
            System.out.println("No borrow history found for user ID: " + userId);
            return;
        }
        System.out.println("\n===== BORROW HISTORY FOR USER " + userId
                + " (" + txList.size() + " records) =====");
        txList.forEach(BorrowTransaction::displayTransaction);
    }

    // ================================================================
    // Private helpers
    // ================================================================

    /**
     * Prints a titled list of books, showing a message if the list is empty.
     *
     * @param title section header
     * @param books list of books to display
     */
    private void printBookList(String title, List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found in category: " + title);
            return;
        }
        System.out.println("\n========== " + title + " (" + books.size() + ") ==========");
        books.forEach(Book::displayBook);
    }
}
