package com.library.daoimpl;

import com.library.config.DatabaseConnection;
import com.library.dao.UserDAO;
import com.library.exception.DatabaseConnectionException;
import com.library.exception.UserNotFoundException;
import com.library.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link UserDAO}.
 *
 * <p>All SQL statements use {@link PreparedStatement} to guard against SQL injection.
 *
 * @author  Library System
 * @version 1.0
 */
public class UserDAOImpl implements UserDAO {

    // ---------------------------------------------------------------
    // SQL Statements
    // ---------------------------------------------------------------

    private static final String INSERT_USER =
            "INSERT INTO users (name, email, phone) VALUES (?, ?, ?)";

    private static final String UPDATE_USER =
            "UPDATE users SET name=?, email=?, phone=? WHERE user_id=?";

    private static final String DELETE_USER =
            "DELETE FROM users WHERE user_id=?";

    private static final String SELECT_BY_ID =
            "SELECT * FROM users WHERE user_id=?";

    private static final String SELECT_BY_NAME =
            "SELECT * FROM users WHERE LOWER(name) LIKE LOWER(?)";

    private static final String SELECT_ALL =
            "SELECT * FROM users ORDER BY user_id";

    // ---------------------------------------------------------------
    // Helper
    // ---------------------------------------------------------------

    /**
     * Returns a validated JDBC connection.
     */
    private Connection getConn() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
        }
    }

    /**
     * Maps the current row of the given {@link ResultSet} to a {@link User}.
     */
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

    // ---------------------------------------------------------------
    // UserDAO Implementation
    // ---------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerUser(User user) {
        try (PreparedStatement stmt = getConn().prepareStatement(INSERT_USER,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                user.setUserId(keys.getInt(1));
            }
            System.out.println("[UserDAO] User registered with ID: " + user.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException("Error registering user: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(User user) throws UserNotFoundException {
        searchUserById(user.getUserId()); // throws if not found

        try (PreparedStatement stmt = getConn().prepareStatement(UPDATE_USER)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setInt(4, user.getUserId());
            int rows = stmt.executeUpdate();
            System.out.println("[UserDAO] Rows updated: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(int userId) throws UserNotFoundException {
        searchUserById(userId); // throws if not found

        try (PreparedStatement stmt = getConn().prepareStatement(DELETE_USER)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            System.out.println("[UserDAO] User deleted: " + userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User searchUserById(int userId) throws UserNotFoundException {
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            throw new UserNotFoundException("User not found with ID: " + userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error searching user by ID: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> searchUserByName(String name) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_BY_NAME)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) users.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error searching user by name: " + e.getMessage(), e);
        }
        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = getConn().prepareStatement(SELECT_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) users.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all users: " + e.getMessage(), e);
        }
        return users;
    }
}
