package main_package;

import BLL_package.FDCXFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	FDCXFactory factory = FDCXFactory.getInstance();
    	factory.initializeSystem();
        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
