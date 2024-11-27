-- Step 1: Create the FBR database
CREATE DATABASE FBR;

-- Step 2: Use the FBR database
USE FBR;

-- Step 3: Create the CitizenPurchases table
CREATE TABLE TransactionLogs 
(
    TransactionID INT AUTO_INCREMENT PRIMARY KEY,   -- Unique identifier for each transaction
    CNIC VARCHAR(15) NOT NULL,                     -- Citizen's CNIC
    Name VARCHAR(100) NOT NULL,                    -- Citizen's name
    DateOfTransaction DATE NOT NULL,               -- Date of the transaction
    TransactionType ENUM('Buy', 'Sell') NOT NULL,  -- Indicates whether the transaction is a buy or sell
    AssetType ENUM('Fiat', 'Crypto') NOT NULL,  -- Type of asset (Fiat or Crypto)
    AssetName VARCHAR(100) NOT NULL,               -- Name of the asset (e.g., Bitcoin, Apple Stock)
    AssetCode VARCHAR(100),                         -- Code for the asset (e.g., BTC, AAPL)
    Quantity DECIMAL(10, 2) NOT NULL,              -- Quantity of the asset
    UnitPrice DECIMAL(10, 2) NOT NULL,             -- Price per unit in USD
    TotalValue DECIMAL(10, 2) AS (Quantity * UnitPrice) STORED, -- Total value of the transaction
    TaxPercentage DECIMAL(5, 2) NOT NULL,          -- Tax percentage applied
    TaxCollected DECIMAL(10, 2) AS (TotalValue * TaxPercentage) STORED, -- Tax collected
    Remarks VARCHAR(255) DEFAULT NULL              -- Optional remarks
);

-- Step 4: Create an index for faster searches on CNIC and AssetType
CREATE INDEX idx_CNIC_AssetType ON CitizenPurchases (CNIC, AssetType);

SELECT * FROM TransactionLogs;

INSERT INTO TransactionLogs 
(CNIC, Name, DateOfTransaction, TransactionType, AssetType, AssetName, AssetCode, Quantity, UnitPrice, TaxPercentage, Remarks) 
VALUES 
('4210123456789', 'Ahmed Ali', '2024-11-01', 'Buy', 'Crypto', 'Bitcoin', 'BTC', 0.5, 35000.00, 5.00, 'First crypto purchase');

INSERT INTO TransactionLogs 
(CNIC, Name, DateOfTransaction, TransactionType, AssetType, AssetName, AssetCode, Quantity, UnitPrice, TaxPercentage, Remarks) 
VALUES 
('6110112345678', 'Fatima Khan', '2024-11-15', 'Sell', 'Fiat', 'US Dollar', 'USD', 1000.00, 1.00, 2.50, 'Selling USD for PKR');

INSERT INTO TransactionLogs 
(CNIC, Name, DateOfTransaction, TransactionType, AssetType, AssetName, AssetCode, Quantity, UnitPrice, TaxPercentage, Remarks) 
VALUES 
('3520212345678', 'Usman Tariq', '2024-11-20', 'Buy', 'Fiat', 'Apple Stock', 'AAPL', 10.00, 170.00, 7.00, 'Invested in Apple shares');

