package main_package;

import java.util.*;

public class CurrencyManager // singleton
{
	private static CurrencyManager instance = null;
    private List<Currency> currencyList; // List of currencies in the system
    private DBHandler dbHandler;
    private FBR fbr;

    // Constructor
    private CurrencyManager() 
    {
        this.currencyList = new ArrayList<>();
        dbHandler = DBHandler.getInstance();
        fbr = new FBR();
    }
    
    public static CurrencyManager getInstance()
    {
    	if(instance == null)
    		instance = new CurrencyManager();
    	
    	return instance;
    }

    public void addCurrency(String currencyName, String currencyCode, double rateAgainstUSD, String type, double amount) 
    {
    	dbHandler.updateSystemBalance(currencyName, currencyCode, rateAgainstUSD, amount, type, true);
    	
        for (Currency c : currencyList) 
        {
            if (c.getCurrencyCode().equals(currencyCode)) 
            {
                // Currency exists, update its amount
                c.setAmount(c.getAmount() + amount);
                System.out.println("Currency amount updated successfully: " + currencyCode + ", New Amount: " + c.getAmount());
                return;
            }
        }

        // Add the currency as it doesn't exist
        Currency newCurrency = new Currency(currencyName, currencyCode, rateAgainstUSD, type, amount);
        currencyList.add(newCurrency);
        System.out.println("Currency added successfully: " + currencyCode + ", Amount: " + amount);
    }


    public void removeCurrency(String currencyCode, double amount) 
    {
        for (Currency c : currencyList) 
        {
            if (c.getCurrencyCode().equals(currencyCode)) 
            {
                // Check if there is enough amount to remove
                if (c.getAmount() < amount) 
                {
                    System.out.println("Insufficient amount for currency: " + currencyCode);
                    return;
                }

                // Deduct the amount
                c.setAmount(c.getAmount() - amount);
                dbHandler.updateSystemBalance(c.getCurrencyName(), currencyCode, c.getRateAgainstUSD(), amount, c.getType(), false);
               
                System.out.println("Currency amount updated successfully: " + currencyCode + ", Remaining Amount: " + c.getAmount());
                
                return;
            }
        }
        System.out.println("Currency not found: " + currencyCode);
    }


    // Add currency to a user's wallet
    public void addCurrencyToWallet(Wallet wallet, String currencyCode, double amount) 
    {
        // Check if the currency exists in the system
        Currency systemCurrency = findCurrency(currencyCode);
        if (systemCurrency == null) {
            System.out.println("Currency not found in the system: " + currencyCode);
            return;
        }

        // Check if the system has enough of that currency
        if (systemCurrency.getAmount() >= amount) 
        {
            wallet.addCurrency(currencyCode, amount); // Add to wallet
            systemCurrency.setAmount(systemCurrency.getAmount() - amount); // Decrease from system
            System.out.println(amount + " units of " + currencyCode + " added to wallet.");
        } else {
            System.out.println("Insufficient amount of " + currencyCode + " in the system.");
        }
    }
    
    public Currency getCurrency(String currencyCode)
    {
    	for(Currency c : currencyList)
    	{
    		if(c.getCurrencyCode().equals(currencyCode))
    		{
    			return c;
    		}
    	}
    	
    	return null;
    }

    // Remove currency from a user's wallet and add it back to the system
    public void removeCurrencyFromWallet(Wallet wallet, String currencyCode, double amount) 
    {
        // Check if the wallet has enough of the currency
        if (wallet.removeCurrency(currencyCode, amount)) {
            // Add back to the system
            Currency systemCurrency = findCurrency(currencyCode);
            if (systemCurrency != null) {
                systemCurrency.setAmount(systemCurrency.getAmount() + amount);
                System.out.println(amount + " units of " + currencyCode + " removed from wallet and added back to the system.");
            }
        } else {
            System.out.println("Insufficient balance or currency not found in wallet.");
        }
    }

    // Find a currency by its code in the system
    private Currency findCurrency(String currencyCode) 
    {
        for (Currency c : currencyList) {
            if (c.getCurrencyCode().equals(currencyCode)) {
                return c;
            }
        }
        return null; // Currency not found
    }

    public double convertCurrency(double amount, double sourceRate, double targetRate) 
    {
        return amount * (targetRate / sourceRate);
    }
    
    public boolean hasEnoughUSD (User user , double amount, String currencyCode)
    {
    	double requiredUSD = this.convertCurrency(amount,this.getCurrencyRate(currencyCode), 1.0);
    	
    	if(user.getAccount().getWallet().getCurrencyBalance("USD") < requiredUSD)
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    public boolean hasEnoughUSD (User user , double unitPrice , int quantity)
    {
    	double requiredUSD =  unitPrice * quantity;
    	
    	if(user.getAccount().getWallet().getCurrencyBalance("USD") < requiredUSD)
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    public double getCurrencyRate(String currencyCode)
    {
    	Currency currency = findCurrency(currencyCode); 
    	if(currency == null)
    	{
    		System.out.println("CURRENCY DOESNT EXIST IN THE SYSTEM");
    		return -1.0;
    	}
    	
    	else
    	{
    		return currency.getRateAgainstUSD();
    	}
    }
    
    public double getTax(double amount , String currencyCode)
    {
    	double inUSD = this.convertCurrency(amount , this.getCurrencyRate(currencyCode) , 1.0);
    	
    	double tax = fbr.calculateTax(inUSD);
    	
    	return tax;
    }
    
    public void recordTransaction(String cnic, String name, String transactionType, String assetType, String assetName, 
            String assetCode, double quantity, double unitPrice,String remarks) 
    {
    	fbr.recordTransaction(cnic, name, transactionType, assetType, assetName, assetCode, quantity, unitPrice, remarks);
    }
    
    // Display all currencies in the system
    public void displayCurrencies() 
    {
        System.out.println("Currencies in the system:");
        for (Currency c : currencyList) {
            System.out.println(c);
        }
    }
    
    
    public void loadCurrency(Currency currency)
    {
    	this.currencyList.add(currency);
    }
    
	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<Currency> currencyList) {
		this.currencyList = currencyList;
	}
	
	public List<prediction> predictCurrency()
    {
    	List<prediction> predictions = new ArrayList<prediction>();
    	
    	for(Currency c: currencyList)
    	{
    		double totalPrice = c.getRateAgainstUSD();
    		
    		Random random = new Random();
    		int randomFactor = random.nextInt(); 
    		
    		int count = randomFactor;

    		double averagePrice = totalPrice + count / random.nextInt();   		
    		
    		predictions.add(new prediction( c.getRateAgainstUSD(), averagePrice, c.getCurrencyName()));
    	}
    	
    	return predictions;
    }
}
