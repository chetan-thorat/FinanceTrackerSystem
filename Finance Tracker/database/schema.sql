-- Intelligent Expense Tracker Database Schema
-- MySQL Database with Optimized Indexes

CREATE DATABASE IF NOT EXISTS expense_tracker;
USE expense_tracker;

-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Expense Categories Table
CREATE TABLE expense_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    icon VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Expenses Table (with encrypted sensitive fields)
CREATE TABLE expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(500),
    expense_date DATE NOT NULL,
    payment_method VARCHAR(50),
    location VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES expense_categories(id),
    -- Optimized indexes for common queries
    INDEX idx_user_id (user_id),
    INDEX idx_category_id (category_id),
    INDEX idx_expense_date (expense_date),
    INDEX idx_user_date (user_id, expense_date),
    INDEX idx_user_category (user_id, category_id),
    INDEX idx_user_date_category (user_id, expense_date, category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Budgets Table
CREATE TABLE budgets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    period VARCHAR(20) NOT NULL DEFAULT 'MONTHLY',
    start_date DATE NOT NULL,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES expense_categories(id),
    INDEX idx_user_id (user_id),
    INDEX idx_user_category (user_id, category_id),
    INDEX idx_period (period)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Default Categories
INSERT INTO expense_categories (name, description, icon) VALUES
('Food', 'Restaurants, groceries, and dining', '🍔'),
('Travel', 'Transportation, flights, hotels', '✈️'),
('Bills', 'Utilities, rent, subscriptions', '💳'),
('Shopping', 'Clothing, electronics, general shopping', '🛍️'),
('Entertainment', 'Movies, games, events', '🎬'),
('Healthcare', 'Medical expenses, pharmacy', '🏥'),
('Education', 'Courses, books, training', '📚'),
('Other', 'Miscellaneous expenses', '📦');

