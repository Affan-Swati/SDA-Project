package BLL_package;

import java.util.List;

import user_package.User;

public class CryptoService 
{

	DBHandler dbHandler = null;
	CurrencyManager currencyManager = null;
	
	public CryptoService()
	{
		dbHandler = DBHandler.getInstance();
		currencyManager = CurrencyManager.getInstance();
	}
	
	public List<Pair<String,Double>> getCryptoExchangeRates() 
	{ 
	    return dbHandler.getCryptoExchangeRates();
	}
	
	
	
	public boolean buyCrypto(User user, String cryptoCode ,double amount)
	 {
		if(!currencyManager.hasEnoughUSD(user, amount + currencyManager.getTax(amount, cryptoCode), cryptoCode)) 
			return false;
		
		Currency c = currencyManager.getCurrency(cryptoCode);
		
		 if(dbHandler.buyCrypto(user.getCNIC(), c.getCurrencyName(),cryptoCode,c.getRateAgainstUSD() , amount))
		 {
			 currencyManager.addCurrencyToWallet(user.getAccount().getWallet(), cryptoCode, amount);
			 currencyManager.removeCurrencyFromWallet(user.getAccount().getWallet(), "USD", currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(cryptoCode), 1.0) + currencyManager.getTax(amount, cryptoCode));
			 dbHandler.sellFiat(user.getCNIC(), "Dollar", "USD", c.getRateAgainstUSD(),currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(cryptoCode), 1.0) + currencyManager.getTax(amount, cryptoCode));
			 currencyManager.recordTransaction(user.getCNIC(), user.getName(), "Buy", "Crypto", c.getCurrencyName(), c.getCurrencyCode(), amount, c.getRateAgainstUSD(),"none"); 
			 return true;
		 }
		 else
		 {
			 System.out.println("INSUFFICIENT SYSTEM BALANCE!");
			 return false;
		 }
	 }
	

	public boolean sellCrypto(User user, String cryptoCode, double amount)
	{
		Currency c = currencyManager.getCurrency(cryptoCode);
		
		 if(dbHandler.sellCrypto(user.getCNIC(), c.getCurrencyName() ,cryptoCode, c.getRateAgainstUSD(), amount))
		 {
			 currencyManager.removeCurrencyFromWallet(user.getAccount().getWallet(), cryptoCode, amount);
			 currencyManager.addCurrencyToWallet(user.getAccount().getWallet(), "USD", currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(cryptoCode), 1.0) - currencyManager.getTax(amount, cryptoCode));
			 dbHandler.buyFiat(user.getCNIC(), "Dollar","USD", c.getRateAgainstUSD(),currencyManager.convertCurrency(amount, currencyManager.getCurrencyRate(cryptoCode), 1.0) - currencyManager.getTax(amount, cryptoCode));
			 currencyManager.recordTransaction(user.getCNIC(), user.getName(), "Sell", "Crypto", c.getCurrencyName(), c.getCurrencyCode(), amount, c.getRateAgainstUSD(),"none"); 
			 return true;
		 }
		 else
		 {
			 System.out.println("INSUFFICIENT USER BALANCE!");
			 return false;
		 }
			 
	}

	public boolean transferCrypto(User fromUser, User toUser, String cryptoCode,double amount)
	{
		Wallet fromWallet =  fromUser.getAccount().getWallet() ;
		Wallet toWallet = toUser.getAccount().getWallet();
		
		if(fromWallet.getCurrencyBalance(cryptoCode) < amount)
		{
			System.out.println("INSUFFICIENT BALANCE!");
			return false;
		}
		
		else
		{
			fromWallet.removeCurrency(cryptoCode, amount);
			toWallet.addCurrency(cryptoCode, amount);
			
			Currency c = currencyManager.getCurrency(cryptoCode);
			
			dbHandler.updateUserBalance(fromUser.getCNIC(),c.getCurrencyName(),cryptoCode, c.getRateAgainstUSD() , amount, c.getType() , false);
			dbHandler.updateUserBalance(toUser.getCNIC(),c.getCurrencyName(),cryptoCode, c.getRateAgainstUSD() , amount, c.getType() , true);
			
			return true;
		}
	}
	
	public void exchangeCrypto(String fromCryptoCode , String toCryptoCode , User user , double amount)
    {
    	
    	double fromRate = currencyManager.getCurrencyRate(fromCryptoCode);
    	double toRate = currencyManager.getCurrencyRate(toCryptoCode);
    	
    	double newAmount = currencyManager.convertCurrency(amount, fromRate, toRate);
    	
    	this.sellCrypto(user, fromCryptoCode, amount);
    	this.buyCrypto(user, toCryptoCode, newAmount);
    	
    }

}

