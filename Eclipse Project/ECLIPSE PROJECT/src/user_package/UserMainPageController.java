package user_package;

import java.io.IOException;

import BLL_package.FDCX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class UserMainPageController 
{
	@FXML private Button submitButton;
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Button backbutton;
	@FXML private Hyperlink signUpbutton;

	 public void handleSubmit() throws IOException
	 {
		 FDCX fdcx = FDCX.getInstance();
		 int status = fdcx.userLogin(username.getText(), password.getText());

		 if (status == 0) 
		 {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("UserWelcomePage.fxml"));
	        Parent mainPage = loader.load(); 

	        UserWelcomePageController controller = loader.getController();
	        controller.setUsername(username.getText()); 

	        Scene mainPageScene = new Scene(mainPage);
	        Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
	        stage.setScene(mainPageScene);
		 }
	     
	     else if(status == 1)
	     {
	    	 Alert alert = new Alert(AlertType.ERROR);

	         alert.setTitle("ERROR");
	         alert.setHeaderText(null);  // No header
	         alert.setContentText("ERROR: USER DOESN'T EXIST");

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
	 
	 public void SignUp() throws Exception
	 {
		Parent signup = FXMLLoader.load(getClass().getResource("UserSignUp.fxml"));
	    Scene signupscene = new Scene(signup);
	    Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
	    stage.setScene(signupscene);
	 }	 
}
