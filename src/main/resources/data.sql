-- 1. Create the Roles table
CREATE TABLE IF NOT EXISTS roles
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Create Users Table
CREATE TABLE users
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    user_name        VARCHAR(255),
    email            VARCHAR(255) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    full_name        VARCHAR(255),
    age              INT,
    currently_active BOOLEAN DEFAULT TRUE
);

-- 3. Create Join Table for User <-> Role
CREATE TABLE users_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);


CREATE TABLE films
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(255),
    genre           VARCHAR(100),
    -- For Lists, we often use TEXT or JSON in simple setups
    casta           TEXT,
    age_restriction INT,
    awards          TEXT,
    languages       TEXT,
    aspect_ratio    DOUBLE,
    color_status    VARCHAR(50),
    camera_used     VARCHAR(255)
);

CREATE TABLE reviews
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    text    TEXT, -- Use TEXT for longer review content
    date    DATE, -- Maps to LocalDate
    score   INT,
    user_id INT,  -- Foreign key for User
    film_id INT,  -- Foreign key for Film

    -- Foreign Key Constraints
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_review_film FOREIGN KEY (film_id) REFERENCES films (id)
);

INSERT INTO roles(name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO users(user_name, email, password, full_name, age, currently_active)
VALUES ('jdoe', 'john@example.com', '$2a$12$uV2rFTwBrfqQh//ZWQNzEeBOhAFrLcESuYMsgGvQ4KkIvCICq2L/a', 'John Doe', 28,
        true),
       ('asmith', 'alice@example.com', '$2a$12$uV2rFTwBrfqQh//ZWQNzEeBOhAFrLcESuYMsgGvQ4KkIvCICq2L/a', 'Alice Smith',
        32, true),
       ('user', 'user@exe.com', '$2a$12$uV2rFTwBrfqQh//ZWQNzEeBOhAFrLcESuYMsgGvQ4KkIvCICq2L/a', 'User Alice', 32,
        false);

-- Assign Roles
-- John is a USER (1, 1), Alice is an USER and ADMIN (2, 1), (2, 2)
-- User not active is user and admin (3, 1), (3, 2);

-- password for all listed users is pass

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 2);

INSERT INTO films(title, age_restriction, aspect_ratio)
VALUES ("testfilm1", 15, 2.2),
       ("testfilm2", 15, 2.2);

-- Insert Reviews
INSERT INTO reviews (text, date, score, film_id, user_id)
VALUES ('Amazing movie!', "2026-01-01", 1, 1, 3),
       ('A masterpiece of cinema.', "2026-01-03", 2, 2, 3);