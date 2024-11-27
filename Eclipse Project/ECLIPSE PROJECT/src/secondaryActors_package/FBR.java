package secondaryActors_package;

import java.sql.*;
import java.util.Date;
import java.util.List;

import BLL_package.FDCX;
import BLL_package.TransactionLog;
import user_package.User;

import java.util.ArrayList;


// TODO: record transactions in FBr database

public class FBR 
{
    private static final double DEFAULT_TAX = 0.075; // Default tax rate
    private static final String connectionString = "jdbc:mysql://localhost:3306/FBR"; // Database URL
    private static final String USER = "root"; // Database username
    private static final String PASSWORD = "raja@168"; // Database password

    public FBR() 
    {
    
    }
    
   
    public double calculateTax(double amount)
    {
    	return (DEFAULT_TAX * amount);
    }
    
    // Record a new transaction (buy/sell)
    public void recordTransaction(String cnic, String name, String transactionType, String assetType, String assetName, 
                                  String assetCode, double quantity, double unitPrice,String remarks) 
    {
        String query = "INSERT INTO TransactionLogs (CNIC, Name, DateOfTransaction, TransactionType, AssetType, AssetName, AssetCode, Quantity, UnitPrice, TaxPercentage, Remarks) " +
                       "VALUES (?, ?, CURDATE(), ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(connectionString, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cnic);
            stmt.setString(2, name);
            stmt.setString(3, transactionType); // Buy or Sell
            stmt.setString(4, assetType);       // Stock or Currency
            stmt.setString(5, assetName);
            stmt.setString(6, assetCode);
            stmt.setDouble(7, quantity);
            stmt.setDouble(8, unitPrice);
            stmt.setDouble(9, DEFAULT_TAX);
            stmt.setString(10, remarks);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction successfully recorded for CNIC: " + cnic);
            } else {
                System.out.println("Failed to record transaction for CNIC: " + cnic);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Generate a transaction report for a user
    public static List<TransactionLog> generateTaxReport(String cnic) throws Exception
    {
    	FDCX fdcx = FDCX.getInstance();
        String query = "SELECT * FROM TransactionLogs WHERE CNIC = ?";
        List<TransactionLog> transacs = new ArrayList<TransactionLog>();
        
        Connection conn = DriverManager.getConnection(connectionString, USER, PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(query); 

        stmt.setString(1, cnic);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
        	User user = fdcx.getUser(rs.getString("CNIC"));
        	
        	StringBuilder formattedDetails = new StringBuilder();
        	formattedDetails.append("Date of Transaction: ").append(rs.getString("DateOfTransaction")).append("\n");
        	formattedDetails.append("Transaction Type: ").append(rs.getString("TransactionType")).append("\n");
        	formattedDetails.append("Asset Name: ").append(rs.getString("AssetName")).append("\n");
        	formattedDetails.append("Asset Type: ").append(rs.getString("AssetType")).append("\n");
        	formattedDetails.append("Quantity: ").append(rs.getString("Quantity")).append("\n");
        	formattedDetails.append("Unit Price: ").append(rs.getString("UnitPrice")).append("\n");
        	formattedDetails.append("Total: ").append(rs.getString("TotalValue")).append("\n");
        	formattedDetails.append("Tax (%): ").append(rs.getString("TaxPercentage")).append("\n");
        	formattedDetails.append("Tax Paid: ").append(rs.getString("TaxCollected")).append("\n");
        	formattedDetails.append("Details: ").append(rs.getString("Remarks")).append("\n\n");
        	formattedDetails.append("=====================================================").append("\n\n");
        	
        	transacs.add(new TransactionLog(user, formattedDetails.toString()));
        }

        return transacs;
    }

	public static double getDefaultTax() 
	{
		return DEFAULT_TAX;
	}
}
