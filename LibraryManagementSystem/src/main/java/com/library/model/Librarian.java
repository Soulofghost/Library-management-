package com.library.model;

/**
 * Represents a library staff member (librarian).
 *
 * <p>Demonstrates multi-level <strong>Inheritance</strong> by extending {@link User},
 * which itself extends {@link Person}.  The librarian role is kept as a model-layer
 * marker; actual database operations are performed via the DAO / Service layer.
 *
 * @author  Library System
 * @version 1.0
 */
public class Librarian extends User {

    /** Staff / employee ID specific to the librarian. */
    private String staffId;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    /**
     * Default no-arg constructor.
     */
    public Librarian() {
        super();
    }

    /**
     * Full constructor.
     *
     * @param userId  unique person identifier (inherited)
     * @param name    full name
     * @param email   email address
     * @param phone   contact phone
     * @param staffId employee / staff identifier
     */
    public Librarian(int userId, String name, String email, String phone, String staffId) {
        super(userId, name, email, phone);
        this.staffId = staffId;
    }

    // ---------------------------------------------------------------
    // Role-Specific Action Stubs
    // ---------------------------------------------------------------

    /**
     * Prints a confirmation that a book was added by this librarian.
     * Actual persistence is handled by {@link com.library.service.LibraryService}.
     *
     * @param bookTitle the title of the book added
     */
    public void addBook(String bookTitle) {
        System.out.println("[Librarian " + getName() + "] Added book: " + bookTitle);
    }

    /**
     * Prints a confirmation that a book was removed by this librarian.
     *
     * @param bookTitle the title of the book removed
     */
    public void removeBook(String bookTitle) {
        System.out.println("[Librarian " + getName() + "] Removed book: " + bookTitle);
    }

    /**
     * Prints a confirmation that a book was updated by this librarian.
     *
     * @param bookTitle the title of the updated book
     */
    public void updateBook(String bookTitle) {
        System.out.println("[Librarian " + getName() + "] Updated book: " + bookTitle);
    }

    /**
     * Prints a confirmation that a user account was managed by this librarian.
     *
     * @param userName the name of the user managed
     */
    public void manageUsers(String userName) {
        System.out.println("[Librarian " + getName() + "] Managed user: " + userName);
    }

    // ---------------------------------------------------------------
    // Overridden Methods
    // ---------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * <p>Extends the user display with librarian-specific fields.
     */
    @Override
    public void displayDetails() {
        System.out.println("+-------------------------------------------------+");
        System.out.printf("| %-10s: %-36d |%n", "User ID",  getUserId());
        System.out.printf("| %-10s: %-36s |%n", "Name",     getName());
        System.out.printf("| %-10s: %-36s |%n", "Email",    getEmail());
        System.out.printf("| %-10s: %-36s |%n", "Phone",    getPhone());
        System.out.printf("| %-10s: %-36s |%n", "Staff ID", staffId);
        System.out.printf("| %-10s: %-36s |%n", "Role",     "Librarian");
        System.out.println("+-------------------------------------------------+");
    }

    // ---------------------------------------------------------------
    // Getters & Setters
    // ---------------------------------------------------------------

    /**
     * Returns the librarian's staff identifier.
     *
     * @return staffId
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * Sets the librarian's staff identifier.
     *
     * @param staffId new staff ID value
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * Returns a compact string representation of this librarian.
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        return String.format("Librarian{id=%d, name='%s', staffId='%s'}",
                getUserId(), getName(), staffId);
    }
}
