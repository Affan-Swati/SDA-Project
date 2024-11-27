package user_package;
import java.time.*;

import BLL_package.Account;
import BLL_package.DBHandler;
import BLL_package.DateTime;

public class User 
{
	private String name;
    private String CNIC;
    private LocalDate DOB;
    private String email;
    private String phoneNumber;
    private String joinDate;
    private boolean isVerified;
    private Account account;
    
    public User(String name , String email ,String CNIC, String phoneNumber , LocalDate DOB) 
    {
    	this.name = name;
        this.email = email;
        this.CNIC = CNIC;
        this.phoneNumber = phoneNumber;
        this.account = null;
        this.DOB = DOB;
        this.joinDate = DateTime.getCurrentDate();
    }
    
    public void createAccount(String username , String password)
    {
    	if(this.account == null)
    	{
    		account = new Account(username,password,"user");
    	}
    	else
    	{
    		System.out.println("User " + name + " already has an account registered ( Username: " + account.getUsername() + " )");
    	}
    }
    
    public void deleteAccount()
    {
    	 if(this.account != null)
    	 {
    		 this.account = null;
    		 DBHandler db = DBHandler.getInstance();
    		 db.deleteAccount(CNIC);
    	 }
    	 else
    	 {
    		 System.out.println("User " + name + " has no account registered!");
    	 }
    }
    
    public String getCNIC() 
    {
		return CNIC;
	}

	public void setCNIC(String CNIC) {
		this.CNIC = CNIC;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified)
	{
		if(this.isVerified != isVerified)
		{
			this.isVerified = isVerified;
			DBHandler dbHandler = DBHandler.getInstance();
			dbHandler.setUserVerification(this.getCNIC(), isVerified);
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    public String getName()
    {
    	return this.name;
    }

	public LocalDate getDOB() {
		return DOB;
	}

	public void setDOB(LocalDate dOB) {
		DOB = dOB;
	}
}
