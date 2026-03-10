CREATE TABLE users (

                       id BIGINT PRIMARY KEY AUTO_INCREMENT,

                       name VARCHAR(100) NOT NULL,

                       email VARCHAR(120) NOT NULL UNIQUE,

                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);