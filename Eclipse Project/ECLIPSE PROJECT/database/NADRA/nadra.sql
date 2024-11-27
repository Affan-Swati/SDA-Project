-- Create the database
CREATE DATABASE NADRA;

-- Use the newly created database
USE NADRA;

-- Create the CitizenInformation table
CREATE TABLE CitizenInformation 
(
    CNIC VARCHAR(13) PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Address VARCHAR(255) NOT NULL
);

-- ENTERIES FOR NADRA DATABASE
SELECT * FROM CitizenInformation;

DELETE FROM CitizenInformation;

INSERT INTO CitizenInformation (CNIC, Name, DateOfBirth, Address) 
VALUES ('4210123456789', 'Ahmed Ali', '1990-05-15', '123 Street A, Karachi, Sindh');

INSERT INTO CitizenInformation (CNIC, Name, DateOfBirth, Address) 
VALUES ('6110112345678', 'Fatima Khan', '1985-08-20', '456 Avenue B, Islamabad');

INSERT INTO CitizenInformation (CNIC, Name, DateOfBirth, Address) 
VALUES ('3520212345678', 'Usman Tariq', '1993-12-10', '789 Road C, Lahore, Punjab');

INSERT INTO CitizenInformation (CNIC, Name, DateOfBirth, Address) 
VALUES ('3740512345678', 'Ayesha Raza', '2020-02-25', '101 Colony D, Rawalpindi, Punjab');

INSERT INTO CitizenInformation (CNIC, Name, DateOfBirth, Address) 
VALUES ('4220123456789', 'Bilal Ahmed', '1995-09-18', '202 Sector E, Karachi, Sindh');

