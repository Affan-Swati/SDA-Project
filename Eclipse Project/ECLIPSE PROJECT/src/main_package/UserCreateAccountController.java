package main_package;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar;

public class UserCreateAccountController
{
	@FXML private Button backButton;
	@FXML private Button submitButton;
	@FXML private TextField username;
	@FXML private PasswordField pass;
	@FXML private PasswordField conf_pass;
	
	private String CNIC;
	
	public void submit()
	{
		if(!pass.getText().equals(conf_pass.getText()))
		{
			 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: PASSWORD MISMATCH!");

	         alert.showAndWait();
	         
	         username.setText("");
	         pass.setText("");
	         conf_pass.setText("");
		}
		
		else
		{
			FDCX fdcx = FDCX.getInstance();
			fdcx.createUserAccount(fdcx.getUser(CNIC), username.getText(), pass.getText());
			DBHandler.getInstance().updateUserBalance(CNIC, "Dollar", "USD", 1.0, 2000, "Fiat", true);
			fdcx.getUser(CNIC).getAccount().getWallet().addCurrency("USD", 2000);
			
			Parent MainPage = null;
			try {
				MainPage = FXMLLoader.load(getClass().getResource("UserMainPage.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Scene MainPageScene = new Scene(MainPage);
		    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
		    stage.setScene(MainPageScene);
		}	
		
	}
	
	public void setCNIC(String c)
	{
		CNIC = c;
	}
	
	public void back()
	{
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("WARNING!");
	    alert.setHeaderText("");
	    alert.setContentText("Going back will lose all progress");

	    // Add Yes and No buttons
	    ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
	    alert.getButtonTypes().setAll(yesButton, noButton);

	    // Show the alert and wait for user input
	    Optional<ButtonType> result = alert.showAndWait();

	    if (result.isPresent() && result.get() == yesButton) 
	    {
	        FDCX fdcx = FDCX.getInstance();
	        fdcx.removeUser(CNIC);
	        
	        Parent mainPage = null;
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
	        try {
	            mainPage = loader.load();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

            Scene mainPageScene = new Scene(mainPage);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(mainPageScene);
	        
	    } 		

	}

}
