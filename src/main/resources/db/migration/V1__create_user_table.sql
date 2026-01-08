CREATE SCHEMA IF NOT EXISTS ts;

CREATE TABLE IF NOT EXISTS ts.user_info (
    UserID int NOT NULL PRIMARY KEY,
    LastName varchar(255) NOT NULL,
    FirstName varchar(255),
    Age int,
    Gender varchar(10),
    City varchar(255),
    PhoneNumber varchar(15),
    Email varchar(255) UNIQUE
);