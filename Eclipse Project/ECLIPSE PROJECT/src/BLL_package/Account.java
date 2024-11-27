package BLL_package;
import java.util.*;

public class Account 
{
	private String type; // "admin" , "user"
	private Map<String, Integer> stockBalances;
    private Wallet wallet;
    private Subscription subscription;
    private int loyaltyPoints;
    private String username;
    private String password;
    
    
    public Account(String username , String password , String type)
    {
    	 if(type == "user")
    	 {
    		 this.wallet = new Wallet();
        	 this.subscription = new Subscription();
        	 this.stockBalances = new HashMap<>();
        	 this.loyaltyPoints = 0;
    	 }
    	 
    	 else
    	 {
    		 this.wallet = null;
        	 this.subscription = null;
        	 this.stockBalances = null;
        	 this.loyaltyPoints = -1;
    	 }
    	 
    	 this.type = type;
    	 this.username = username;
    	 this.password = password;
    }
    
    
    public void addStock(Stock stock , int quantity)
    {
    	if (quantity < 0) 
        {
            System.out.println("Quantity must be greater than zero!");
            return;
        }
    	
    	stockBalances.put(stock.getName(), stockBalances.getOrDefault(stock.getName(), 0) + quantity);
    }
    
    public boolean removeStock(Stock stock , int quantity)
    {
    	if (quantity < 0) 
        {
            System.out.println("Quantity must be greater than zero!");
            return false;
        }

        if (!stockBalances.containsKey(stock.getName())) {
            System.out.println("Stock not found in account: " + stock.getName());
            return false;
        }

        int currentBalance = stockBalances.get(stock.getName());
        if (currentBalance >= quantity) 
        {
        	stockBalances.put(stock.getName(), currentBalance - quantity);
            System.out.println(quantity + " units of " + stock.getName() + " removed from account.");
            return true;
        } else {
            System.out.println("Insufficient balance of " + stock.getName() + " in account.");
            return false;
        }
    }
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Wallet getWallet() 
    {
        return wallet;
    }

    public void claimLoyaltyPoints(int points) 
    {
        loyaltyPoints += points;
    }
    
    public int getLoyaltyPoints() 
    {
        return loyaltyPoints;
    }
	
	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) 
	{
		this.subscription = subscription;
	}

	public void setWallet(Wallet wallet) 
	{
		this.wallet = wallet;
	}

	public void setLoyaltyPoints(int loyaltyPoints) 
	{
		this.loyaltyPoints = loyaltyPoints;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public List<Pair<String,Integer>> getOwnedStockList(List<Stock> systemStocks)
	{
		List<Pair<String,Integer>> stockList = new ArrayList<>();
		
		for(Stock s : systemStocks)
		{
			if(stockBalances.containsKey(s.getName()))
			{
				stockList.add(new Pair<String,Integer>(s.getName(),stockBalances.get(s.getName())));
			}
		}
		
		
		return stockList;
	}

	public Map<String, Integer> getStockBalances() {
		return stockBalances;
	}

	public void setStockBalances(Map<String, Integer> stockBalances) {
		this.stockBalances = stockBalances;
	}
	
}
