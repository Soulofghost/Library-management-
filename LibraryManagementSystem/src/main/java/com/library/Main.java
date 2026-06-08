package com.library;

import com.library.config.DatabaseConnection;
import com.library.exception.*;
import com.library.service.LibraryService;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Entry point for the Library Management System console application.
 *
 * <p>Provides a fully interactive, menu-driven interface organised into:
 * <ul>
 *   <li>Book Management (add / update / delete / view)</li>
 *   <li>User Management (register / update / delete / view)</li>
 *   <li>Borrow / Return operations</li>
 *   <li>Search (books and users)</li>
 *   <li>Reports</li>
 * </ul>
 *
 * @author  Library System
 * @version 1.0
 */
public class Main {

    /** Service layer used by all menu handlers. */
    private static final LibraryService service = new LibraryService();

    /** Shared scanner for all console input. */
    private static final Scanner scanner = new Scanner(System.in);

    // ---------------------------------------------------------------
    // Application entry point
    // ---------------------------------------------------------------

    /**
     * Launches the application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        printBanner();

        // Verify DB connectivity before showing menus
        try {
            DatabaseConnection.getInstance();
        } catch (DatabaseConnectionException e) {
            System.err.println("[FATAL] Cannot connect to the database: " + e.getMessage());
            System.err.println("Please check your MySQL settings in DatabaseConnection.java and try again.");
            return;
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Select option: ");

            switch (choice) {
                case 1 -> bookManagementMenu();
                case 2 -> userManagementMenu();
                case 3 -> borrowBookMenu();
                case 4 -> returnBookMenu();
                case 5 -> searchBookMenu();
                case 6 -> searchUserMenu();
                case 7 -> reportsMenu();
                case 8 -> {
                    running = false;
                    shutdown();
                }
                default -> System.out.println("⚠ Invalid option. Please choose 1–8.");
            }
        }
    }

    // ================================================================
    // Main Menu
    // ================================================================

    private static void printBanner() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     LIBRARY MANAGEMENT SYSTEM        ║");
        System.out.println("║         Java + MySQL + JDBC          ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private static void printMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println(" 1. Book Management");
        System.out.println(" 2. User Management");
        System.out.println(" 3. Borrow Book");
        System.out.println(" 4. Return Book");
        System.out.println(" 5. Search Book");
        System.out.println(" 6. Search User");
        System.out.println(" 7. Reports");
        System.out.println(" 8. Exit");
        System.out.println("================================");
    }

    // ================================================================
    // Book Management Menu
    // ================================================================

    private static void bookManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- BOOK MANAGEMENT ---");
            System.out.println(" 1. Add Book");
            System.out.println(" 2. Update Book");
            System.out.println(" 3. Delete Book");
            System.out.println(" 4. View All Books");
            System.out.println(" 5. Back");

            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> addBookFlow();
                case 2 -> updateBookFlow();
                case 3 -> deleteBookFlow();
                case 4 -> service.viewAllBooks();
                case 5 -> back = true;
                default -> System.out.println("⚠ Invalid option.");
            }
        }
    }

    private static void addBookFlow() {
        System.out.println("\n--- Add New Book ---");
        String title  = readString("Title   : ");
        String author = readString("Author  : ");
        String isbn   = readString("ISBN    : ");
        String genre  = readString("Genre   : ");
        System.out.println("Status options: Available / Borrowed");
        String status = readString("Status  : ");
        if (status.isBlank()) status = "Available";

        try {
            service.addBook(title, author, isbn, genre, status);
        } catch (DuplicateBookException e) {
            System.out.println("✘ " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Validation error: " + e.getMessage());
        }
    }

    private static void updateBookFlow() {
        System.out.println("\n--- Update Book ---");
        int bookId = readInt("Book ID to update: ");
        String title  = readString("New Title  : ");
        String author = readString("New Author : ");
        String isbn   = readString("New ISBN   : ");
        String genre  = readString("New Genre  : ");
        String status = readString("New Status (Available/Borrowed): ");

        try {
            service.updateBook(bookId, title, author, isbn, genre, status);
        } catch (BookNotFoundException e) {
            System.out.println("✘ " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Validation error: " + e.getMessage());
        }
    }

    private static void deleteBookFlow() {
        System.out.println("\n--- Delete Book ---");
        int bookId = readInt("Book ID to delete: ");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirm)) {
            try {
                service.deleteBook(bookId);
            } catch (BookNotFoundException e) {
                System.out.println("✘ " + e.getMessage());
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // ================================================================
    // User Management Menu
    // ================================================================

    private static void userManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- USER MANAGEMENT ---");
            System.out.println(" 1. Register User");
            System.out.println(" 2. Update User");
            System.out.println(" 3. Delete User");
            System.out.println(" 4. View All Users");
            System.out.println(" 5. Back");

            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> registerUserFlow();
                case 2 -> updateUserFlow();
                case 3 -> deleteUserFlow();
                case 4 -> service.viewAllUsers();
                case 5 -> back = true;
                default -> System.out.println("⚠ Invalid option.");
            }
        }
    }

    private static void registerUserFlow() {
        System.out.println("\n--- Register New User ---");
        String name  = readString("Name  : ");
        String email = readString("Email : ");
        String phone = readString("Phone : ");

        try {
            service.registerUser(name, email, phone);
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Validation error: " + e.getMessage());
        }
    }

    private static void updateUserFlow() {
        System.out.println("\n--- Update User ---");
        int userId = readInt("User ID to update: ");
        String name  = readString("New Name  : ");
        String email = readString("New Email : ");
        String phone = readString("New Phone : ");

        try {
            service.updateUser(userId, name, email, phone);
        } catch (UserNotFoundException e) {
            System.out.println("✘ " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Validation error: " + e.getMessage());
        }
    }

    private static void deleteUserFlow() {
        System.out.println("\n--- Delete User ---");
        int userId = readInt("User ID to delete: ");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirm)) {
            try {
                service.deleteUser(userId);
            } catch (UserNotFoundException e) {
                System.out.println("✘ " + e.getMessage());
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // ================================================================
    // Borrow / Return
    // ================================================================

    private static void borrowBookMenu() {
        System.out.println("\n--- BORROW BOOK ---");
        int userId = readInt("User ID  : ");
        int bookId = readInt("Book ID  : ");

        try {
            service.borrowBook(userId, bookId);
        } catch (UserNotFoundException | BookNotFoundException | BookUnavailableException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private static void returnBookMenu() {
        System.out.println("\n--- RETURN BOOK ---");
        int bookId = readInt("Book ID to return: ");

        try {
            service.returnBook(bookId);
        } catch (BookNotFoundException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    // ================================================================
    // Search Menus
    // ================================================================

    private static void searchBookMenu() {
        System.out.println("\n--- SEARCH BOOK ---");
        System.out.println(" 1. By ID");
        System.out.println(" 2. By Title");
        System.out.println(" 3. By Author");
        System.out.println(" 4. By ISBN");
        System.out.println(" 5. By Genre");
        System.out.println(" 6. Back");

        int choice = readInt("Select option: ");
        try {
            switch (choice) {
                case 1 -> service.searchBookById(readInt("Book ID: "));
                case 2 -> service.searchBookByTitle(readString("Title keyword: "));
                case 3 -> service.searchBookByAuthor(readString("Author keyword: "));
                case 4 -> service.searchBookByISBN(readString("ISBN: "));
                case 5 -> service.searchBookByGenre(readString("Genre keyword: "));
                case 6 -> { /* return */ }
                default -> System.out.println("⚠ Invalid option.");
            }
        } catch (BookNotFoundException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private static void searchUserMenu() {
        System.out.println("\n--- SEARCH USER ---");
        System.out.println(" 1. By ID");
        System.out.println(" 2. By Name");
        System.out.println(" 3. Back");

        int choice = readInt("Select option: ");
        try {
            switch (choice) {
                case 1 -> service.searchUserById(readInt("User ID: "));
                case 2 -> service.searchUserByName(readString("Name keyword: "));
                case 3 -> { /* return */ }
                default -> System.out.println("⚠ Invalid option.");
            }
        } catch (UserNotFoundException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    // ================================================================
    // Reports Menu
    // ================================================================

    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTS ---");
            System.out.println(" 1. All Books");
            System.out.println(" 2. Available Books");
            System.out.println(" 3. Borrowed Books");
            System.out.println(" 4. Full Borrow History");
            System.out.println(" 5. User Borrow History");
            System.out.println(" 6. Back");

            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> service.reportAllBooks();
                case 2 -> service.reportAvailableBooks();
                case 3 -> service.reportBorrowedBooks();
                case 4 -> service.reportBorrowHistory();
                case 5 -> {
                    int uid = readInt("User ID: ");
                    service.reportUserBorrowHistory(uid);
                }
                case 6 -> back = true;
                default -> System.out.println("⚠ Invalid option.");
            }
        }
    }

    // ================================================================
    // Shutdown
    // ================================================================

    private static void shutdown() {
        System.out.println("\nClosing database connection...");
        try {
            DatabaseConnection.getInstance().closeConnection();
        } catch (DatabaseConnectionException e) {
            // Connection was already closed or never opened – safe to ignore
        }
        scanner.close();
        System.out.println("Thank you for using the Library Management System. Goodbye!");
    }

    // ================================================================
    // Console I/O helpers
    // ================================================================

    /**
     * Prints a prompt, reads a line, and returns it (trimmed).
     * Re-prompts if the line is blank.
     *
     * @param prompt the prompt to display
     * @return non-blank, trimmed input string
     */
    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("  ⚠ Input cannot be empty.");
        }
    }

    /**
     * Prints a prompt, reads an integer, and returns it.
     * Re-prompts on non-integer input.
     *
     * @param prompt the prompt to display
     * @return the parsed integer value
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume trailing newline
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // discard bad input
                System.out.println("  ⚠ Please enter a valid number.");
            }
        }
    }
}
