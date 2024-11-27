package BLL_package;

import java.sql.*;
import java.time.*;
import java.util.*;

import admin_package.Admin;
import user_package.User;

import java.time.format.DateTimeFormatter;

public class FDCXFactory 
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FDCX";
    private static final String USER = "root";
    private static final String PASSWORD = "raja@168";
    
    private CurrencyManager currencyManager;
    private StockManager stockManager;
    private FDCX fdcx;
    private static FDCXFactory instance;
    
    private FDCXFactory() 
    {
        currencyManager = CurrencyManager.getInstance();
        stockManager = StockManager.getInstance();
        fdcx = FDCX.getInstance();
    }
    
    public static FDCXFactory getInstance()
    {
    	if(instance == null)
    		instance = new FDCXFactory();
    	
    	return instance;
    }
    

    // Initialize the system by loading all data
    public void initializeSystem() 
    {
        loadAdmins();
        loadUsers();
        loadSystemCurrencies();
        loadSystemStocks();
        loadSystemLogs();
    }

    // Load all Admins into the system
    private void loadAdmins() 
    {
        String adminQuery = "SELECT * FROM Admins";
        String accountQuery = "SELECT Username, Password FROM Accounts WHERE AdminID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
             ResultSet adminRs = adminStmt.executeQuery()) {

            while (adminRs.next()) 
            {
                String adminID = adminRs.getString("adminID");
                String name = adminRs.getString("Name");
                String email = adminRs.getString("Email");
                String phoneNumber = adminRs.getString("PhoneNumber");
                java.sql.Date dob = adminRs.getDate("DOB");
                java.sql.Date joinDate = adminRs.getDate("JoinDate");

                // Fetch corresponding account details
                String username = "";
                String password = "";
                try (PreparedStatement accountStmt = conn.prepareStatement(accountQuery)) 
                {
                    accountStmt.setString(1, adminID);
                    try (ResultSet accountRs = accountStmt.executeQuery()) {
                        if (accountRs.next()) {
                            username = accountRs.getString("Username");
                            password = accountRs.getString("Password");
                        }
                    }
                }

                Admin admin = new Admin(name ,email,adminID , phoneNumber ,dob.toLocalDate());
                admin.setJoinDate(joinDate.toString());
                admin.registerAccount(username, password);
                fdcx.loadAdmin(admin);
           	 

                System.out.println("Admin loaded: " + adminID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Load all Users and their associated details
    private void loadUsers() 
    {
        String query = "SELECT * FROM Users";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) 
            {
                String userID = rs.getString("UserID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phoneNumber = rs.getString("PhoneNumber");
                java.sql.Date dob = rs.getDate("DOB");
                java.sql.Date joinDate = rs.getDate("JoinDate");
                Boolean isVerified = rs.getBoolean("IsVerified");
                
                // create user
                User user = new User(name, email, userID, phoneNumber , dob.toLocalDate());
                user.setJoinDate(joinDate.toString());
                user.setVerified(isVerified);
                
                // load user account
                if(this.loadUserAccount(user))  // Load account details for the user
                {
                	// load user stocks
                    this.loadUserStocks(user);
                    
                    //load user wallet
                    this.loadUserWallet(user);
                    
                    //load user currencies
                    this.loadUserCurrencies(user);
                    
                    //load user subscription
                    this.loadUserSubscription(user);
                    
                    //load loyalty points
                    this.loadUserLoyaltyPoints(user);
                }
                
                 
                fdcx.loadUser(user);
                // Add user to system (you can store it in a list or process it further)
                System.out.println("User loaded: " + user.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Load the user's account details from the database
    private boolean loadUserAccount(User user) 
    {
        String accountQuery = "SELECT Username, Password FROM Accounts WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(accountQuery);) 
        {
        	 Stmt.setString(1, user.getCNIC());
        	 ResultSet rs = Stmt.executeQuery();
        			
        	
                // Fetch corresponding account details
                String username = "";
                String password = "";
                
                if (rs.next()) 
                {
                    username = rs.getString("Username");
                    password =rs.getString("Password");               
                    user.createAccount(username, password);
                    return true;
                }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();      
        
        }
        
        return false;
    }
    
    private void loadUserStocks(User user) 
    {
        String stockQuery = "SELECT Name, Quantity FROM UserStocks WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(stockQuery);) 
        {
        	 Stmt.setString(1, user.getCNIC());
        	 ResultSet rs = Stmt.executeQuery();
        			
        	
                // Fetch corresponding account details
                String name = "";
                int quantity = 0;
                
                while (rs.next()) 
                {
                    name = rs.getString("Name");
                    quantity =rs.getInt("Quantity");    
                    user.getAccount().addStock(new Stock(name,0.0,0), quantity); // 0 are just dummy values, they aren't required here  
                }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        
        
        }
    }

    private void loadUserWallet(User user) 
    {
        String walletQuery = "SELECT walletID FROM Wallets WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(walletQuery);) 
        {
        	 Stmt.setString(1, user.getCNIC());
        	 ResultSet rs = Stmt.executeQuery();
        			
        	
                // Fetch corresponding account details
                String walletID = "";
                
                if (rs.next()) 
                {
                    walletID = rs.getString("walletID");    
                    user.getAccount().getWallet().setWalletID(walletID);
                }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        
        
        }
    }
    
    private void loadUserCurrencies(User user) 
    {
        String currenciesQuery = "SELECT CurrencyCode, Amount FROM UserCurrencies WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(currenciesQuery);) 
        {
        	 Stmt.setString(1, user.getCNIC());
        	 ResultSet rs = Stmt.executeQuery();
        			
        	
                // Fetch corresponding account details
                String currencyCode = "";
                double amount = 0.0;
                
                while (rs.next()) 
                {
                	currencyCode = rs.getString("CurrencyCode");
                	amount =rs.getInt("Amount");    
                    user.getAccount().getWallet().addCurrency(currencyCode, amount);  
                }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
             
        }
    }
    
    private void loadUserSubscription(User user) 
    {
    	int subID = this.getSubscriptionID(user);
    	if( subID == -1)
    		return;
    	
        String subscriptionQuery = "SELECT Type, Price ,RenewalDate FROM  Subscriptions WHERE subscriptionID = ?" ;
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(subscriptionQuery);) 
        {
        	 Stmt.setInt(1,subID);
        	 ResultSet rs = Stmt.executeQuery();
        			
        	                
                if (rs.next()) 
                {
                	String type = rs.getString("Type");
                	double price = rs.getDouble("Price");
                	LocalDate renewalDate = rs.getDate("RenewalDate").toLocalDate(); 
                	user.getAccount().getSubscription().setPrice(price);
                	user.getAccount().getSubscription().setType(type);
                	user.getAccount().getSubscription().setRenewalDate(renewalDate);

                }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
             
        }
    }
    
    private int getSubscriptionID(User user) 
    {
        String subscriptionIDQuery = "SELECT SubscriptionID FROM Accounts WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(subscriptionIDQuery);) 
        {
        	 Stmt.setString(1, user.getCNIC());
        	 ResultSet rs = Stmt.executeQuery();
        			
        	
                
                if (rs.next()) 
                {
                	return rs.getInt("SubscriptionID");
                }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
             
        }
        
        return -1;
    }
    
    private void loadUserLoyaltyPoints(User user)
    {
    	String pointsQuery = "SELECT LoyaltyPoints FROM Accounts WHERE userID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(pointsQuery);) 
        {
        	 Stmt.setString(1, user.getCNIC());
        	 ResultSet rs = Stmt.executeQuery();    			
                     
            if (rs.next()) 
            {
            	int points = rs.getInt("LoyaltyPoints");
            	
            	user.getAccount().setLoyaltyPoints(points);
            }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
             
        }
    }
    
    private void loadSystemCurrencies()
    {
    	String currenciesQuery = "SELECT * FROM SystemCurrencies";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(currenciesQuery);) 
        {
        	 ResultSet rs = Stmt.executeQuery();    			
                     
            while (rs.next()) 
            {
            	String currencyName = rs.getString("CurrencyName");
            	String currencyCode = rs.getString("CurrencyCode");
            	double rateAgainstUSD = rs.getDouble("RateAgainstUSD");
            	String type = rs.getString("Type");
            	double amount = rs.getDouble("Amount");
            	
            	Currency currency = new Currency(currencyName,currencyCode,rateAgainstUSD,type,amount);
            	currencyManager.loadCurrency(currency);
            }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
             
        }
    		
    }
    
    private void loadSystemStocks()
    {
    	String stocksQuery = "SELECT * FROM SYSTEMSTOCKS";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(stocksQuery);) 
        {
        	 ResultSet rs = Stmt.executeQuery();    			
                     
            while (rs.next()) 
            {
            	String name = rs.getString("Name");
            	double unitPrice = rs.getDouble("UnitPrice");
            	int quantity = rs.getInt("Quantity");
            	boolean available = rs.getBoolean("Available");

            	
            	Stock stock = new Stock(name,unitPrice,quantity);
            	stock.setAvailable(available);
            	stockManager.loadStock(stock);
            }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
             
        }
    		
    }
    
    private void loadSystemLogs()
    {
    	String logsQuery = "SELECT * FROM TransactionLogs";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        		PreparedStatement Stmt = conn.prepareStatement(logsQuery);) 
        {
        	 ResultSet rs = Stmt.executeQuery();    			
                     
            while (rs.next()) 
            {

            	String userID = rs.getString("UserID");
            	String currencyCode = rs.getString("CurrencyCode");
            	double amount = rs.getDouble("Amount");
            	String type = rs.getString("Type");
            	Timestamp timestamp = rs.getTimestamp("TransactionDateTime");

                 LocalDateTime localDateTime = timestamp.toLocalDateTime();

                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                 String TransactionDateTime = localDateTime.format(formatter);
                 
                 User user = fdcx.getUser(userID);
                 TransactionLog log = new TransactionLog(user , currencyCode + " " + amount + " " + type);
                 log.setTransactionDateTime(TransactionDateTime);
            	
            	 fdcx.loadLog(log);
            }
         }
        
        catch (SQLException e) 
        {
            e.printStackTrace();
             
        }
    		
    }

}
