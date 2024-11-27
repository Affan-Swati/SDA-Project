CREATE DATABASE FDCX;
USE FDCX;

CREATE TABLE Users 
(
    UserID VARCHAR(15) PRIMARY KEY,    -- Unique ID CNIC for each user
    Name VARCHAR(100) NOT NULL,               -- User's name
    DOB DATE NOT NULL,                         -- User's date of birth
    Email VARCHAR(100) NOT NULL,              -- User's email
    PhoneNumber VARCHAR(15),                  -- User's phone number
    JoinDate DATE NOT NULL,                   -- Join date of the user
    IsVerified BOOLEAN DEFAULT FALSE          -- Whether the user is verified or not
);

CREATE TABLE Accounts 
(
    AccountID INT AUTO_INCREMENT PRIMARY KEY,     -- Unique ID for each account
    Username VARCHAR(50) NOT NULL UNIQUE,         -- Username associated with the account
    Password VARCHAR(100) NOT NULL,               -- Password for the account
    Type ENUM('user', 'admin') NOT NULL,          -- Type of account: 'user' or 'admin'
    WalletID VARCHAR(50),                                 -- Foreign key to Wallet table (only for users)
    SubscriptionID INT,                           -- Foreign key to Subscription table (only for users)
    LoyaltyPoints INT DEFAULT 0,                  -- Loyalty points for the account
    UserID  VARCHAR(15),                                   -- Foreign key to Users table (for both users and admins)
    AdminID VARCHAR(15),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),  -- Link to Users table
    FOREIGN KEY (AdminID) REFERENCES Admins(AdminID),  -- Link to Users table
    FOREIGN KEY (WalletID) REFERENCES Wallets(WalletID),         -- Link to Wallet table
    FOREIGN KEY (SubscriptionID) REFERENCES Subscriptions(SubscriptionID) -- Link to Subscription table
);

CREATE TABLE Admins 
(
    AdminID VARCHAR(15) PRIMARY KEY,    -- Unique ID (CNIC) for each admin
	Name VARCHAR(100) NOT NULL,               -- User's name
    DOB DATE NOT NULL,                         -- User's DOB
    Email VARCHAR(100) NOT NULL,              -- User's email
    PhoneNumber VARCHAR(15),                  -- User's phone number
    JoinDate DATE NOT NULL                  -- Join date of the user
);

CREATE TABLE Stocks (
    StockID INT AUTO_INCREMENT PRIMARY KEY,      -- Unique ID for each stock entry
    Name VARCHAR(100) NOT NULL,                  -- Stock's name
    UnitPrice DECIMAL(10, 2) NOT NULL,           -- Stock's unit price
    PurchaseTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Time when the stock was purchased
    Available BOOLEAN DEFAULT TRUE               -- Whether the stock is available
);

CREATE TABLE Currencies 
(
    CurrencyID INT AUTO_INCREMENT PRIMARY KEY,   -- Unique ID for each currency
    CurrencyName VARCHAR(100) NOT NULL,          -- Name of the currency (e.g., US Dollar, Bitcoin)
    CurrencyCode VARCHAR(10) NOT NULL UNIQUE,    -- Code for the currency (e.g., USD, BTC)
    RateAgainstUSD DECIMAL(10, 2) NOT NULL,      -- Exchange rate compared to USD
    Type ENUM('Fiat', 'Crypto') NOT NULL,        -- Type of currency: "Fiat" or "Crypto"
    Amount DECIMAL(10, 2) DEFAULT 0              -- Amount of the currency
);

CREATE TABLE Wallets 
(
    WalletID VARCHAR(50) NOT NULL PRIMARY KEY,   -- Unique identifier for each wallet
    UserID varchar(13) NOT NULL,                         -- Foreign key to Users table
    FOREIGN KEY (UserID) REFERENCES Users(UserID) -- Link to Users table
);

CREATE TABLE Subscriptions 
(
    SubscriptionID INT AUTO_INCREMENT PRIMARY KEY, -- Unique ID for each subscription
    Type ENUM('monthly', 'quarterly', 'yearly', 'cancelled') NOT NULL, -- Type of subscription
    Price DECIMAL(10, 2) NOT NULL,                 -- Subscription price
    RenewalDate DATE NOT NULL                      -- Renewal date for the subscription
);

-- For stocks
CREATE TABLE UserStocks 
(
	UserID varchar(13) NOT NULL,
    StockID INT AUTO_INCREMENT PRIMARY KEY,       -- Unique ID for each stock in the system
    Name VARCHAR(100) NOT NULL,                   -- Stock's name (e.g., Apple, Tesla)
    UnitPrice DECIMAL(10, 2) NOT NULL,            -- Stock's unit price in USD
    Quantity INT DEFAULT 0,                       -- Quantity of the stock available
    Available BOOLEAN DEFAULT TRUE                -- Whether the stock is available for purchase
);

