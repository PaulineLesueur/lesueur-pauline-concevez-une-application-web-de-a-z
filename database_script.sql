CREATE DATABASE pmb_database;
USE pmb_database;

CREATE TABLE user (
id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(100) NOT NULL,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
role VARCHAR(100)
);

INSERT INTO `user` (`id`, `username`, `password`, `first_name`, `last_name`, `role`)
VALUES (1, 'johndoe@email.com', '$2a$12$T/by6oHixXjarc5ZSx3QKOj5ZAlvXFJXOdPHqMLmwNGuEOVaqElgC', 'John', 'Doe', 'USER'),
(2, 'janesmith@email.com', '$2a$12$T/by6oHixXjarc5ZSx3QKOj5ZAlvXFJXOdPHqMLmwNGuEOVaqElgC', 'Jane', 'Smith', 'USER'),
(3, 'mikepeters@email.com', '$2a$12$T/by6oHixXjarc5ZSx3QKOj5ZAlvXFJXOdPHqMLmwNGuEOVaqElgC', 'Mike', 'Peters', 'USER');

CREATE TABLE account (
id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
user_id INTEGER,
balance DECIMAL(10, 2)
);

INSERT INTO `account` (`id`, `user_id`, `balance`)
VALUES (1, 1, 203),
(2, 2, 5620),
(3, 3, 10.00);

CREATE TABLE transaction (
id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
giver_account INTEGER,
receiver_account VARCHAR(255),
description VARCHAR(255),
amount DOUBLE,
fee DOUBLE
);

CREATE TABLE connection (
user_id INTEGER NOT NULL,
connection_id INTEGER NOT NULL,
PRIMARY KEY (user_id, connection_id),
FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
FOREIGN KEY (connection_id) REFERENCES user(id) ON DELETE CASCADE,
CHECK (user_id <> connection_id)
);

INSERT INTO `connection` (`user_id`, `connection_id`)
VALUES (1, 3),
(2, 1),
(3, 2);