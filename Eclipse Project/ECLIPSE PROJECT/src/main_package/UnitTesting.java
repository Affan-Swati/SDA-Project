package main_package;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class UnitTesting 
{	
//	@Test
//    public void createUser() 
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		
//		User user = fdcx.getUser("3740583626159");
//		assertEquals(user.getCNIC(),"3740583626159");		
//    }
	
//	@Test
//	public void verifyUserViaNadra()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		assertTrue(fdcx.verifyUser(user));
//	}
	
//	@Test
//	public void createUserAccount()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		
//		fdcx.createUserAccount(user,"affan","123");
//	}
	
//	@Test
//	public void createAdminAndAdminAccount()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Adil Nadeem", "adil.nadeem@gmail.com", "1234567898765", "+923339412345", LocalDate.of(2002, 9, 27),"adil","123");
//		
//	}
//	
	
//	@Test
//	public void addStockToSystem()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Asim Muneer", "asim.muneer@gmail.com", "1234512398765", "+923339812345", LocalDate.of(1947, 8, 14),"asim","123");
//		fdcx.addStockToSystem("Tesla", 20.0, 20);
//	}
	
//	@Test
//	public void removeStockFromSystem()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.addStockToSystem("SpaceX", 32.0, 50);
//		fdcx.removeStockFromSystem("SpaceX", 45);
//	}
	
//	@Test
//	public void addRemoveCurrencyFromToSystem()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.addCurrencyToSystem("dollar", "USD" , 1.0, "Fiat", 2000);
//		fdcx.addCurrencyToSystem("pound", "GBP" , 0.80, "Fiat", 1200);
//		fdcx.removeCurrencyFromSystem("GBP", 100);
//		fdcx.addCurrencyToSystem("bitcoin", "BTC" , 100.50, "Crypto", 18);
//		fdcx.removeCurrencyFromSystem("BTC", 5);
//	}
	
//	@Test
//	public void assignRemoveStockFromToUser()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		fdcx.createUserAccount(user,"affan","123");
//		
//		fdcx.addStockToSystem("SpaceX", 10.0, 50);
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		DBHandler.getInstance().updateUserBalance(user.getCNIC(), "Dollar", "USD", 1.0, 200000, "Fiat", true);
//		user.getAccount().getWallet().addCurrency("USD", 200000);
//		
//		Stock stock = new Stock("SpaceX" , 10.0 , 0);
//		fdcx.assignStockToUser(user, stock, 7);
//		fdcx.removeStockFromUser(user, stock, 5);
//	}
	
//	@Test
//	public void assignRemoveCurrencyFromToUser()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		fdcx.createUserAccount(user,"affan","123");
//
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		fdcx.addCurrencyToSystem("Bitcoin", "BTC", 0.01, "Crypto", 18);
//		
//		DBHandler.getInstance().updateUserBalance(user.getCNIC(), "Dollar", "USD", 1.0, 200000, "Fiat", true);
//		user.getAccount().getWallet().addCurrency("USD", 200000);
//		
//		fdcx.depositFunds("Fiat", user, "GBP", 2000);
//		fdcx.depositFunds("Crypto", user, "BTC", 3);
//		fdcx.withdrawFunds("Crypto", user, "BTC", 2);
//	}
	
//	@Test
//	public void currencyExchange()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		fdcx.createUserAccount(user,"affan","123");
//
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		fdcx.addCurrencyToSystem("Canadian Dollar", "CAD", 1.8, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Bitcoin", "BTC", 0.01, "Crypto", 18);
//		fdcx.addCurrencyToSystem("Etherium", "ETH", 0.03, "Crypto", 10);
//		
//		DBHandler.getInstance().updateUserBalance(user.getCNIC(), "Dollar", "USD", 1.0, 200000, "Fiat", true);
//		user.getAccount().getWallet().addCurrency("USD", 200000);
//		
//		fdcx.depositFunds("Fiat", user, "GBP", 2000);
//		fdcx.depositFunds("Crypto", user, "BTC", 3);
//		
//		fdcx.exchangeFiat("GBP", "CAD", user, 5);
//		fdcx.exchangeCrypto("BTC", "ETH", user, 2);
//	}
	
//	@Test
//	public void currencyTrade()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		fdcx.createUserAccount(user,"affan","123");
//		
//		fdcx.registerUser("Adil Nadeem", "adil.nadeem@gmail.com", "3740583620979", "+923339121521", LocalDate.of(2002, 9, 27));
//		User user_2 = fdcx.getUser("3740583620979");
//		fdcx.verifyUser(user_2);
//		fdcx.createUserAccount(user_2,"adil","123");
//
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		fdcx.addCurrencyToSystem("Canadian Dollar", "CAD", 1.8, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Bitcoin", "BTC", 0.01, "Crypto", 18);
//		fdcx.addCurrencyToSystem("Etherium", "ETH", 0.03, "Crypto", 10);
//		
//		DBHandler.getInstance().updateUserBalance(user.getCNIC(), "Dollar", "USD", 1.0, 200000, "Fiat", true);
//		user.getAccount().getWallet().addCurrency("USD", 200000);
//		
//		fdcx.depositFunds("Fiat", user, "GBP", 2000);
//		fdcx.depositFunds("Crypto", user, "BTC", 3);
//
//		fdcx.tradeFunds("Fiat", user, user_2, "GBP", 1000);
//	}
	
//	@Test
//	public void getTaxReportViaFBR()
//	{
//		FDCX fdcx = new FDCX();
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		fdcx.createUserAccount(user,"affan","123");
//
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		fdcx.addCurrencyToSystem("Bitcoin", "BTC", 0.01, "Crypto", 18);
//		
//		DBHandler.getInstance().updateUserBalance(user.getCNIC(), "Dollar", "USD", 1.0, 200000, "Fiat", true);
//		user.getAccount().getWallet().addCurrency("USD", 200000);
//		
//		fdcx.depositFunds("Fiat", user, "GBP", 2000);
//		fdcx.depositFunds("Crypto", user, "BTC", 3);
//		fdcx.withdrawFunds("Crypto", user, "BTC", 2);
//		
//		List<String> report = FDCX.getTaxReport(user.getCNIC());
//		
//		for(String str : report)
//		{
//			System.out.println(str);
//		}
//	}
	
//	@Test
//	public void subscriptionTesting()
//	{
//		FDCX fdcx = FDCX.getInstance();
//		
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		fdcx.createUserAccount(user,"affan","123");
//
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		fdcx.addCurrencyToSystem("Bitcoin", "BTC", 0.01, "Crypto", 18);
//		
//		DBHandler.getInstance().updateUserBalance(user.getCNIC(), "Dollar", "USD", 1.0, 200000, "Fiat", true);
//		user.getAccount().getWallet().addCurrency("USD", 200000);
//		
//		fdcx.depositFunds("Fiat", user, "GBP", 2000);
//		fdcx.depositFunds("Crypto", user, "BTC", 3);
//		
//		fdcx.subscribe(user, "monthly");
//		
//		
//	}
	
	/* FDCX FACTORY TESTING */ 
	
//	@Test
//	public void AddAdmins()
//	{
//		FDCXFactory.getInstance().initializeSystem();
//		
//		FDCX fdcx = FDCX.getInstance();
//		
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");	
//	}
	
//	@Test
//	public void LoadAdmins()
//	{
//		FDCXFactory.getInstance().initializeSystem();
//		
//		FDCX fdcx = FDCX.getInstance();
//		
//		System.out.println(fdcx.getAdmin("1234512398987").getAccount().getUsername());	
//	}
	
//	@Test
//	public void AddUsers()
//	{
//		FDCXFactory.getInstance().initializeSystem();
//		FDCX fdcx = FDCX.getInstance();
//		
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		
//		fdcx.registerUser("Affan Ahmad", "affanswati12@gmail.com", "3740583626159", "+923339464521", LocalDate.of(2004, 04, 12));
//		User user = fdcx.getUser("3740583626159");
//		fdcx.verifyUser(user);
//		fdcx.createUserAccount(user,"affan","123");
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		fdcx.addCurrencyToSystem("Canadian Dollar", "CAD", 1.8, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Bitcoin", "BTC", 0.01, "Crypto", 18);
//		fdcx.addCurrencyToSystem("Etherium", "ETH", 0.03, "Crypto", 10);
//		
//		DBHandler.getInstance().updateUserBalance(user.getCNIC(), "Dollar", "USD", 1.0, 200000, "Fiat", true);
//		user.getAccount().getWallet().addCurrency("USD", 200000);
//		
//		fdcx.depositFunds("Fiat", user, "GBP", 2000);
//		fdcx.depositFunds("Crypto", user, "BTC", 3);
//		fdcx.subscribe(user, "quarterly");
//		
//		fdcx.registerUser("Adil Nadeem", "adil.nadeem@gmail.com", "3740583620979", "+923339121521", LocalDate.of(2002, 9, 27));
//		User user_2 = fdcx.getUser("3740583620979");
//		fdcx.verifyUser(user_2);
//		fdcx.createUserAccount(user_2,"adil","123");
//		
//		
//		fdcx.registerUser("Shayaan Khalid", "shayaan.khalid@gmail.com", "3740654620970", "+923339987521", LocalDate.of(2005, 3, 30));
//		User user_3 = fdcx.getUser("3740654620970");
//		fdcx.verifyUser(user_3);
//	}
////	
//	@Test
//	public void LoadUsers()
//	{
//		FDCXFactory.getInstance().initializeSystem();
//		
//		FDCX fdcx = FDCX.getInstance();
//		
//		System.out.println(fdcx.getUser("3740583626159").getAccount().getWallet().getCurrencyBalance("USD") + " points-> " + fdcx.getUser("3740583626159").getAccount().getLoyaltyPoints());
//		System.out.println(fdcx.getUser("3740583620979").getAccount().getPassword());
//		
//
//		if(fdcx.getUser("3740654620970").getAccount() == null)
//			System.out.println(fdcx.getUser("3740654620970").getName() + " has no account created!");
//		
//		
//		
//	}
	
//	@Test
//	public void AddSystemStockAndCurrencies()
//	{
//		FDCXFactory.getInstance().initializeSystem();
//		
//		FDCX fdcx = FDCX.getInstance();
//		
//		fdcx.addAdmin("Azlan Awan", "azlan.awan@gmail.com", "1234512398987", "+923335672345", LocalDate.of(2023, 04, 27),"azlan","123");
//		
//		
//		fdcx.addStockToSystem("Tesla", 20.0, 20);
//		fdcx.addStockToSystem("SpaceX", 40.0, 38);
//		fdcx.addStockToSystem("Apple", 80.0, 53);
//		
//		fdcx.addCurrencyToSystem("Dollar", "USD", 1.0, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Pound", "GBP", 0.85, "Fiat", 10000);
//		fdcx.addCurrencyToSystem("Canadian Dollar", "CAD", 1.8, "Fiat", 20000);
//		fdcx.addCurrencyToSystem("Bitcoin", "BTC", 0.01, "Crypto", 18);
//		fdcx.addCurrencyToSystem("Etherium", "ETH", 0.03, "Crypto", 10);
//		
//	}
	
//	@Test
//	public void LoadSystemStockAndCurrencies()
//	{
//		FDCXFactory.getInstance().initializeSystem();
//		
//		FDCX fdcx = FDCX.getInstance();
//		
//		List<Pair<String,Double>> listCrypto = fdcx.viewCryptoExchangeRates();
//		List<Pair<String,Double>> listFiat = fdcx.viewFiatExchangeRates();
//		
//		for(Pair<String,Double> p : listCrypto)
//		{
//			System.out.println("CryptoCode: " + p.getFirst() + " , Rate(Against USD): " + p.getSecond());
//		}
//		
//		for(Pair<String,Double> p : listFiat)
//		{
//			System.out.println("FiatCode: " + p.getFirst() + " , Rate(Against USD): " + p.getSecond());
//		}
//	
//	}
//	
//	@Test
//	public void LoadTransactionLogs()
//	{
//		FDCXFactory.getInstance().initializeSystem();
//		
//		FDCX fdcx = FDCX.getInstance();
//		
//
//		List<TransactionLog> logs = fdcx.getTransactionLogs();
//		
//		for(TransactionLog l : logs)
//		{
//			System.out.println(l.getUser().getCNIC() + " " + l.getDetails() + " "+l.getTransactionDateTime());
//		}
//	
//	}
//	
	
}
