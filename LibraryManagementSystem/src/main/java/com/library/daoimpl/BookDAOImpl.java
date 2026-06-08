package com.library.daoimpl;

import com.library.config.DatabaseConnection;
import com.library.dao.BookDAO;
import com.library.exception.BookNotFoundException;
import com.library.exception.DatabaseConnectionException;
import com.library.exception.DuplicateBookException;
import com.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link BookDAO}.
 *
 * <p>Every method uses a {@link PreparedStatement} to prevent SQL injection.
 * {@link DatabaseConnectionException} is re-thrown as an unchecked
 * {@link RuntimeException} so callers are not forced to handle it at every call-site
 * (it is still caught and logged by the service layer).
 *
 * @author  Library System
 * @version 1.0
 */
public class BookDAOImpl implements BookDAO {

    // ---------------------------------------------------------------
    // SQL Statements
    // ---------------------------------------------------------------

    private static final String INSERT_BOOK =
            "INSERT INTO books (title, author, isbn, genre, status) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_BOOK =
            "UPDATE books SET title=?, author=?, isbn=?, genre=?, status=? WHERE book_id=?";

    private static final String DELETE_BOOK =
            "DELETE FROM books WHERE book_id=?";

    private static final String SELECT_BY_ID =
            "SELECT * FROM books WHERE book_id=?";

    private static final String SELECT_BY_TITLE =
            "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(?)";

    private static final String SELECT_BY_AUTHOR =
            "SELECT * FROM books WHERE LOWER(author) LIKE LOWER(?)";

    private static final String SELECT_BY_ISBN =
            "SELECT * FROM books WHERE isbn=?";

    private static final String SELECT_BY_GENRE =
            "SELECT * FROM books WHERE LOWER(genre) LIKE LOWER(?)";

    private static final String SELECT_ALL =
            "SELECT * FROM books ORDER BY book_id";

    private static final String SELECT_AVAILABLE =
            "SELECT * FROM books WHERE status='Available' ORDER BY book_id";

    private static final String SELECT_BORROWED =
            "SELECT * FROM books WHERE status='Borrowed' ORDER BY book_id";

    private static final String CHECK_ISBN =
            "SELECT COUNT(*) FROM books WHERE isbn=?";

    // ---------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------

    /**
     * Returns a validated connection from the singleton.
     */
    private Connection getConn() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
        }
    }

    /**
     * Maps the current row of the given {@link ResultSet} to a {@link Book}.
     */
    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getString("genre"),
                rs.getString("status")
        );
    }

    // ---------------------------------------------------------------
    // BookDAO Implementation
    // ---------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBook(Book book) throws DuplicateBookException {
        // Check for duplicate ISBN first
        try (PreparedStatement checkStmt = getConn().prepareStatement(CHECK_ISBN)) {
            checkStmt.setString(1, book.getIsbn());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new DuplicateBookException(
                        "A book with ISBN '" + book.getIsbn() + "' already exists.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking ISBN uniqueness: " + e.getMessage(), e);
        }

        try (PreparedStatement stmt = getConn().prepareStatement(INSERT_BOOK,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getStatus());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                book.setBookId(keys.getInt(1));
            }
            System.out.println("[BookDAO] Book added with ID: " + book.getBookId());
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting book: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBook(Book book) throws BookNotFoundException {
        searchBookById(book.getBookId()); // throws if not found

        try (PreparedStatement stmt = getConn().prepareStatement(UPDATE_BOOK)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getStatus());
            stmt.setInt(6, book.getBookId());
            int rows = stmt.executeUpdate();
            System.out.println("[BookDAO] Rows updated: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating book: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBook(int bookId) throws BookNotFoundException {
        searchBookById(bookId); // throws if not found

        try (PreparedStatement stmt = getConn().prepareStatement(DELETE_BOOK)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            System.out.println("[BookDAO] Book deleted: " + bookId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Book searchBookById(int bookId) throws BookNotFoundException {
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            throw new BookNotFoundException("Book not found with ID: " + bookId);
        } catch (SQLException e) {
            throw new RuntimeException("Error searching book by ID: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> searchBookByTitle(String title) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BY_TITLE)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error searching book by title: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> searchBookByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BY_AUTHOR)) {
            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error searching book by author: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Book searchBookByISBN(String isbn) throws BookNotFoundException {
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BY_ISBN)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            throw new BookNotFoundException("Book not found with ISBN: " + isbn);
        } catch (SQLException e) {
            throw new RuntimeException("Error searching book by ISBN: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> searchBookByGenre(String genre) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BY_GENRE)) {
            stmt.setString(1, "%" + genre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error searching book by genre: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all books: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_AVAILABLE)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching available books: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> getBorrowedBooks() {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BORROWED)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) books.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching borrowed books: " + e.getMessage(), e);
        }
        return books;
    }
}
