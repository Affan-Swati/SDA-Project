package main_package;

public class TransactionLog 
{
	private String transactionDateTime;
    private User user;         // The user associated with the transaction
    private String details;    // Details of the transaction

    // Constructor
    public TransactionLog(User user, String details) 
    {
        this.user = user;               // Set the user associated with this log
        this.details = details;         // Set the transaction details
        this.transactionDateTime = DateTime.getCurrentDate() + DateTime.getCurrentTime();
    }

    // Getter for Date and Time
    public String getTransactionDateTime() 
    {
        return transactionDateTime;
    }
    // Getter for user
    public User getUser() 
    {
        return user;
    }

    // Getter for transaction details
    public String getDetails() 
    {
        return details;
    }

    public String toString() 
    {
        return "TransactionLog{" +
                "date_time=" + transactionDateTime +
                ", user=" + user.toString() +
                ", details='" + details + '\'' +
                '}';
    }

	public void setUser(User user) {
		this.user = user;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setTransactionDateTime(String transactionDateTime) 
	{
		this.transactionDateTime = transactionDateTime;
	}
}
