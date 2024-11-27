package BLL_package;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import admin_package.Admin;
import secondaryActors_package.FBR;
import secondaryActors_package.NADRA;
import user_package.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;


public class FDCX 
{
    private List<User> users;
    private List<Admin> admins;
    private List<TransactionLog> transactionLogs;
    private DBHandler dbHandler;
    private StockManager stockManager;
    private BankingService bankingService;
    private CryptoService cryptoService;
    private NADRA nadra; 
    private static FDCX instance;

    private FDCX() 
    {
        users = new ArrayList<>();
        admins = new ArrayList<>();
        transactionLogs = new ArrayList<>();
        stockManager = StockManager.getInstance();
        nadra = new NADRA();
        dbHandler = DBHandler.getInstance();
        bankingService = new BankingService();
        cryptoService = new CryptoService();
    }
    
    public String getCNICfromUsername(String username)
    {
    	for(int i = 0; i < users.size(); i++)
    	{
    		if(users.get(i).getAccount().getUsername().equals(username))
    		{
    			return users.get(i).getCNIC();
    		}
    	}
    	
    	return null;
    }
    
    public void removeAccount(String username)
    {
    	for(int i = 0; i < users.size(); i++)
    	{
    		if(users.get(i).getAccount().getUsername().equals(username))
    		{
    			users.get(i).deleteAccount();
    		}
    	}
    }
    
    public static FDCX getInstance()
    {
    	if(instance == null)
    	{
    		instance = new FDCX();
    	}
    	
    	return instance;
    }
    
    public void removeUser(String cnic)
    {
    	for(int i = 0; i < users.size(); i++)
    	{
    		if(users.get(i).getCNIC().equals(cnic))
    		{
    			users.remove(i);
    		}
    	}
    		
    	dbHandler.removeUser(cnic);
    }

    // USE CASE : REGISTER USER
    public boolean registerUser(String name,String email,String CNIC,String phoneNumber , LocalDate DOB) 
    {
        User user = new User(name,email,CNIC ,phoneNumber,DOB);
        users.add(user);
        boolean status = dbHandler.registerUser(name, CNIC, DOB, email, phoneNumber);
        return status;
    }

    public void addAdmin(String name, String email, String CNIC ,  String phoneNumber ,  LocalDate DOB , String username , String password) 
    {
        Admin admin = new Admin(name, email, CNIC , phoneNumber ,DOB);
        admins.add(admin);
        
        dbHandler.registerAdmin(name, CNIC, DOB, email, phoneNumber);
        this.createAdminAccount(admin, username,password);
        System.out.println("Admin added successfully: " + admin);
    }
     
    // returns - > 0 for successfull login , 1 for username doesn't exist , 2 for password incorrect
    public int userLogin(String username , String password) 
    {
    	Account account = null;
    	String retrivedPassword = "";
    	for(User u : users)
    	{
    		if(u.getAccount() != null && u.getAccount().getUsername().equals(username))
    		{
    			account = u.getAccount() ;
    			retrivedPassword = account.getPassword();
    		}
    	}
    	
    	if(account == null)
    	{
    		return 1;
    	}
    	
    	if(!password.equals(retrivedPassword))
    	{
    		return 2;
    	}
    	
    	return 0;
    }
    
    // returns - > 0 for successfull login , 1 for username doesn't exist , 2 for password incorrect
    public int adminLogin(String username , String password) 
    {
    	Account account = null;
    	String retrivedPassword = "";
    	for(Admin a : admins)
    	{
    		if(a.getAccount() != null && a.getAccount().getUsername().equals(username))
    		{
    			account = a.getAccount() ;
    			retrivedPassword = account.getPassword();
    		}
    	}
    	
    	if(account == null)
    	{
    		return 1;
    	}
    	
    	if(!password.equals(retrivedPassword))
    	{
    		return 2;
    	}
    	
    	return 0;
    }
    
    
    public void createUserAccount(User user , String username, String password)
    {
    	if(!user.isVerified())
    	{
    		System.out.println("USER NOT VERIFIED. VERIFY USER FIRST!");
    		return;
    	}
    	
    	user.createAccount(username, password);
    	dbHandler.createUserAccount(user.getCNIC(), username, password,user.getAccount().getWallet().getWalletID());   	
    }
    
