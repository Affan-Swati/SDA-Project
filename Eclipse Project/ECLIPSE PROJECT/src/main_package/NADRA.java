package main_package;

import java.sql.*;


public class NADRA 
{
	private static final String connectionString = "jdbc:mysql://localhost:3306/NADRA"; // Database URL
    private static final String USER = "root"; // Database username
    private static final String PASSWORD = "raja@168"; // Database password

    public NADRA() 
    {
    
    }
    
    public boolean verifyUser(String cnic, String name) 
    {
        try (Connection conn = DriverManager.getConnection(connectionString, USER, PASSWORD)) 
        {
            // Step 1: Check if the user exists
            try (PreparedStatement userExistsStmt = conn.prepareStatement("SELECT * FROM NADRA.CitizenInformation WHERE cnic = ?")) 
            {
                userExistsStmt.setString(1, cnic);

                ResultSet userExistsRs = userExistsStmt.executeQuery();
                
                if (!userExistsRs.next()) 
                {
                    System.out.println("User with CNIC " + cnic + " does not exist.");
                    return false;
                }
            }

            // Step 2: Check if the name matches
            try (PreparedStatement nameMatchStmt = conn.prepareStatement("SELECT * FROM CitizenInformation WHERE cnic = ? AND name = ?")) 
            {
                nameMatchStmt.setString(1, cnic);
                nameMatchStmt.setString(2, name);
                ResultSet nameMatchRs = nameMatchStmt.executeQuery();

                if (!nameMatchRs.next()) {
                    System.out.println("Name does not match for the CNIC " + cnic + ".");
                    return false;
                }
            }

            // Step 3: Check if the age is 18 or older
            try (PreparedStatement ageCheckStmt = conn.prepareStatement(
                    "SELECT TIMESTAMPDIFF(YEAR, DateOfBirth, CURDATE()) AS Age FROM CitizenInformation WHERE cnic = ?")) 
            {
                ageCheckStmt.setString(1, cnic);
                ResultSet ageCheckRs = ageCheckStmt.executeQuery();
                

                if (ageCheckRs.next()) {
                    int age = ageCheckRs.getInt("Age");
                    if (age < 18) {
                        System.out.println("User with CNIC " + cnic + " is under 18 years old.");
                        return false;
                    }
                }
            }

            // All checks passed
            return true;

        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return false;
    }

}