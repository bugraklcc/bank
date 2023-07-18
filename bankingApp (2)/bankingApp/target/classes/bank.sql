DROP DATABASE IF EXISTS bank;
CREATE DATABASE bank;
USE bank;
CREATE TABLE users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE accounts (
  account_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  balance DECIMAL(10, 2) DEFAULT 0,
  currency VARCHAR(10) DEFAULT 'TRY',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE transactions (
  transaction_id INT PRIMARY KEY AUTO_INCREMENT,
  sender_id INT,
  receiver_id INT,
  amount DECIMAL(10, 2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (sender_id) REFERENCES users(user_id),
  FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);
ALTER TABLE accounts
ADD CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_sender FOREIGN KEY (sender_id) REFERENCES users(user_id);

ALTER TABLE transactions
ADD CONSTRAINT fk_transactions_receiver FOREIGN KEY (receiver_id) REFERENCES users(user_id);
