package admin_package;

import java.io.IOException;

import BLL_package.FDCX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AdminLogInController 
{
	@FXML private Button submitButton;
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Button backbutton;

	public void handleSubmit() throws IOException
	 {
		 FDCX fdcx = FDCX.getInstance();
		 int status = fdcx.adminLogin(username.getText(), password.getText());

		 if (status == 0) 
		 {
			Parent adminlogin = FXMLLoader.load(getClass().getResource("AdminWelcomePage.fxml"));
		    Scene adminloginscene = new Scene(adminlogin);
		    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
		    stage.setScene(adminloginscene);
		 }
	     
	     else if(status == 1)
	     {
	    	 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: ADMIN DOESN'T EXIST");

	         alert.showAndWait();
	         
	         username.setText(null);
	         password.setText(null);

	     }
	     
	     else if(status == 2)
	     {
	    	 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: INCORRECT PASSWORD");

	         alert.showAndWait();
	         
	         username.setText(null);
	         password.setText(null);
	     }
       
	 }
	 
	 public void back() throws Exception
	 {
		Parent MainPage = FXMLLoader.load(getClass().getResource("/main_package/MainPage.fxml"));
	    Scene MainPageScene = new Scene(MainPage);
	    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
	    stage.setScene(MainPageScene);
	 }
}
