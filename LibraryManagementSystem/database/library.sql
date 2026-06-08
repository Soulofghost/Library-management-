-- ============================================================
-- Library Management System - Database Schema
-- library_db | MySQL 8+
-- ============================================================

CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- ============================================================
-- Table: books
-- ============================================================
CREATE TABLE IF NOT EXISTS books (
    book_id   INT          PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(255) NOT NULL,
    author    VARCHAR(255) NOT NULL,
    isbn      VARCHAR(50)  NOT NULL UNIQUE,
    genre     VARCHAR(100),
    status    ENUM('Available','Borrowed') NOT NULL DEFAULT 'Available'
);

-- ============================================================
-- Table: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT          PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL UNIQUE,
    phone   VARCHAR(20)
);

-- ============================================================
-- Table: borrow_transactions
-- ============================================================
CREATE TABLE IF NOT EXISTS borrow_transactions (
    transaction_id INT  PRIMARY KEY AUTO_INCREMENT,
    user_id        INT  NOT NULL,
    book_id        INT  NOT NULL,
    borrow_date    DATE NOT NULL,
    return_date    DATE,
    CONSTRAINT fk_bt_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_bt_book FOREIGN KEY (book_id)
        REFERENCES books(book_id) ON DELETE CASCADE
);

-- ============================================================
-- Sample Data: 10 Books
-- ============================================================
INSERT INTO books (title, author, isbn, genre, status) VALUES
('Clean Code',                      'Robert C. Martin',  '978-0132350884', 'Technology',  'Available'),
('The Pragmatic Programmer',        'Andrew Hunt',       '978-0201616224', 'Technology',  'Available'),
('Design Patterns',                 'Gang of Four',      '978-0201633610', 'Technology',  'Borrowed'),
('Effective Java',                  'Joshua Bloch',      '978-0134685991', 'Technology',  'Available'),
('Introduction to Algorithms',      'Cormen et al.',     '978-0262033848', 'Computer Science','Borrowed'),
('The Great Gatsby',                'F. Scott Fitzgerald','978-0743273565','Fiction',     'Available'),
('To Kill a Mockingbird',           'Harper Lee',        '978-0061935466', 'Fiction',     'Available'),
('1984',                            'George Orwell',     '978-0451524935', 'Dystopian',   'Borrowed'),
('Sapiens',                         'Yuval Noah Harari', '978-0062316097', 'Non-Fiction', 'Available'),
('Atomic Habits',                   'James Clear',       '978-0735211292', 'Self-Help',   'Available');

-- ============================================================
-- Sample Data: 10 Users
-- ============================================================
INSERT INTO users (name, email, phone) VALUES
('Alice Johnson',   'alice@example.com',   '555-0101'),
('Bob Smith',       'bob@example.com',     '555-0102'),
('Carol White',     'carol@example.com',   '555-0103'),
('David Brown',     'david@example.com',   '555-0104'),
('Eva Martinez',    'eva@example.com',     '555-0105'),
('Frank Lee',       'frank@example.com',   '555-0106'),
('Grace Kim',       'grace@example.com',   '555-0107'),
('Henry Wilson',    'henry@example.com',   '555-0108'),
('Iris Taylor',     'iris@example.com',    '555-0109'),
('Jake Anderson',   'jake@example.com',    '555-0110');

-- ============================================================
-- Sample Data: 10 Borrow Transactions
-- ============================================================
INSERT INTO borrow_transactions (user_id, book_id, borrow_date, return_date) VALUES
(1,  3, '2024-01-10', '2024-01-24'),
(2,  5, '2024-01-15', NULL),
(3,  8, '2024-01-20', NULL),
(4,  1, '2024-02-01', '2024-02-15'),
(5,  2, '2024-02-05', '2024-02-19'),
(6,  4, '2024-02-10', '2024-02-24'),
(7,  6, '2024-02-15', '2024-03-01'),
(8,  7, '2024-02-20', '2024-03-06'),
(9,  9, '2024-03-01', '2024-03-15'),
(10,10, '2024-03-05', '2024-03-19');