    private void createAdminAccount(Admin admin , String username, String password)
    {
    	dbHandler.createAdminAccount(admin.getCNIC(), username, password);
    	admin.registerAccount(username, password);
    }
    
    

    public boolean verifyUser(User user) 
    {
        if(nadra.verifyUser(user.getCNIC(), user.getName()))
        {
        	user.setVerified(true);
        	return true;
        }   		
        else
        {
        	return false;
        }
    }
    
    // USE CASE : VIEW EXCHNAGE RATE 
    public List<Pair<String,Double>> viewFiatExchangeRates()
    {
    	return bankingService.getFiatExchangeRates();
    }
    
 // USE CASE : VIEW EXCHANGE RATE 
    public List<Pair<String,Double>> viewCryptoExchangeRates()
    {
    	return cryptoService.getCryptoExchangeRates();
    }
 
    public boolean withdrawFunds(String type , User user , String currencyCode , double amount) // type fiat or crypto
    {
    	
    	if("Fiat".equals(type))
    	{
    		bankingService.sellFiat(user, currencyCode, amount);
    		this.logTransaction(user, currencyCode, amount, type + " sold");
    		return true;
    	}
    	else if ("Crypto".equals(type))
    	{
    		cryptoService.sellCrypto(user, currencyCode, amount);
    		this.logTransaction(user, currencyCode, amount, type + " sold");
    		return true;
    	}
    	return false;
    	
    }
    
    public boolean depositFunds(String type , User user , String currencyCode , double amount) // type fiat or crypto
    {
    	if("Fiat".equals(type))
    	{
    		bankingService.buyFiat(user, currencyCode, amount);
    		this.logTransaction(user, currencyCode, amount, type + " bought");
    		user.getAccount().setLoyaltyPoints(user.getAccount().getLoyaltyPoints() + 10);
    		dbHandler.updateUserLoyaltyPoints(user.getCNIC(), 10, true);
    		return true;
    	}
    	else if ("Crypto".equals(type))
    	{
    		cryptoService.buyCrypto(user, currencyCode, amount);
    		this.logTransaction(user, currencyCode, amount, type + " bought");
    		user.getAccount().setLoyaltyPoints(user.getAccount().getLoyaltyPoints() + 10);
    		dbHandler.updateUserLoyaltyPoints(user.getCNIC(), 10, true);
    		return true;
    	}
    	
    	return false;
    	
    }
    
    public boolean tradeFunds(String type , User fromUser , User toUser, String currencyCode , double amount) // type fiat or crypto
    {
    	if("Fiat".equals(type))
    	{
    		bankingService.transferFiat(fromUser, toUser, currencyCode, amount);
    		this.logTransaction(fromUser, currencyCode, amount, type + " trade out");
    		this.logTransaction(toUser, currencyCode, amount, type + " trade in");
    		return true;
    	}
    	else if ("Crypto".equals(type))
    	{
    		cryptoService.transferCrypto(fromUser, toUser, currencyCode, amount);
    		this.logTransaction(fromUser, currencyCode, amount, type + " trade out");
    		this.logTransaction(toUser, currencyCode, amount, type + " trade in");
    		return true;
    	}
    	return false;
    	
    }
    
    public void exchangeType(String fromCurrencyCode , String toCurrencyCode , User user , double amount, String type)
    {
    	if(type.equals("Fiat"))
    		exchangeFiat(fromCurrencyCode, toCurrencyCode, user, amount);
    	else if(type.equals("Crypto"))
    		exchangeCrypto(fromCurrencyCode, toCurrencyCode, user, amount);
    }
    
    public void exchangeFiat(String fromCurrencyCode , String toCurrencyCode , User user , double amount) // exchange is free of cost too
    {
    	bankingService.exchangeFiat(fromCurrencyCode, toCurrencyCode, user, amount);
    }
    
