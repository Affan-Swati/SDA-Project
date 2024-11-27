package main_package;

import java.util.List;

public class BankingService 
{

	private DBHandler dbHandler = null;
	private CurrencyManager currencyManager = null;
	
	public BankingService ()
	{
		dbHandler = DBHandler.getInstance();
		currencyManager = CurrencyManager.getInstance();
	}
	
	public List<Pair<String,Double>> getFiatExchangeRates() 
	{ 
	    return dbHandler.getFiatExchangeRates();
	}
	
	public boolean buyFiat(User user, String currencyCode ,double amount)
	 {
		if(!currencyManager.hasEnoughUSD(user, amount + currencyManager.getTax(amount, currencyCode), currencyCode)) 
			return false;
		
		Currency c = currencyManager.getCurrency(currencyCode);
		
		 if(dbHandler.buyFiat(user.getCNIC(),c.getCurrencyName() , c.getCurrencyCode() , c.getRateAgainstUSD() , amount))
		 {
			 currencyManager.addCurrencyToWallet(user.getAccount().getWallet(), currencyCode, amount);
			 currencyManager.removeCurrencyFromWallet(user.getAccount().getWallet(), "USD", currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(currencyCode), 1.0) + currencyManager.getTax(amount, currencyCode));
			 dbHandler.sellFiat(user.getCNIC(), "Dollar" ,  "USD", c.getRateAgainstUSD(),currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(currencyCode), 1.0) + currencyManager.getTax(amount, currencyCode));
			 currencyManager.recordTransaction(user.getCNIC(), user.getName(), "Buy", "Fiat", c.getCurrencyName(), c.getCurrencyCode(), amount, c.getRateAgainstUSD(),"none"); 
			 return true;
	
		 }
		 else
		 {
			 System.out.println("INSUFFICIENT SYSTEM BALANCE!");
			 return false;
		 }
	 }
	
	public boolean sellFiat(User user, String currencyCode, double amount)
	{
		Currency c = currencyManager.getCurrency(currencyCode);
		
		 if(dbHandler.sellFiat(user.getCNIC(),c.getCurrencyName() , c.getCurrencyCode() , c.getRateAgainstUSD() , amount))
		 {
			 currencyManager.removeCurrencyFromWallet(user.getAccount().getWallet(), currencyCode, amount);
			 currencyManager.addCurrencyToWallet(user.getAccount().getWallet(), "USD", currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(currencyCode), 1.0) - currencyManager.getTax(amount, currencyCode));
			 dbHandler.buyFiat(user.getCNIC(), "Dollar","USD", c.getRateAgainstUSD(),currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(currencyCode), 1.0) - currencyManager.getTax(amount, currencyCode));
			 currencyManager.recordTransaction(user.getCNIC(), user.getName(), "Sell", "Fiat", c.getCurrencyName(), c.getCurrencyCode(), amount, c.getRateAgainstUSD(),"none"); 
			 return true;
		 }
		 else
		 {
			 System.out.println("INSUFFICIENT USER BALANCE!");
			 return false;
		 }
			 
	}
	
	public boolean transferFiat(User fromUser, User toUser, String currencyCode,double amount) // transfer is free of cost
	{
		Wallet fromWallet =  fromUser.getAccount().getWallet() ;
		Wallet toWallet = toUser.getAccount().getWallet();
		
		if(fromWallet.getCurrencyBalance(currencyCode) < amount)
		{
			System.out.println("INSUFFICIENT BALANCE!");
			return false;
		}
		
		else
		{
			Currency c = currencyManager.getCurrency(currencyCode);
			fromWallet.removeCurrency(currencyCode, amount);
			toWallet.addCurrency(currencyCode, amount);
			
			dbHandler.updateUserBalance(fromUser.getCNIC(),c.getCurrencyName(),currencyCode, c.getRateAgainstUSD() , amount, c.getType() , false);
			dbHandler.updateUserBalance(toUser.getCNIC(),c.getCurrencyName(),currencyCode, c.getRateAgainstUSD() , amount, c.getType() , true);
			
			return true;
		}
	 }
	
    public void exchangeFiat(String fromCurrencyCode , String toCurrencyCode , User user , double amount) // exchange is free of cost too
    {
    	double fromRate = currencyManager.getCurrencyRate(fromCurrencyCode);
    	double toRate = currencyManager.getCurrencyRate(toCurrencyCode);
    	
    	double newAmount = currencyManager.convertCurrency(amount, fromRate, toRate);
    	
    	this.sellFiat(user, fromCurrencyCode, amount);
    	this.buyFiat(user, toCurrencyCode, newAmount);
    }
}

