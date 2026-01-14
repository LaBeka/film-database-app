-- 1. Create the Roles table
--CREATE TABLE roles
--(
--    id   INT AUTO_INCREMENT PRIMARY KEY,
--    name VARCHAR(50) NOT NULL UNIQUE
--);

-- 2. Create Users Table
--CREATE TABLE users
--(
--    id               INT AUTO_INCREMENT PRIMARY KEY,
--    user_name        VARCHAR(255),
--    email            VARCHAR(255) NOT NULL UNIQUE,
--    password         VARCHAR(255) NOT NULL,
--    full_name        VARCHAR(255),
--    age              INT,
--    currently_active BOOLEAN DEFAULT TRUE
--);

-- 3. Create Join Table for User <-> Role
--CREATE TABLE users_roles
--(
--    user_id INT NOT NULL,
--    role_id INT NOT NULL,
--    PRIMARY KEY (user_id, role_id),
--    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
--    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
--);
-- 4. Create Reviews Table (Linked to User)
--CREATE TABLE reviews
--(
--    id       INT AUTO_INCREMENT PRIMARY KEY,
--    content  TEXT,
--    movie_id INT, -- Logical FK for your microservice
--    user_id  INT, -- Physical FK to User
--    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
--);

INSERT INTO roles(name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO users(user_name, email, password, full_name, age, currently_active)
VALUES ('jdoe', 'john@example.com', '$2a$12$uV2rFTwBrfqQh//ZWQNzEeBOhAFrLcESuYMsgGvQ4KkIvCICq2L/a', 'John Doe', 28, true),
       ('asmith', 'alice@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8Kn.2GYfAGFBue2PzV5S3nDX1N9vB9q.i5.', 'Alice Smith', 32, true),
       ('user', 'user@exe.com', '$2a$10$8.UnVuG9HHgffUDAlk8Kn.2GYfAGFBue2PzV5S3nDX1N9vB9q.i5.', 'User Alice', 32, false);

-- Assign Roles
-- John is a USER (1, 1), Alice is an USER and ADMIN (2, 1), (2, 2)
-- User not active is user and admin (3, 1), (3, 2);
INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 2);

-- Insert Reviews
INSERT INTO reviews (text, date, score)
VALUES
    ('Amazing movie!', "2026-01-01", 1),
    ('I didnt really like the ending.', "2026-01-02", 1),
    ('A masterpiece of cinema.', "2026-01-03", 2);