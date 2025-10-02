# rewards-program-springboot

## Overview
This project calculates reward points for retail customers based on their transactions.

## Conditions
- 2 points for every dollar spent over $100 in each transaction.
- 1 point for every dollar spent between $50 and $100 in each transaction.
- $120 purchase = 90 points (2×20 + 1×50).

## Tech Stack
- Java 21
- Spring Boot 3.5.5
- MySQL (running on localhost:3306)
- JPA (Hibernate)
- Lombok
- JUnit 5
- Maven 3.8+

  ### Build
```bash
mvn clean install

 ** ### Run**
mvn spring-boot:run


**## Endpoints**

### 1. Get Total Rewards for Customer
**GET** `/api/rewards/customer/{id}?start=YYYY-MM-DD&end=YYYY-MM-DD` 
**Example:**
**GET** `/api/rewards/customer/1?start=2025-06-01&end=2025-08-31`

**Response:**

{
  "customerName": "John Doe",
  "email": "john.doe@example.com",
  "totalPoints": 360,
  "period": "2025-06-01 to 2025-08-31"
}

### 2. Get Monthly Rewards for Customer
**GET** `/api/rewards/customer/{id}/monthly?start=yyyy-MM-dd&end=yyyy-MM-dd`
**Example:**
**GET** `/api/rewards/customer/1/monthly?start=2025-06-01&end=2025-08-31`

**Response:**

{
  "2025-JUNE": 120,
  "2025-JULY": 90,
  "2025-AUGUST": 150
}


**Database Setup
MySQL Setup**
CREATE DATABASE IF NOT EXISTS rewards_db;
USE rewards_db;

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
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Insert Sample Data
INSERT INTO customer (first_name, last_name, email) VALUES
('John', 'Doe', 'john.doe@example.com'),
('Jane', 'Smith', 'jane.smith@example.com');

INSERT INTO transaction (customer_id, amount, transaction_date) VALUES
(1, 120.00, '2025-06-15 10:30:00'),
(1, 75.00,  '2025-07-05 12:15:00'),
(1, 200.00, '2025-08-20 14:45:00'),
(2, 50.00,  '2025-06-10 09:20:00'),
(2, 130.00, '2025-07-18 17:00:00'),
(2, 95.00,  '2025-08-25 19:30:00');


