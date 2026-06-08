package com.library.model;

/**
 * Abstract base class representing a person in the library system.
 *
 * <p>Demonstrates <strong>Abstraction</strong> and <strong>Encapsulation</strong> OOP principles.
 * All concrete person types (User, Librarian) must extend this class and
 * provide their own implementation of {@link #displayDetails()}.
 *
 * @author  Library System
 * @version 1.0
 */
public abstract class Person {

    /** Unique identifier for this person. */
    private int userId;

    /** Full name of this person. */
    private String name;

    /** Email address (unique per person). */
    private String email;

    /** Contact phone number. */
    private String phone;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    /**
     * Default no-arg constructor required by DAO layer.
     */
    public Person() {}

    /**
     * Full constructor.
     *
     * @param userId unique person identifier
     * @param name   full name
     * @param email  email address
     * @param phone  contact phone
     */
    public Person(int userId, String name, String email, String phone) {
        this.userId = userId;
        this.name   = name;
        this.email  = email;
        this.phone  = phone;
    }

    // ---------------------------------------------------------------
    // Abstract Methods
    // ---------------------------------------------------------------

    /**
     * Displays a formatted summary of this person's details to {@code System.out}.
     * Subclasses must override to include role-specific information.
     */
    public abstract void displayDetails();

    // ---------------------------------------------------------------
    // Getters & Setters
    // ---------------------------------------------------------------

    /**
     * Returns the unique person identifier.
     *
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the unique person identifier.
     *
     * @param userId new identifier value
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the person's full name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the person's full name.
     *
     * @param name new name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the person's email address.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the person's email address.
     *
     * @param email new email value
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the person's phone number.
     *
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the person's phone number.
     *
     * @param phone new phone value
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
