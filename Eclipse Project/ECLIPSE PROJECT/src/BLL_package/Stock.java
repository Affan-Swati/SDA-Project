package BLL_package;

public class Stock 
{
    private String name;
    private double unitPrice;
    private String purchase_time;
    private int quantity;
    private boolean available;

    public Stock(String name, double unitPrice , int quantity) 
    {
        this.name = name;
        this.unitPrice = unitPrice;
        this.available = true;
        this.quantity = quantity;
    }

    public void purchaseStock()
    {
        this.purchase_time = DateTime.getCurrentDate() + DateTime.getCurrentTime();
    }
    
    public void getPrediction() 
    {
        // Predict stock trends
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getPurchase_time() {
		return purchase_time;
	}

	public void setPurchase_time(String purchase_time) {
		this.purchase_time = purchase_time;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getQuantity() 
	{
		return quantity;
	}

	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}
}