-- For currencies


CREATE TABLE UserCurrencies 
(
    CurrencyID INT AUTO_INCREMENT,   -- Unique ID for each currency in the system
    UserID varchar(13) NOT NULL,                                   -- Foreign key to Users table
    CurrencyName VARCHAR(100) NOT NULL,          -- Name of the currency (e.g., USD, BTC)
    CurrencyCode VARCHAR(10) NOT NULL ,    -- Code for the currency (e.g., USD, BTC)
    RateAgainstUSD DECIMAL(10, 2) NOT NULL,      -- Exchange rate compared to USD
	Amount DECIMAL(10,2) DEFAULT 0,              
    Type varchar(10)  NOT NULL,        -- Type of currency: "Fiat" or "Crypto"
    PRIMARY KEY (CurrencyID),                    -- Composite primary key
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE SystemStocks 
(
    StockID INT AUTO_INCREMENT PRIMARY KEY,       -- Unique ID for each stock in the system
    Name VARCHAR(100) NOT NULL,                   -- Stock's name (e.g., Apple, Tesla)
    UnitPrice DECIMAL(10, 2) NOT NULL,            -- Stock's unit price in USD
    Quantity INT DEFAULT 0,                       -- Quantity of the stock available
    Available BOOLEAN DEFAULT TRUE                -- Whether the stock is available for purchase
);

CREATE TABLE SystemCurrencies 
(
    CurrencyID INT AUTO_INCREMENT ,   -- Unique ID for each currency in the system
    CurrencyName VARCHAR(100) NOT NULL UNIQUE,          -- Name of the currency (e.g., USD, BTC)
    CurrencyCode VARCHAR(10) NOT NULL UNIQUE,    -- Code for the currency (e.g., USD, BTC)
    RateAgainstUSD DECIMAL(10, 2) NOT NULL,      -- Exchange rate compared to USD
	Amount DECIMAL(10,2) DEFAULT 0,                       -- Quantity of the stock available
    Type varchar(10) NOT NULL,                  -- Type of currency: "Fiat" or "Crypto"
    Available BOOLEAN DEFAULT TRUE,               -- Whether the currency is available in the system
	 PRIMARY KEY (CurrencyID)
);

CREATE TABLE TransactionLogs 
(
    UserID varchar(13) NOT NULL,                                     -- Foreign key to the Users table
	CurrencyCode VARCHAR(100) NOT NULL ,
    Amount Decimal(10,2) Not NULL, 
    Type VARCHAR(20) NOT NULL,
    TransactionDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Date and time of the transaction
    FOREIGN KEY (UserID) REFERENCES Users(UserID)   -- Link to Users table
);

INSERT INTO UserCurrenies (UserID, CurrencyName, CurrencyCode,RateAgainstUSD,Amount) VALUES ("3740583626159","Dollar","USD",1.0,10000);

UPDATE UserCurrencies SET Amount = 10000000 WHERE UserID = 4210123456789 AND CurrencyCode = "USD"

 Select * from Users;
 Select * from accounts;
 SELECT * FROM ADMINS;
 SELECT * FROM SYSTEMSTOCKS;
 SELECT * FROM USERSTOCKS;
 SELECT * FROM USERCURRENCIES;
 Select * From SystemCurrencies;
 SELECT * FROM TRANSACTIONLOGS;
 SELECT * FROM Subscriptions;
 SELECT * FROM Wallets;
  
Delete from SystemStocks;
DELETE from TransactionLogs;
Delete from UserStocks ;
Delete from SystemCurrencies;
Delete from UserCurrencies ;
DELETE FROM SYSTEMSTOCKS ; 
Delete from Accounts ; 
Delete from Wallets ;
Delete From Users ;
DELETE FROM ADMINS;
DELETE FROM SUBSCRIPTIONS;

INSERT INTO ADMINS VALUES ("4220123456789", "Bilal Ahmed", "1995-09-18", "bilalAhmed@email.com", "03121212121", CURDATE());
INSERT INTO Accounts (username, password, type, adminid) VALUES ("bilal", "bilal", 'admin', "4220123456789");

INSERT INTO SystemStocks (Name, UnitPrice, Quantity, Available) 
VALUES 
('Apple Stock', 17.00, 100, TRUE),
('Tesla Stock', 25.00, 50, TRUE),
('Microsoft Stock', 32.00, 75, TRUE);

INSERT INTO SystemCurrencies (CurrencyName, CurrencyCode, RateAgainstUSD, Amount, Type, Available) 
VALUES 
('US Dollar', 'USD', 1.00, 100000.00, 'Fiat', TRUE),
('Bitcoin', 'BTC', 0.350, 50.00, 'Crypto', TRUE),
('Ethereum', 'ETH', 0.20, 150.00, 'Crypto', TRUE);






