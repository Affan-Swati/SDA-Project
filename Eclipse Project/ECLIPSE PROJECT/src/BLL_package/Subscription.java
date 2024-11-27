package BLL_package;
import java.time.*;

public class Subscription 
{
    private String type; // monthly, quarterly, yearly, cancelled
    private double price;
    private LocalDate renewalDate;
    private final double monthlyPrice = 10; // USD 120 PER YEAR
    private final double qurterlyPrice = 22.5; // USD 90 PER YEAR
    private final double yearlyPrice = 65; // USD 65 PER YEAR
    private DBHandler dbHandler;

    public Subscription()
    {
        this.type = "cancelled";
        dbHandler = DBHandler.getInstance();
    }

    public boolean subscribe(String type, String userId , double USDBalance)
    {
    	if(!this.type.equals("cancelled"))
    	{
    		System.out.println("User " + userId + " already subscribed!");

    		return false;
    	}
    	
        this.type = type;

        if (this.type.equals("monthly"))
        {
            this.price = this.monthlyPrice;
        }
        else if (this.type.equals("quarterly"))
        {
            this.price = this.qurterlyPrice;
        }
        else if (this.type.equals("yearly"))
        {
            this.price = this.yearlyPrice;
        }

        // Check if the user has enough funds and update the database
        if (canSubscribeAndUpdateFunds(userId,USDBalance)) 
        {
            this.renewSubscription(userId);
            dbHandler.userSubscription(userId, this.type, this.price, this.renewalDate);
            
            System.out.println("Subscription successful. Next renewal date: " + this.renewalDate);
            return true;
        } 
        else 
        {
            System.out.println("Insufficient funds to subscribe to " + this.type + " plan.");
            this.type = "cancelled"; // Reset subscription type if funds are insufficient
            return false;
        }
    }

    public void renewSubscription(String CNIC) 
    {
        if (this.type.equals("monthly"))
        {
            renewalDate = DateTime.getDateAfterMonths(1);
        }
        else if (this.type.equals("quarterly"))
        {
            renewalDate = DateTime.getDateAfterMonths(3);
        }
        else if (this.type.equals("yearly"))
        {
            renewalDate = DateTime.getDateAfterMonths(12);
        }
        
        dbHandler.updateRenewalDate(CNIC , renewalDate);
    }

    public void cancelSubscription(String userID) 
    {
        this.type = "cancelled";
        dbHandler.cancelSubscription(userID);
    }

    public void changeSubscription(String newType) 
    {
        this.type = newType;
    }

    public boolean canSubscribeAndUpdateFunds(String userId , double USDBalance) 
    {
        try 
        {

            if (USDBalance >= this.price) 
            {
               
                dbHandler.updateUserBalance(userId, "Dollar", "USD", 1.0, this.price, "Fiat", false);
                System.out.println("Subscription cost deducted. Remaining balance: " + (USDBalance - this.price));
                return true;
            } 
            else 
            {
                return false;
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Error while processing subscription: " + e.getMessage());
            return false;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }
}