    public void exchangeCrypto(String fromCryptoCode , String toCryptoCode , User user , double amount) // exchange is free of cost too
    {
    	cryptoService.exchangeCrypto(fromCryptoCode, toCryptoCode, user, amount);
    }
    
    public void assignStockToUser(User user , Stock stock , int quantity)
    {
    	stockManager.addStockToUser(user, stock, quantity);
    	user.getAccount().setLoyaltyPoints(user.getAccount().getLoyaltyPoints() + 10);
    	dbHandler.updateUserLoyaltyPoints(user.getCNIC(), 10, true);
    	this.logTransaction(user, stock.getName(), quantity, " stock bought");
    }
    
    public void removeStockFromUser(User user , Stock stock , int quantity)
    {
    	stockManager.removeStockFromUser(user, stock, quantity);
    	this.logTransaction(user, stock.getName(), quantity, " stock sold");
    }

    public boolean claimLoyaltyPoints(User user)
    {
    	int points = user.getAccount().getLoyaltyPoints();
    	
    	if(points == 0)
    	{
    		System.out.println("User has no loyalty points!");
    		return false;
    	}
    	
    	
    	user.getAccount().setLoyaltyPoints(0);
    	dbHandler.updateUserLoyaltyPoints(user.getCNIC(), points, false);
    	
    	if(!user.getAccount().getSubscription().getType().equals("cancelled"))
    	{
    		points += 1000;
    	}
    
    	dbHandler.updateUserBalance(user.getCNIC(), "Dollar" ,"USD", 1.0 ,points/10 , "Fiat" , true);
    	dbHandler.updateSystemBalance( "Dollar" ,"USD", 1.0 ,points/10 , "Fiat" , false);
    	
    	return true;
 
    }
    
    public void addCurrencyToSystem(String currencyName, String currencyCode, double rateAgainstUSD, String type, double amount)
    {
    	this.admins.getFirst().addCurrencyToSystem(currencyName, currencyCode, rateAgainstUSD, type, amount);
    }
    
    public void removeCurrencyFromSystem(String currencyCode, double amount)
    {
    	this.admins.getFirst().removeCurrencyFromSystem(currencyCode, amount);
    }
    
    public void addStockToSystem(String stockName , double unitPrice , int quantity)
    {
    	this.admins.getFirst().addStockToSystem(stockName, unitPrice, quantity);
    }
    
    public void removeStockFromSystem(String stockName , int quantity)
    {
    	this.admins.getFirst().removeStockFromSystem(stockName, quantity);
    }
    
    
    public void logTransaction( User user , String currencyCode , double amount ,String type)
    {
    	TransactionLog log = new TransactionLog(user , currencyCode + " " + amount + " " + type);
    	transactionLogs.add(log);
    	dbHandler.recordTransaction(user.getCNIC(), currencyCode, amount, type);
    }
    
    public List<String> getTransactionHistory(String userId)
    {
    	return dbHandler.getTransactionHistory(userId);
    }
    
    public Pair<List<Pair<String,Integer>>,List<Pair<String,Double>>> getWallet(String userId)
    {
    	if(!isUser(userId))
    	{
    		System.out.println("User doesn't exist in the system!");
    		return null;
    	}
    	
    	User user = getUser(userId);
    	
    	List<Stock> stockList = stockManager.getStocks();
    	List<Currency> currencyList = CurrencyManager.getInstance().getCurrencyList();
    	
    	return new Pair<List<Pair<String,Integer>>,List<Pair<String,Double>>> (user.getAccount().getOwnedStockList(stockList),user.getAccount().getWallet().getOwnedCurrenciesList(currencyList));
    }
    
    public List<Stock> getUserStockDB() throws SQLException
    {
    	return dbHandler.getUserStockDB();
    }
    
    public List<Currency> getUserCurrencyDB() throws SQLException
    {
    	return dbHandler.getUserCurrencyDB();
    }
    
    public List<Stock> getStockDB() throws SQLException
    {
    	return dbHandler.getStockDB();
    }
    
