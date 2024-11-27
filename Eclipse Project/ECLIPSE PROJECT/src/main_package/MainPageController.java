package main_package;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.*;

public class MainPageController 
{
	@FXML
	private Button adminButton;
	@FXML
	private Button userButton;

	public void AdminLogin() throws Exception
	{
	    Parent adminlogin = FXMLLoader.load(getClass().getResource("/admin_package/AdminLogIn.fxml"));
	    Scene adminloginscene = new Scene(adminlogin);
	    Stage stage = (Stage) adminButton.getScene().getWindow(); // Get current stage
	    stage.setScene(adminloginscene);
	}
	
	public void UserMainPage() throws Exception
	{
	    Parent userMainPage = FXMLLoader.load(getClass().getResource("/user_package/UserMainPage.fxml"));
	    Scene userMainPageScene = new Scene(userMainPage);
	    Stage stage = (Stage) adminButton.getScene().getWindow(); // Get current stage
	    stage.setScene(userMainPageScene);
	}
	
}
