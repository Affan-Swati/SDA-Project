package main_package;

public class Currency 
{

    private String currencyName;    // Name of the currency (e.g., US Dollar, Bitcoin)
    private String currencyCode;    // Code for the currency (e.g., USD, BTC)
    private double rateAgainstUSD;  // Exchange rate compared to USD
    private String type;            // Type of currency: "Fiat" or "Crypto"
    private double amount;

    // Constructor
    public Currency(String currencyName, String currencyCode, double rateAgainstUSD, String type, double amount) 
    {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.rateAgainstUSD = rateAgainstUSD;
        this.type = type;
        this.amount = amount;
    }
    
    public String getCurrencyName() 
    {
        return currencyName;
    }
    
    public double getAmount() 
    {
        return amount;
    }

    public String getCurrencyCode() 
    {
        return currencyCode;
    }

    public double getRateAgainstUSD() {
        return rateAgainstUSD;
    }

    public String getType() {
        return type;
    }
    
    // Setters
    public void setCurrencyName(String currencyName) 
    {
        this.currencyName = currencyName;
    }

    public void setCurrencyCode(String currencyCode) 
    {
        this.currencyCode = currencyCode;
    }

    public void setRateAgainstUSD(double rateAgainstUSD) 
    {
        this.rateAgainstUSD = rateAgainstUSD;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setAmount(double amount)
    {
    	this.amount = amount;
    }
    
    // Display information
    public String toString() 
    {
        return "Currency: " + currencyName + " (" + currencyCode + ")\n" +
               "Type: " + type + "\n"+
               "Rate Against USD: " + rateAgainstUSD + "\n";
    }
}
