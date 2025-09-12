-- Create Database
CREATE DATABASE IF NOT EXISTS rewards_db;
USE rewards_db;

-- Drop old tables if exist
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS customer;

-- Create Customer Table
CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE
);

-- Create Transaction Table
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_date DATETIME NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Insert Sample Customers
INSERT INTO customer (first_name, last_name, email) VALUES
('John', 'Doe', 'john.doe@example.com'),
('Jane', 'Smith', 'jane.smith@example.com');

-- Insert Sample Transactions (last 3 months)
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES
(1, 120.00, '2025-06-15 10:30:00'),  -- John
(1, 75.00,  '2025-07-05 12:15:00'),
(1, 200.00, '2025-08-20 14:45:00'),
(2, 50.00,  '2025-06-10 09:20:00'),  -- Jane
(2, 130.00, '2025-07-18 17:00:00'),
(2, 95.00,  '2025-08-25 19:30:00');



-- Show table content
select * from customer;
select * from transaction;
