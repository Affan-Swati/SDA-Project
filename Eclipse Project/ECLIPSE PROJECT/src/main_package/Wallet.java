package main_package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Wallet 
{
    private String walletID; // Unique ID for the wallet
    private Map<String, Double> currencyBalances; // Currency code -> Amount held

    // Constructor
    public Wallet() 
    {
        this.walletID = UUID.randomUUID().toString();
        this.currencyBalances = new HashMap<>(); // Initialize with an empty set of currencies
    }

    // Get the wallet ID
    public String getWalletID() {
        return walletID;
    }

    // Add a currency to the wallet or increase its balance
    public void addCurrency(String currencyCode, double amount) 
    {
        if (amount < 0) 
        {
            System.out.println("Amount must be greater than zero!");
            return;
        }
        currencyBalances.put(currencyCode, currencyBalances.getOrDefault(currencyCode, 0.0) + amount);
        System.out.println(amount + " units of " + currencyCode + " added to wallet.");
    }

    // Remove a specific amount of a currency from the wallet
    public boolean removeCurrency(String currencyCode, double amount) 
    {
        if (amount < 0) 
        {
            System.out.println("Amount must be greater than zero!");
            return false;
        }

        if (!currencyBalances.containsKey(currencyCode)) {
            System.out.println("Currency not found in wallet: " + currencyCode);
            return false;
        }

        double currentBalance = currencyBalances.get(currencyCode);
        if (currentBalance >= amount) {
            currencyBalances.put(currencyCode, currentBalance - amount);
            System.out.println(amount + " units of " + currencyCode + " removed from wallet.");
            return true;
        } else {
            System.out.println("Insufficient balance of " + currencyCode + " in wallet.");
            return false;
        }
    }

    // Get the balance of a specific currency
    public double getCurrencyBalance(String currencyCode) 
    {
        return currencyBalances.getOrDefault(currencyCode, 0.0);
    }

    // Display all currency balances in the wallet
    public void displayWalletBalances() 
    {
        System.out.println("Wallet ID: " + walletID);
        System.out.println("Wallet Balances:");
        if (currencyBalances.isEmpty()) {
            System.out.println("No currencies in wallet.");
        } else {
            for (Map.Entry<String, Double> entry : currencyBalances.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    
	public List<Pair<String,Double>> getOwnedCurrenciesList(List<Currency> systemCurrencies)
	{
		List<Pair<String,Double>> currencyList = new ArrayList<>();
		
		for(Currency c : systemCurrencies)
		{
			if(currencyBalances.containsKey(c.getCurrencyCode()))
			{
				currencyList.add(new Pair<String,Double>(c.getCurrencyCode(),currencyBalances.get(c.getCurrencyCode())));
			}
		}
		
		
		return currencyList;
	}
    
	public Map<String, Double> getCurrencyBalances() {
		return currencyBalances;
	}

	public void setCurrencyBalances(Map<String, Double> currencyBalances) {
		this.currencyBalances = currencyBalances;
	}

	public void setWalletID(String walletID) {
		this.walletID = walletID;
	}
}
