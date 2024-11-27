package BLL_package;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.time.*;

public class DBHandler // singleton 
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FDCX";
    private static final String USER = "root";
    private static final String PASSWORD = "raja@168";
    private static DBHandler instance = null;

    private DBHandler() 
    {
  
    }
    
    public List<Stock> getStockDB() throws SQLException 
    {
        String query = "SELECT Name, UnitPrice, Quantity FROM SystemStocks";
        List<Stock> stocks = new ArrayList<Stock>();
        
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query);

         ResultSet rs = stmt.executeQuery() ;
             
        	while(rs.next())
        	{
        		String name = rs.getString("Name");
    		    String price = rs.getString("UnitPrice");
    		    String quantity = rs.getString("Quantity");

    		    stocks.add(new Stock(name, Double.parseDouble(price), Integer.parseInt(quantity)));
        	}
        
        
        return stocks;
    }
    
    public List<Stock> getUserStockDB() throws SQLException
    {
    	String query = "SELECT Name, UnitPrice, Quantity FROM UserStocks";
        List<Stock> stocks = new ArrayList<Stock>();
        
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query);

         ResultSet rs = stmt.executeQuery() ;
             
        	while(rs.next())
        	{
        		String name = rs.getString("Name");
    		    String price = rs.getString("UnitPrice");
    		    String quantity = rs.getString("Quantity");

    		    stocks.add(new Stock(name, Double.parseDouble(price), Integer.parseInt(quantity)));
        	}
        
        
        return stocks;
    }
    
    public List<Currency> getCurrencyDB() throws SQLException 
    {
        String query = "SELECT CurrencyName, RateAgainstUSD, Amount, Type, CurrencyCode FROM SystemCurrencies";
        List<Currency> curr = new ArrayList<Currency>();
        
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query);

         ResultSet rs = stmt.executeQuery() ;
             
        	while(rs.next())
        	{
        		String name = rs.getString("CurrencyName");
    		    String code = rs.getString("CurrencyCode");
    		    String rate = rs.getString("RateAgainstUSD");
    		    String amount = rs.getString("Amount");
    		    String type = rs.getString("Type");

    		    curr.add(new Currency(name, code, Double.parseDouble(rate), type, Double.parseDouble(amount)));
        	}        
        
        return curr;
    }
    
    public List<Currency> getUserCurrencyDB() throws SQLException 
    {
        String query = "SELECT CurrencyName, RateAgainstUSD, Amount, Type, CurrencyCode FROM UserCurrencies";
        List<Currency> curr = new ArrayList<Currency>();
        
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query);

         ResultSet rs = stmt.executeQuery() ;
             
        	while(rs.next())
        	{
        		String name = rs.getString("CurrencyName");
    		    String code = rs.getString("CurrencyCode");
    		    String rate = rs.getString("RateAgainstUSD");
    		    String amount = rs.getString("Amount");
    		    String type = rs.getString("Type");

    		    curr.add(new Currency(name, code, Double.parseDouble(rate), type, Double.parseDouble(amount)));
        	}        
        
        return curr;
    }
       
    public void deleteAccount(String CNIC)
    {
    	String deleteUser = "DELETE FROM Accounts WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteUser)) {

            stmt.setString(1, CNIC);

            int affectedRows = stmt.executeUpdate();
       
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
        deleteUser = "DELETE FROM Wallets WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteUser)) {

            stmt.setString(1, CNIC);

            int affectedRows = stmt.executeUpdate();
       
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        deleteUser = "DELETE FROM Subscriptions WHERE SubscriptionID = (SELECT SubscriptionID FROM Accounts WHERE UserID = ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteUser)) {

            stmt.setString(1, CNIC);

            int affectedRows = stmt.executeUpdate();
       
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        deleteUser = "DELETE FROM UserStocks WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteUser)) {

            stmt.setString(1, CNIC);

            int affectedRows = stmt.executeUpdate();
       
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        deleteUser = "DELETE FROM UserCurrencies WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteUser)) {

            stmt.setString(1, CNIC);

            int affectedRows = stmt.executeUpdate();
       
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public static DBHandler getInstance()
    {
    	if(instance == null)
    	{
    		instance = new DBHandler();
    	}
    	return instance;
    }
    
    public boolean removeUser(String cnic)
    {
        String deleteUser = "DELETE FROM Users WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(deleteUser)) {

            stmt.setString(1, cnic);

            // Execute the delete query
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return true; // User successfully removed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Deletion failed
    }


    // 1. Register a new user (without account creation)
    public boolean registerUser(String name, String cnic, LocalDate dob, String email, String phoneNumber) 
    {
        String insertUser = "INSERT INTO Users (UserID, Name , DOB, Email, PhoneNumber, JoinDate, IsVerified) VALUES (?, ?, ?, ?, ?, CURDATE(), FALSE)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cnic);
            stmt.setString(2, name);
            stmt.setDate(3, java.sql.Date.valueOf(dob));  // Convert LocalDate to sql.Date
            stmt.setString(4, email);
            stmt.setString(5, phoneNumber);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return true; // User successfully registered
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void setUserVerification(String CNIC, boolean status) 
    {
    	  String updateVerification = "UPDATE Users SET IsVerified = ? WHERE UserID = ?";
    	  try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    	       PreparedStatement stmt = conn.prepareStatement(updateVerification)) 
    	  {
    	    stmt.setBoolean(1, status);
    	    stmt.setString(2, CNIC);
    	    stmt.executeUpdate();
    	  } catch (SQLException e) {
    	    e.printStackTrace();
    	  }
    }
    

    // 2. Register an admin (without account creation)
    public boolean registerAdmin(String name, String cnic, LocalDate dob, String email, String phoneNumber) 
    {
        String insertAdmin = "INSERT INTO Admins (adminID, Name, DOB, Email, PhoneNumber, JoinDate) VALUES (?, ?, ?, ?, ?, CURDATE())";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertAdmin, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cnic);
            stmt.setString(2, name);
            stmt.setDate(3, java.sql.Date.valueOf(dob));
            stmt.setString(4, email);
            stmt.setString(5, phoneNumber);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return true; // Admin successfully registered
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Create an account for a user
    public boolean createUserAccount(String userID, String username, String password, String walletID) 
    {
        String insertWallet = "INSERT INTO Wallets (WalletID, UserID) VALUES (?, ?)";
        String insertAccount = "INSERT INTO Accounts (Username, Password, Type, UserID, WalletID) VALUES (?, ?, 'user', ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Disable auto-commit to manage transactions manually
            conn.setAutoCommit(false);

            try (PreparedStatement walletStmt = conn.prepareStatement(insertWallet);
                 PreparedStatement accountStmt = conn.prepareStatement(insertAccount)) {

                // Step 1: Insert into Wallets table
                walletStmt.setString(1, walletID);
                walletStmt.setString(2, userID);
                int walletRows = walletStmt.executeUpdate();

                if (walletRows == 0) {
                    throw new SQLException("Failed to insert into Wallets table.");
                }

                // Step 2: Insert into Accounts table
                accountStmt.setString(1, username);
                accountStmt.setString(2, password);
                accountStmt.setString(3, userID);
                accountStmt.setString(4, walletID);
                int accountRows = accountStmt.executeUpdate();

                if (accountRows == 0) {
                    throw new SQLException("Failed to insert into Accounts table.");
                }

                // Commit transaction
                conn.commit();
                return true;
            } catch (SQLException e) {
                // Rollback transaction in case of failure
                conn.rollback();
                e.printStackTrace();
            } finally {
                // Restore auto-commit mode
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 4. Create an account for an admin
    public boolean createAdminAccount(String adminID, String username, String password) 
    {
        String insertAccount = "INSERT INTO Accounts (Username, Password, Type, adminID) VALUES (?, ?, 'admin', ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertAccount)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, adminID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 
    public List<Pair<String,Double>> getFiatExchangeRates() 
    {
    	  String query = "SELECT CurrencyCode, RateAgainstUSD FROM SystemCurrencies WHERE Available = TRUE AND Type = ?";
    	  List<Pair<String,Double>> exchangeRates = new ArrayList<>();
    	  
    	  try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    	       PreparedStatement stmt = conn.prepareStatement(query)) 
    	  {
    	    stmt.setString(1, "Fiat"); // Set the type filter to "fiat"
    	    ResultSet rs = stmt.executeQuery();

    	    while (rs.next()) 
    	    {
    	    	Pair<String,Double> pair = new Pair<String,Double>(rs.getString("CurrencyCode"), rs.getDouble("RateAgainstUSD"));
    	    	exchangeRates.add(pair);
    	    }
    	  } catch (SQLException e) {
    	    e.printStackTrace();
    	  }
    	  return exchangeRates;
    	}
    
    public List<Pair<String,Double>> getCryptoExchangeRates() 
    {
    	  String query = "SELECT CurrencyCode, RateAgainstUSD FROM SystemCurrencies WHERE Available = TRUE AND Type = ?";
    	  List<Pair<String,Double>> exchangeRates = new ArrayList<>();
    	  
	  	  try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
	  	       PreparedStatement stmt = conn.prepareStatement(query)) {
	  	    stmt.setString(1, "Crypto");
	  	    ResultSet rs = stmt.executeQuery();
	
	  	    while (rs.next()) 
	  	    {
	  	    	Pair<String,Double> pair = new Pair<String,Double>(rs.getString("CurrencyCode"), rs.getDouble("RateAgainstUSD"));
    	    	exchangeRates.add(pair);
	  	    }
	  	  } catch (SQLException e) {
	  	    e.printStackTrace();
	  	  }
	  	  return exchangeRates;
  	}

    
    public boolean isSufficientSystemBalance(String currencyCode, double amount)
    {
        String query = "SELECT Amount FROM SystemCurrencies WHERE CurrencyCode = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1,currencyCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double systemBalance = rs.getDouble("Amount");
                return systemBalance >= amount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateUserBalance(String userId,String currencyName , String currencyCode , double rateAgainstUSD, double amount, String type ,boolean isIncrease)
    {
        // Try updating the existing record first
        String updateQuery = "UPDATE UserCurrencies SET Amount = Amount " + (isIncrease ? "+" : "-") + " ? WHERE UserID = ? AND CurrencyCode = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, userId);
            stmt.setString(3, currencyCode);

            int rowsAffected = stmt.executeUpdate();

            // If no rows were affected, insert the new balance entry
            if (rowsAffected == 0) {
                String insertQuery = "INSERT INTO UserCurrencies(UserID, CurrencyName, CurrencyCode,RateAgainstUSD,Amount, type) VALUES (?,?,?,?,?,?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, userId);
                    insertStmt.setString(2, currencyName);
                    insertStmt.setString(3, currencyCode);
                    insertStmt.setDouble(4, rateAgainstUSD);
                    insertStmt.setDouble(5,amount);
                    insertStmt.setString(6,type);
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateSystemBalance(String currencyName , String currencyCode , double rateAgainstUSD, double amount, String type ,boolean isIncrease) 
    {
        // Try updating the existing record first
        String updateQuery = "UPDATE SystemCurrencies SET Amount = Amount " + (isIncrease ? "+" : "-") + " ? WHERE CurrencyCode = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, currencyCode);

            int rowsAffected = stmt.executeUpdate();

            // If no rows were affected, insert the new system balance entry
            if (rowsAffected == 0) 
            {
                String insertQuery = "INSERT INTO SystemCurrencies (CurrencyName , CurrencyCode , RateAgainstUSD, Amount, Type) VALUES (?,?,?,?,?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, currencyName);
                    insertStmt.setString(2, currencyCode);
                    insertStmt.setDouble(3, rateAgainstUSD);
                    insertStmt.setDouble(4, amount);
                    insertStmt.setString(5, type);
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void recordTransaction(String userId, String currencyCode, double amount, String type) 
    {
        String insertQuery = "INSERT INTO TransactionLogs (UserID, CurrencyCode, Amount, Type, TransactionDateTime) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, userId);
            stmt.setString(2, currencyCode);
            stmt.setDouble(3, amount);
            stmt.setString(4, type);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public List<String> getTransactionHistory(String userId) 
    {
        List<String> transactionHistory = new ArrayList<>();
        String query = "SELECT CurrencyCode, Amount, Type, TransactionDateTime FROM TransactionLogs WHERE UserID = ? ORDER BY TransactionDateTime DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the userId parameter in the query
            stmt.setString(1, userId);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) 
            {
                // Process the results and populate the transaction history list
                while (rs.next()) {
                    String currencyCode = rs.getString("CurrencyCode");
                    double amount = rs.getDouble("Amount");
                    String type = rs.getString("Type");
                    Timestamp transactionDateTime = rs.getTimestamp("TransactionDateTime");

                    // Format the transaction as a String and add it to the list
                    String transaction = String.format("Currency: %s | Amount: %.2f | Type: %s | Date: %s", 
                                                        currencyCode, amount, type, transactionDateTime);
                    transactionHistory.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistory;
    }

    public boolean buyCrypto(String userId , String cryptoName , String cryptoCode , double rateAgainstUSD, double amount) 
    {
        if (isSufficientSystemBalance(cryptoCode, amount)) {
            updateUserBalance(userId, cryptoName, cryptoCode, rateAgainstUSD , amount, "Crypto", true);
            updateSystemBalance(cryptoName, cryptoCode, rateAgainstUSD , amount, "Crypto", false);
            recordTransaction(userId, cryptoCode, amount, "Buy Crypto");
            return true;
        } else {
            return false; // Or throw an exception
        }
    }

    public boolean sellCrypto(String userId , String cryptoName , String cryptoCode , double rateAgainstUSD, double amount) 
    {
        // Check user's balance
        if (isUserBalanceSufficient(userId, cryptoCode, amount)) 
        {
            if (isSufficientSystemBalance(cryptoCode, amount)) {
            	updateUserBalance(userId, cryptoName, cryptoCode, rateAgainstUSD , amount, "Crypto", false);
                updateSystemBalance(cryptoName, cryptoCode, rateAgainstUSD , amount, "Crypto", true);
                recordTransaction(userId, cryptoCode, amount, "Sell Crypto");
                return true;
            } else {
                return false; // Or throw an exception
            }
        } else {
            return false; // Or throw an exception
        }
    }

    public boolean buyFiat(String userId , String currencyName , String currencyCode , double rateAgainstUSD, double amount) 
    {
    	if (isSufficientSystemBalance(currencyCode, amount)) {
    		updateUserBalance(userId, currencyName, currencyCode, rateAgainstUSD , amount, "Fiat", true);
            updateSystemBalance(currencyName, currencyCode, rateAgainstUSD , amount, "Fiat", false);
            recordTransaction(userId, currencyCode, amount, "Buy Fiat");
            return true;
        } else {
            return false; // Or throw an exception
        }
    }

    public boolean sellFiat(String userId , String currencyName , String currencyCode , double rateAgainstUSD, double amount) 
    {
        if (isUserBalanceSufficient(userId, currencyCode, amount)) 
        {
        	updateUserBalance(userId, currencyName, currencyCode, rateAgainstUSD , amount, "Fiat", false);
            updateSystemBalance(currencyName, currencyCode, rateAgainstUSD , amount, "Fiat", true);
            recordTransaction(userId, currencyCode, amount, "Sell Fiat");
            return true;
        } else 
        {
            return false; // Or throw an exception
        }
    }


    public boolean isUserBalanceSufficient(String userId, String currencyCode, double amount) 
    {
        String query = "SELECT Amount FROM UserCurrencies WHERE UserID = ? AND CurrencyCode = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1,userId);
            stmt.setString(2, currencyCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double userBalance = rs.getDouble("Amount");
                return userBalance >= amount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 1. Add stock to the system
    public void addStockToSystem(String stockName, double unitPrice, int quantity) 
    {
        if (isStockExists(stockName)) 
        {
            // Update the quantity of the existing stock
            String updateQuery = "UPDATE SystemStocks SET Quantity = Quantity + ? WHERE Name = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, quantity);
                stmt.setString(2, stockName);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Insert a new stock
            String insertQuery = "INSERT INTO SystemStocks (Name, UnitPrice, Quantity) VALUES (?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, stockName);
                stmt.setDouble(2, unitPrice);
                stmt.setInt(3, quantity);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 2. Remove stock from the system
    public boolean removeStockFromSystem(String stockName, int quantity) 
    {
        // Check if sufficient stock exists in the system
        if (!isSufficientSystemStock(stockName, quantity)) {
            return false; // Not enough stock to remove
        }

        String updateQuery = "UPDATE SystemStocks SET Quantity = Quantity - ? WHERE Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, stockName);
            stmt.executeUpdate();
            return true; // Successfully removed stock
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurred
    }

    // 3. Add stock to a user
    public boolean addUserStock(String userId, String stockName, int quantity , double unitPrice) 
    {
        // Check if sufficient stock exists in the system
        if (!isSufficientSystemStock(stockName, quantity)) {
            return false; // Not enough stock in the system
        }

        String insertQuery = "UPDATE UserStocks SET Quantity = Quantity + ? WHERE Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
        	stmt.setInt(1, quantity);
            stmt.setString(2, stockName);
            
            int rowsaffected = stmt.executeUpdate();
            
            if (rowsaffected == 0) {
                String insertQuery_ = "INSERT INTO UserStocks (UserID, Name, UnitPrice ,Quantity) VALUES (?,?,?,?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery_)) {
                	insertStmt.setString(1, userId); // UserID as String
                	insertStmt.setString(2, stockName);
                	insertStmt.setDouble(3, unitPrice);
                	insertStmt.setInt(4, quantity);
                    insertStmt.executeUpdate();
                }
            }

            return true; // Successfully added stock to the user
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurred
    }

    // 4. Remove stock from a user
    public boolean removeUserStock(String userId, String stockName, int quantity) 
    {
        // Check if the user has sufficient stock
        if (!isSufficientUserStock(userId, stockName, quantity)) {
            return false; // Not enough stock in the user's account
        }

        String updateQuery = "UPDATE UserStocks SET Quantity = Quantity - ? " +
                             "WHERE UserID = ? AND Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, userId);
            stmt.setString(3, stockName);
            stmt.executeUpdate();

            return true; // Successfully removed user stock
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurred
    }

    // Helper Method: Check if a stock exists in the system
    private boolean isStockExists(String stockName) 
    {
        String query = "SELECT COUNT(*) AS StockCount FROM SystemStocks WHERE Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, stockName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("StockCount") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if stock is not found or an error occurred
    }

    // Helper Method: Check if sufficient system stock is available
    private boolean isSufficientSystemStock(String stockName, int quantity) 
    {
        String query = "SELECT Quantity FROM SystemStocks WHERE Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, stockName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int availableQuantity = rs.getInt("Quantity");
                return availableQuantity >= quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if stock is not found or an error occurred
    }

    // Helper Method: Check if the user has sufficient stock
    private boolean isSufficientUserStock(String userId, String stockName, int quantity) 
    {
        String query = "SELECT Quantity FROM UserStocks WHERE UserID = ? AND  Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, stockName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int availableQuantity = rs.getInt("Quantity");
                return availableQuantity >= quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if stock is not found or an error occurred
    }

    // Loyalty Points
    
    public void updateUserLoyaltyPoints(String userId, int amount ,boolean isIncrease)
    {

        String updateQuery = "UPDATE Accounts SET LoyaltyPoints = LoyaltyPoints " + (isIncrease ? "+" : "-") + " ? WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, userId);
            stmt.executeUpdate();

        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    
    
    // Subscription management methods
    
    public boolean userSubscription(String userId, String type, double price, LocalDate renewalDate) 
    {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) 
        {
            // Step 1: Add a new subscription entry in the Subscriptions table
            int subscriptionId = addSubscription(type, price, renewalDate);

            if (subscriptionId == -1) {
                System.out.println("Failed to add subscription to the Subscriptions table.");
                return false;
            }

            // Step 2: Update the user's account with the SubscriptionID
            String updateAccountQuery = "UPDATE Accounts SET SubscriptionID = ? WHERE UserID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateAccountQuery)) {
                stmt.setInt(1, subscriptionId);
                stmt.setString(2, userId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Subscription successfully updated for user: " + userId);
                    return true;
                } else {
                    System.out.println("Failed to update the user's account with SubscriptionID.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int addSubscription(String type, double price, LocalDate renewalDate) 
    {
        String query = "INSERT INTO Subscriptions (Type, Price, RenewalDate) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, type);
            stmt.setDouble(2, price);
            stmt.setDate(3, java.sql.Date.valueOf(renewalDate)); // Convert LocalDate to SQL Date

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated SubscriptionID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failed to add subscription
    }
    
    public int getSubscriptionID(String type, LocalDate renewalDate) 
    {
        String query = "SELECT SubscriptionID FROM Subscriptions WHERE Type = ? AND RenewalDate = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, type);
            stmt.setDate(2,  java.sql.Date.valueOf(renewalDate)); // Convert LocalDate to SQL Date

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SubscriptionID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if subscription not found
    }

    public boolean updateAccountSubscription(String userId, int subscriptionId) 
    {
        String query = "UPDATE Accounts SET SubscriptionID = ? WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subscriptionId);
            stmt.setString(2, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if the update failed
    }
    
    public void cancelSubscription(String userID)
    {
  	  
    	int subscriptionID = this.getSubscriptionID(userID);
    	
      String query = "UPDATE Accounts SET SubscriptionID = NULL WHERE UserID = ?";
      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
           PreparedStatement stmt = conn.prepareStatement(query)) {
          stmt.setString(1, userID);

          stmt.executeUpdate();

      } catch (SQLException e) {
          e.printStackTrace();
      }
      
    	this.deleteSubscription(subscriptionID);
    }
    
    private int getSubscriptionID(String userID) 
    {
        String query = "SELECT SubscriptionID FROM Accounts WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userID);
           
            try (ResultSet rs = stmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return rs.getInt("SubscriptionID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if subscription not found
    }
    
    private void deleteSubscription(int subscriptionID) 
    {
  	    String deleteUser = "DELETE FROM SUBSCRIPTIONS WHERE subscriptionID = ?";
  	    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
  	         PreparedStatement stmt = conn.prepareStatement(deleteUser)) 
  	    {

  	        stmt.setInt(1, subscriptionID); // Set the user ID parameter

  	        stmt.executeUpdate(); // Execute the deletion

  	    } catch (SQLException e) {
  	        e.printStackTrace(); // Print stack trace for debugging
  	    }
  	}

      public void updateRenewalDate(String userID , LocalDate renewalDate)
      {
      	int subscriptionID = this.getSubscriptionID(userID);
      	
      	String query = "UPDATE Subscriptions SET RenewalDate = ? WHERE SubscriptionID = ?";
          try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
               PreparedStatement stmt = conn.prepareStatement(query)) 
          {
          	
          	stmt.setDate(1, java.sql.Date.valueOf(renewalDate));
          	stmt.setInt(2, subscriptionID);

              stmt.executeUpdate();

          } catch (SQLException e) {
              e.printStackTrace();
          }
      }     
}
