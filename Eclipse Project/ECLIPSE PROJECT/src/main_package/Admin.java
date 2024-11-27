package main_package;
import java.time.*;

public class Admin extends User
{
    private FraudMonitor fraudMonitor;
    private StockManager stockManager;
    private CurrencyManager currencyManager;
	
	 public Admin(String name ,String email,String CNIC, String phoneNumber , LocalDate DOB) 
	 {
		 super(name ,email,CNIC,phoneNumber,DOB);
		 fraudMonitor = new FraudMonitor();
		 stockManager = StockManager.getInstance();
		 currencyManager = CurrencyManager.getInstance();
	 }
	 
	 public void registerAccount(String username , String password)
	 {
		 if(this.getAccount() != null)
		 {
			 System.out.println("ACCOUNT ALREADY EXISTS FOR ADMIN");
			 return;
		 }
		 this.setAccount(new Account(username,password,"admin"));
	 }
	 
	 public void addStockToSystem(String stockName , double unitPrice , int quantity)
	 {
		 stockManager.addStockToSystem(stockName, unitPrice, quantity);
	 }
	 
	 public void removeStockFromSystem(String stockName , int quantity)
	 {
		 stockManager.removeStockFromSystem(stockName, quantity);
	 }
	 
	 public void addCurrencyToSystem(String currencyName, String currencyCode, double rateAgainstUSD, String type, double amount) 
	 {
		 currencyManager.addCurrency(currencyName, currencyCode, rateAgainstUSD, type, amount);
	 }
	 
	 public void removeCurrencyFromSystem(String currencyName , double amount)
	 {
		 currencyManager.removeCurrency(currencyName, amount);
	 }
	 
	 void resolveAnamoly(Anamoly anamoly)
	 {
		 anamoly.assignAdmin(this);
		 anamoly.resolve();
	 }

	public FraudMonitor getFraudMonitor() {
		return fraudMonitor;
	}

	public void setFraudMonitor(FraudMonitor fraudMonitor) {
		this.fraudMonitor = fraudMonitor;
	}

	public StockManager getStockManager() {
		return stockManager;
	}

	public void setStockManager(StockManager stockManager) {
		this.stockManager = stockManager;
	}
}
