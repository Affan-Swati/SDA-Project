package main_package;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class UserSignUpController 
{
	@FXML private Button submitButton;
	@FXML private TextField name;
	@FXML private TextField email;
	@FXML private TextField cnic;
	@FXML private TextField phone;
	@FXML private DatePicker dob;
	@FXML private Button backbutton;

	 public void handleSubmit() throws IOException
	 {		
        FDCX fdcx = FDCX.getInstance();        
        
        if(fdcx.getUser(cnic.getText()) == null)
        	fdcx.registerUser(name.getText(), email.getText(), cnic.getText(), phone.getText(), dob.getValue());
        
        else
        {
   		 	 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: USER ACCOUNT ALREADY EXISTS!");

	         alert.showAndWait();
	         	         
	         Parent MainPage = null;
				try {
					MainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 	    Scene MainPageScene = new Scene(MainPage);
		 	    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
		 	    stage.setScene(MainPageScene);
		 	    return;
        }
        
        boolean status = fdcx.verifyUser(fdcx.getUser(cnic.getText()));
        
        if(status == false)
        {           	
        	 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: USER COULD NOT BE VERIFIED THROUGH NADRA!");

	         alert.showAndWait();
	         
	         fdcx.removeUser(cnic.getText());
	         
	         Parent MainPage = null;
				try {
					MainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 	    Scene MainPageScene = new Scene(MainPage);
		 	    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
		 	    stage.setScene(MainPageScene);
        	 
        }
        
        else if(status == true)
        {
        	Parent MainPage = null;
    	    FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCreateAccount.fxml"));
    	    try {
    	        MainPage = loader.load();
    	    } catch (IOException e1) {
    	        e1.printStackTrace();
    	    }

    	    // Access the controller and set the parameter
    	    UserCreateAccountController controller = loader.getController();
    	    controller.setCNIC(cnic.getText()); 

    	    // Set up the scene and stage
    	    Scene MainPageScene = new Scene(MainPage);
    	    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
    	    stage.setScene(MainPageScene);
        }
	 }
	 
	 public void back() throws Exception
	 {
		Parent MainPage = FXMLLoader.load(getClass().getResource("UserMainPage.fxml"));
	    Scene MainPageScene = new Scene(MainPage);
	    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
	    stage.setScene(MainPageScene);
	 }

}