    public List<Currency> getCurrencyDB() throws SQLException
    {
    	return dbHandler.getCurrencyDB();
    }
    
    public boolean predictStockTrend(String stockName)
    {
    	return stockManager.predictStockTrend(stockName);
    }
    
    public List<prediction> getCurrencyPrediction()
    {
    	CurrencyManager manager = CurrencyManager.getInstance();
    	return manager.predictCurrency();
    }
    
    public List<prediction> getStockPrediction()
    {
    	return stockManager.predictStock();
    }
    
    public void subscribe (User user , String type)
    {
    	double USDBalance = user.getAccount().getWallet().getCurrencyBalance("USD");
    	
    	if(user.getAccount().getSubscription().subscribe(type, user.getCNIC(), USDBalance))
    	{
    		user.getAccount().getWallet().removeCurrency("USD", user.getAccount().getSubscription().getPrice());
    		CurrencyManager.getInstance().addCurrency("Dollar", "USD", 1.0 ,"Fiat", user.getAccount().getSubscription().getPrice());
    		this.logTransaction(user, "USD",user.getAccount().getSubscription().getPrice() , "Subscription");
    		CurrencyManager.getInstance().recordTransaction(user.getCNIC(), user.getName(), "Sell", "Fiat", "Dollar", "USD", user.getAccount().getSubscription().getPrice(), 1.0,"Subscription Bought"); 
    	}
    	
    	else
    	{
    		System.out.println("SUBSCRIPTION FAILED!");
    	}
    	
    }
    
    public void renewSubscription(User user) 
    {
    	user.getAccount().getSubscription().renewSubscription(user.getCNIC());
        user.getAccount().getWallet().removeCurrency("USD", user.getAccount().getSubscription().getPrice());
        
 	   CurrencyManager.getInstance().addCurrency("Dollar", "USD", 1.0 ,"Fiat", user.getAccount().getSubscription().getPrice());
 	   this.logTransaction(user, "USD",user.getAccount().getSubscription().getPrice() , "Subscription");
 	   CurrencyManager.getInstance().recordTransaction(user.getCNIC(), user.getName(), "Sell", "Fiat", "Dollar", "USD", user.getAccount().getSubscription().getPrice(), 1.0,"Subscription renewal");
     }

    public void cancelSubscription(User user) 
    {
    	user.getAccount().getSubscription().cancelSubscription(user.getCNIC());
    }

    public void changeSubscription(User user,String newType) 
    {
       user.getAccount().getSubscription().changeSubscription(newType);
       this.cancelSubscription(user);
       this.subscribe(user, newType);
    }
    
    private boolean isUser(String userId)
    {
    	for(User user : users)
    	{
    		if(user.getCNIC().equals(userId))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public User getUser(String userId)
    {
    	for(User user : users)
    	{
    		if(user.getCNIC().equals(userId))
    		{
    			return user;
    		}
    	}
    	
    	return null;
    }
    
    public List<TransactionLog> getTransactionLogs()
    {
    	return transactionLogs;
    }
    
    public Admin getAdmin(String adminId)
    {
    	for(Admin admin : admins)
    	{
    		if(admin.getCNIC().equals(adminId))
    		{
    			return admin;
    		}
    	}
    	
    	return null;
    }
    
    public static List<TransactionLog> getTaxReport(String CNIC) throws Exception
    {
    	return FBR.generateTaxReport(CNIC);
    }
   
    // Loading from Db 
    
    public void loadUser(User user)
    {
    	users.add(user);
    }
    
    public void loadAdmin(Admin admin)
    {
    	admins.add(admin);
    }
    
    public void loadLog(TransactionLog log)
    {
    	transactionLogs.add(log);
    }
    
    // List all users
    public void listUsers() 
    {
        System.out.println("Registered Users:");
        for (User user : users) 
        {
            System.out.println(user);
        }
    }
    
    public void listAdmins() 
    {
        System.out.println("Admins:");
        for (Admin admin : admins) 
        {
            System.out.println(admin);
        }
    }
}
