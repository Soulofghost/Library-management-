package com.library.util;

/**
 * Utility class providing static input-validation helpers.
 *
 * <p>All methods are static; instantiation is prevented via a private constructor.
 *
 * @author  Library System
 * @version 1.0
 */
public final class InputValidator {

    /** Prevent instantiation. */
    private InputValidator() {}

    /**
     * Returns {@code true} if the given string is non-null and non-blank.
     *
     * @param value the string to test
     * @return {@code true} if the value has content
     */
    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Returns {@code true} if the string is a valid e-mail address.
     * Uses a simple regex – sufficient for UI-level validation.
     *
     * @param email the email to validate
     * @return {@code true} if the format appears valid
     */
    public static boolean isValidEmail(String email) {
        if (!isNonEmpty(email)) return false;
        return email.matches("^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Returns {@code true} if the string represents a positive integer.
     *
     * @param value the string to parse
     * @return {@code true} if the value is a positive integer
     */
    public static boolean isPositiveInt(String value) {
        try {
            return Integer.parseInt(value.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses and returns the integer value of the given string.
     *
     * @param value the string to parse
     * @return the parsed integer
     * @throws IllegalArgumentException if the string is not a valid integer
     */
    public static int parsePositiveInt(String value) {
        try {
            int v = Integer.parseInt(value.trim());
            if (v <= 0) throw new IllegalArgumentException("Value must be positive: " + value);
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a valid integer: " + value, e);
        }
    }

    /**
     * Trims and returns the given string, or throws if blank.
     *
     * @param value     the string to sanitise
     * @param fieldName label used in the exception message
     * @return trimmed, non-blank string
     * @throws IllegalArgumentException if the string is blank
     */
    public static String requireNonBlank(String value, String fieldName) {
        if (!isNonEmpty(value)) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }
}
