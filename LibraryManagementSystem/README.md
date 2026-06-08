# 📚 Library Management System

A complete, production-ready **Library Management System** built with **Java 17**, **MySQL 8**, **JDBC**, and **Maven** following the **DAO design pattern** and core **Object-Oriented Programming** principles.

---

## 🗂 Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Installation & Running](#installation--running)
- [Console Menu Reference](#console-menu-reference)
- [UML Class Diagram](#uml-class-diagram)
- [Future Enhancements](#future-enhancements)

---

## Project Overview

This system manages the day-to-day operations of a library, including cataloguing books, registering members, issuing and returning books, and generating reports. All data is persisted to a MySQL database, accessed through a clean DAO layer.

---

## Features

| Category | Feature |
|---|---|
| **Books** | Add, Update, Delete, Search (by ID / Title / Author / ISBN / Genre), View All / Available / Borrowed |
| **Users** | Register, Update, Delete, Search (by ID / Name), View All |
| **Transactions** | Borrow book (with availability check), Return book (atomically) |
| **Reports** | All books, Available books, Borrowed books, Full borrow history, Per-user borrow history |
| **Safety** | Duplicate ISBN prevention, Borrowing unavailable books blocked, Custom exceptions, Input validation |
| **DB** | Singleton connection, PreparedStatement everywhere, Atomic borrow/return via JDBC transactions |

---

## Technologies Used

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core language |
| MySQL | 8+ | Relational database |
| JDBC | — | Database connectivity |
| Maven | 3.8+ | Build & dependency management |
| mysql-connector-j | 8.3.0 | MySQL JDBC driver |
| maven-shade-plugin | 3.5.1 | Executable fat JAR |

---

## OOP Concepts Demonstrated

| Concept | Where |
|---|---|
| **Encapsulation** | All model fields are `private` with getters/setters |
| **Abstraction** | `Person` abstract class; `BookDAO`, `UserDAO`, `TransactionDAO` interfaces |
| **Inheritance** | `User extends Person`, `Librarian extends User` |
| **Polymorphism** | `displayDetails()` overridden in `User` and `Librarian`; DAO interface / impl separation |
| **Exception Handling** | Five custom checked exceptions; try-catch at service and Main layers |
| **Singleton Pattern** | `DatabaseConnection` |
| **DAO Pattern** | Interface + Impl for Book, User, Transaction |
| **Facade Pattern** | `LibraryService` hides all DAO complexity from `Main` |

---

## Project Structure

```
LibraryManagementSystem/
├── pom.xml
├── README.md
├── database/
│   └── library.sql                         ← Schema + sample data
└── src/main/java/com/library/
    ├── Main.java                           ← Console menu entry point
    ├── config/
    │   └── DatabaseConnection.java         ← Singleton JDBC manager
    ├── model/
    │   ├── Person.java                     ← Abstract base class
    │   ├── User.java                       ← Library member
    │   ├── Librarian.java                  ← Staff (extends User)
    │   ├── Book.java                       ← Catalogue entry
    │   └── BorrowTransaction.java          ← Borrow/return record
    ├── dao/
    │   ├── BookDAO.java                    ← Book CRUD interface
    │   ├── UserDAO.java                    ← User CRUD interface
    │   └── TransactionDAO.java             ← Transaction interface
    ├── daoimpl/
    │   ├── BookDAOImpl.java                ← JDBC book impl
    │   ├── UserDAOImpl.java                ← JDBC user impl
    │   └── TransactionDAOImpl.java         ← JDBC transaction impl
    ├── service/
    │   └── LibraryService.java             ← Business logic facade
    ├── util/
    │   └── InputValidator.java             ← Validation helpers
    └── exception/
        ├── BookNotFoundException.java
        ├── UserNotFoundException.java
        ├── BookUnavailableException.java
        ├── DuplicateBookException.java
        └── DatabaseConnectionException.java
```

---

## Database Setup

1. Start your MySQL 8 server.
2. Open a MySQL client (MySQL Workbench, DBeaver, or the CLI).
3. Run the SQL script:
   ```sql
   SOURCE /path/to/LibraryManagementSystem/database/library.sql;
   ```
   Or paste the contents of `database/library.sql` directly.

This will:
- Create the `library_db` database.
- Create the `books`, `users`, and `borrow_transactions` tables with foreign keys.
- Insert 10 sample books, 10 sample users, and 10 sample transactions.

---

## Installation & Running

### Prerequisites
- Java 17+ (`java -version`)
- Maven 3.8+ (`mvn -version`)
- MySQL 8+ running locally

### Step 1 – Configure database credentials

Open `src/main/java/com/library/config/DatabaseConnection.java` and update:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/library_db...";
private static final String USERNAME = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

### Step 2 – Build

```bash
cd LibraryManagementSystem
mvn clean package
```

This produces a runnable fat JAR at:
```
target/LibraryManagementSystem-1.0.0-runnable.jar
```

### Step 3 – Run

```bash
java -jar target/LibraryManagementSystem-1.0.0-runnable.jar
```

---

## Console Menu Reference

```
╔══════════════════════════════════════╗
║     LIBRARY MANAGEMENT SYSTEM        ║
╚══════════════════════════════════════╝

========== MAIN MENU ==========
 1. Book Management
 2. User Management
 3. Borrow Book
 4. Return Book
 5. Search Book
 6. Search User
 7. Reports
 8. Exit
================================
```

### Book Management Sub-Menu
```
 1. Add Book          → prompts for title, author, ISBN, genre, status
 2. Update Book       → prompts for book ID + new values
 3. Delete Book       → prompts for book ID + confirmation
 4. View All Books    → tabular display of every book
 5. Back
```

### User Management Sub-Menu
```
 1. Register User     → prompts for name, email, phone
 2. Update User       → prompts for user ID + new values
 3. Delete User       → prompts for user ID + confirmation
 4. View All Users    → tabular display of every user
 5. Back
```

### Reports Sub-Menu
```
 1. All Books
 2. Available Books
 3. Borrowed Books
 4. Full Borrow History
 5. User Borrow History   → prompts for user ID
 6. Back
```

---

## UML Class Diagram

```
┌───────────────────────────────────────────────────┐
│                   <<abstract>>                    │
│                     Person                        │
│───────────────────────────────────────────────────│
│ - userId: int                                     │
│ - name: String                                    │
│ - email: String                                   │
│ - phone: String                                   │
│───────────────────────────────────────────────────│
│ + getters/setters                                 │
│ + {abstract} displayDetails(): void               │
└───────────────────────┬───────────────────────────┘
                        │ extends
          ┌─────────────┴─────────────┐
          │                           │
┌─────────▼──────────┐      ┌─────────▼──────────────────┐
│       User         │      │        Librarian            │
│────────────────────│      │─────────────────────────────│
│                    │      │ - staffId: String           │
│────────────────────│      │─────────────────────────────│
│ + displayDetails() │      │ + addBook(String): void     │
└────────────────────┘      │ + removeBook(String): void  │
                            │ + updateBook(String): void  │
                            │ + manageUsers(String): void │
                            │ + displayDetails(): void    │
                            └─────────────────────────────┘

┌───────────────────────────────────┐
│             Book                  │
│───────────────────────────────────│
│ - bookId: int                     │
│ - title: String                   │
│ - author: String                  │
│ - isbn: String                    │
│ - genre: String                   │
│ - status: String                  │
│───────────────────────────────────│
│ + constructors                    │
│ + getters/setters                 │
│ + displayBook(): void             │
└───────────────────────────────────┘

┌───────────────────────────────────┐
│        BorrowTransaction          │
│───────────────────────────────────│
│ - transactionId: int              │
│ - userId: int                     │
│ - bookId: int                     │
│ - borrowDate: LocalDate           │
│ - returnDate: LocalDate           │
│───────────────────────────────────│
│ + constructors                    │
│ + getters/setters                 │
│ + displayTransaction(): void      │
└───────────────────────────────────┘

<<interface>>  BookDAO
  + addBook(Book)
  + updateBook(Book)
  + deleteBook(int)
  + searchBookById(int)
  + searchBookByTitle(String)
  + searchBookByAuthor(String)
  + searchBookByISBN(String)
  + searchBookByGenre(String)
  + getAllBooks()
  + getAvailableBooks()
  + getBorrowedBooks()
         ▲ implements
  BookDAOImpl (JDBC)

<<interface>>  UserDAO
  + registerUser(User)
  + updateUser(User)
  + deleteUser(int)
  + searchUserById(int)
  + searchUserByName(String)
  + getAllUsers()
         ▲ implements
  UserDAOImpl (JDBC)

<<interface>>  TransactionDAO
  + borrowBook(int, int)
  + returnBook(int)
  + getBorrowHistory()
  + getUserBorrowHistory(int)
         ▲ implements
  TransactionDAOImpl (JDBC)

LibraryService ──uses──► BookDAO
               ──uses──► UserDAO
               ──uses──► TransactionDAO

Main ──uses──► LibraryService
     ──uses──► DatabaseConnection (Singleton)
```

---

## Screenshots Section

> _Screenshots will be added once the application is running. Run `java -jar target/LibraryManagementSystem-1.0.0-runnable.jar` and capture the menus._

---

## Future Enhancements

| Enhancement | Description |
|---|---|
| GUI | JavaFX or Swing front-end to replace the console |
| Web API | Spring Boot REST layer exposing the service |
| Fine System | Automatic fine calculation for overdue books |
| Authentication | Librarian login with hashed passwords |
| Pagination | Paginated book / user listing for large datasets |
| Connection Pool | HikariCP connection pooling for multi-threaded use |
| Unit Tests | JUnit 5 + Mockito test suite |
| Logging | SLF4J + Logback replacing `System.out` |
| Docker | Containerised MySQL + app with `docker-compose` |
| Export | PDF / CSV report export via iText / Apache POI |

---

## License

This project is released for educational purposes. Feel free to use and extend it.
