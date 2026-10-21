CREATE DATABASE IF NOT EXISTS user_management_api;
USE user_management_api;

CREATE TABLE users (
                       id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name        VARCHAR(100) NOT NULL,
                       cpf         VARCHAR(11)  NOT NULL UNIQUE,
                       email       VARCHAR(100) NOT NULL UNIQUE,
                       password    VARCHAR(64)  NOT NULL,
                       role        VARCHAR(20),
                       created_at  DATETIME(6)  NOT NULL,
                       birth_date  DATE         NOT NULL
);

CREATE TABLE books (
                       id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title       VARCHAR(255),
                       author      VARCHAR(255),
                       isbn        VARCHAR(255) UNIQUE,
                       quantity    INT,
                       created_at  DATETIME(6)
);

CREATE TABLE loans (
                       id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id     BIGINT         NOT NULL,
                       book_id     BIGINT         NOT NULL,
                       loan_date   DATE           NOT NULL,
                       return_date DATE,
                       loan_days   INT            NOT NULL,
                       daily_rate  DECIMAL(38,2)  NOT NULL,
                       CONSTRAINT fk_loans_user FOREIGN KEY (user_id) REFERENCES users(id),
                       CONSTRAINT fk_loans_book FOREIGN KEY (book_id) REFERENCES books(id)
);