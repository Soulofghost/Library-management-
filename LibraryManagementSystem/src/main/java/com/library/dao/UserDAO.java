package com.library.dao;

import com.library.model.User;
import com.library.exception.UserNotFoundException;

import java.util.List;

/**
 * Data Access Object interface for {@link User} entities.
 *
 * <p>Defines the full CRUD contract for user persistence.
 * All implementations must use {@code PreparedStatement} to prevent SQL injection.
 *
 * @author  Library System
 * @version 1.0
 */
public interface UserDAO {

    /**
     * Registers (inserts) a new user into the database.
     *
     * @param user the user to register (userId is ignored; assigned by DB)
     */
    void registerUser(User user);

    /**
     * Updates an existing user record.
     *
     * @param user the user with updated fields (userId must be valid)
     * @throws UserNotFoundException if no user with that ID exists
     */
    void updateUser(User user) throws UserNotFoundException;

    /**
     * Removes a user from the database.
     *
     * @param userId the primary key of the user to delete
     * @throws UserNotFoundException if no user with that ID exists
     */
    void deleteUser(int userId) throws UserNotFoundException;

    /**
     * Finds a user by their primary key.
     *
     * @param userId the primary key to look up
     * @return the matching {@link User}
     * @throws UserNotFoundException if not found
     */
    User searchUserById(int userId) throws UserNotFoundException;

    /**
     * Finds all users whose name contains the given keyword (case-insensitive).
     *
     * @param name the name keyword to search for
     * @return list of matching users (may be empty)
     */
    List<User> searchUserByName(String name);

    /**
     * Returns every registered user.
     *
     * @return list of all users (may be empty)
     */
    List<User> getAllUsers();
}
