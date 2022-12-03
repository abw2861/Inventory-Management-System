package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Inventory;

/** This is the Main class.

 FUTURE_ENHANCEMENT In order to extend functionality of this program in a future update, I would add a method that alerts the user when certain parts or products inventory level is getting low. The user would set the threshold for when the system would alert.
 The threshold could be the same for every item in the system and/or custom for individual items, based on user preference. This would allow the company to stay ahead and not allow items to go out of stock.

 The Javadoc html file is located at Javadoc/index.html
  */
public class Main extends Application {

    /** This method loads the Main Form screen. */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root, 1200, 720));
        stage.show();
    }

    /** This method launches the application. */
    public static void main(String[] args){
        launch(args);
    }
}










