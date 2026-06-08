package com.library.model;

/**
 * Represents a library member (borrower).
 *
 * <p>Demonstrates <strong>Inheritance</strong> and <strong>Polymorphism</strong> by
 * extending {@link Person} and overriding {@link #displayDetails()}.
 *
 * @author  Library System
 * @version 1.0
 */
public class User extends Person {

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    /**
     * Default no-arg constructor.
     */
    public User() {
        super();
    }

    /**
     * Full constructor.
     *
     * @param userId unique user identifier
     * @param name   full name
     * @param email  email address
     * @param phone  contact phone
     */
    public User(int userId, String name, String email, String phone) {
        super(userId, name, email, phone);
    }

    /**
     * Constructor used when creating a new user (no ID assigned yet).
     *
     * @param name  full name
     * @param email email address
     * @param phone contact phone
     */
    public User(String name, String email, String phone) {
        super(0, name, email, phone);
    }

    // ---------------------------------------------------------------
    // Overridden Methods
    // ---------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * <p>Prints user-specific details including their role label.
     */
    @Override
    public void displayDetails() {
        System.out.println("+-------------------------------------------------+");
        System.out.printf("| %-10s: %-36d |%n", "User ID",  getUserId());
        System.out.printf("| %-10s: %-36s |%n", "Name",     getName());
        System.out.printf("| %-10s: %-36s |%n", "Email",    getEmail());
        System.out.printf("| %-10s: %-36s |%n", "Phone",    getPhone());
        System.out.printf("| %-10s: %-36s |%n", "Role",     "Library Member");
        System.out.println("+-------------------------------------------------+");
    }

    /**
     * Returns a compact string representation of this user.
     *
     * @return formatted string
     */
    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s', phone='%s'}",
                getUserId(), getName(), getEmail(), getPhone());
    }
}
