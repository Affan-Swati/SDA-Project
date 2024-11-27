package main_package;

import java.time.LocalDateTime;

public class Anamoly 
{
    private LocalDateTime dateTime;
    private boolean isResolved;
    private String description;
    private Admin assignedAdmin;

    // Constructor
    public Anamoly(String description) 
    {
        this.dateTime = LocalDateTime.now(); // Automatically set the current date and time
        this.isResolved = false;            // Default to unresolved
        this.description = description;
    }

    // Getter for dateTime
    public LocalDateTime getDateTime() 
    {
        return dateTime;
    }

    // Getter for isResolved
    public boolean isResolved() 
    {
        return isResolved;
    }

    // Getter for description
    public String getDescription() 
    {
        return description;
    }

    // Getter for assignedAdmin
    public Admin getAssignedAdmin() 
    {
        return assignedAdmin;
    }

    // Setter for assignedAdmin
    public void assignAdmin(Admin admin) 
    {
        this.assignedAdmin = admin;
    }

    // Method to resolve the anomaly
    public void resolve() 
    {
        this.isResolved = true;
        System.out.println("Anamoly resolved.");
    }

    public String toString() 
    {
        return "Anamoly{" +
                "dateTime=" + dateTime +
                ", isResolved=" + isResolved +
                ", description='" + description + '\'' +
                ", assignedAdmin=" + (assignedAdmin != null ? assignedAdmin.getName() : "None") +
                '}';
    }

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public void setResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAssignedAdmin(Admin assignedAdmin) {
		this.assignedAdmin = assignedAdmin;
	}
}

