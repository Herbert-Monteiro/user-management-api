-- Library API - schema do banco (MySQL 8)
-- Uso: crie o banco e rode este script uma vez.
--   CREATE DATABASE user_management_api;
--   USE user_management_api;

CREATE TABLE users (
                       id          BIGINT          NOT NULL AUTO_INCREMENT,
                       name        VARCHAR(100)    NOT NULL,
                       cpf         VARCHAR(11)     NOT NULL,
                       email       VARCHAR(100)    NOT NULL,
                       password    VARCHAR(64)     NOT NULL,
                       role        ENUM('ADMIN', 'USER'),
                       created_at  DATETIME(6)     NOT NULL,
                       birth_date  DATE            NOT NULL,
                       deleted_at  DATETIME(6),
                       PRIMARY KEY (id),
                       CONSTRAINT uk_users_cpf   UNIQUE (cpf),
                       CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE books (
                       id          BIGINT          NOT NULL AUTO_INCREMENT,
                       title       VARCHAR(255)    NOT NULL,
                       author      VARCHAR(255)    NOT NULL,
                       isbn        VARCHAR(255)    NOT NULL,
                       quantity    INT             NOT NULL,
                       created_at  DATETIME(6),
                       PRIMARY KEY (id),
                       CONSTRAINT uk_books_isbn UNIQUE (isbn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE loans (
                       id           BIGINT         NOT NULL AUTO_INCREMENT,
                       user_id      BIGINT         NOT NULL,
                       book_id      BIGINT         NOT NULL,
                       loan_date    DATE           NOT NULL,
                       return_date  DATE,
                       loan_days    INT            NOT NULL,
                       daily_rate   DECIMAL(10,2)  NOT NULL,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_loans_user FOREIGN KEY (user_id) REFERENCES users (id),
                       CONSTRAINT fk_loans_book FOREIGN KEY (book_id) REFERENCES books (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;