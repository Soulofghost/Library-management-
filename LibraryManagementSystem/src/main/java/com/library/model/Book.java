package com.library.model;

/**
 * Represents a book in the library catalogue.
 *
 * <p>Demonstrates <strong>Encapsulation</strong> through private fields and
 * public accessor methods.
 *
 * @author  Library System
 * @version 1.0
 */
public class Book {

    // ---------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------

    /** Database primary-key identifier. */
    private int bookId;

    /** Title of the book. */
    private String title;

    /** Author(s) of the book. */
    private String author;

    /** International Standard Book Number (unique). */
    private String isbn;

    /** Genre / category of the book. */
    private String genre;

    /**
     * Current circulation status.
     * Valid values: {@code "Available"} or {@code "Borrowed"}.
     */
    private String status;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    /**
     * Default no-arg constructor required by DAO layer.
     */
    public Book() {}

    /**
     * Full constructor (used when loading from database).
     *
     * @param bookId unique book identifier
     * @param title  book title
     * @param author author name(s)
     * @param isbn   ISBN string
     * @param genre  genre / category
     * @param status circulation status ("Available" or "Borrowed")
     */
    public Book(int bookId, String title, String author, String isbn,
                String genre, String status) {
        this.bookId = bookId;
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
        this.genre  = genre;
        this.status = status;
    }

    /**
     * Constructor used when adding a new book (no ID assigned yet).
     *
     * @param title  book title
     * @param author author name(s)
     * @param isbn   ISBN string
     * @param genre  genre / category
     * @param status initial circulation status
     */
    public Book(String title, String author, String isbn, String genre, String status) {
        this(0, title, author, isbn, genre, status);
    }

    // ---------------------------------------------------------------
    // Display
    // ---------------------------------------------------------------

    /**
     * Prints a formatted summary of this book's details to {@code System.out}.
     */
    public void displayBook() {
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("| %-10s: %-45d |%n", "Book ID", bookId);
        System.out.printf("| %-10s: %-45s |%n", "Title",   title);
        System.out.printf("| %-10s: %-45s |%n", "Author",  author);
        System.out.printf("| %-10s: %-45s |%n", "ISBN",    isbn);
        System.out.printf("| %-10s: %-45s |%n", "Genre",   genre);
        System.out.printf("| %-10s: %-45s |%n", "Status",  status);
        System.out.println("+----------------------------------------------------------+");
    }

    // ---------------------------------------------------------------
    // Getters & Setters
    // ---------------------------------------------------------------

    /** @return the bookId */
    public int getBookId()                { return bookId; }
    /** @param bookId new value */
    public void setBookId(int bookId)     { this.bookId = bookId; }

    /** @return the title */
    public String getTitle()              { return title; }
    /** @param title new value */
    public void setTitle(String title)    { this.title = title; }

    /** @return the author */
    public String getAuthor()             { return author; }
    /** @param author new value */
    public void setAuthor(String author)  { this.author = author; }

    /** @return the isbn */
    public String getIsbn()               { return isbn; }
    /** @param isbn new value */
    public void setIsbn(String isbn)      { this.isbn = isbn; }

    /** @return the genre */
    public String getGenre()              { return genre; }
    /** @param genre new value */
    public void setGenre(String genre)    { this.genre = genre; }

    /** @return the circulation status */
    public String getStatus()             { return status; }
    /** @param status new value ("Available" or "Borrowed") */
    public void setStatus(String status)  { this.status = status; }

    /**
     * Returns a compact string representation of this book.
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', isbn='%s', genre='%s', status='%s'}",
                bookId, title, author, isbn, genre, status);
    }
}
