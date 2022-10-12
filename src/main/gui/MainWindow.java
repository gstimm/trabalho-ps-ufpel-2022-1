package main.gui;

import javafx.stage.*;
import javafx.scene.*;

import javafx.application.Application;
import javafx.fxml.*;

public class MainWindow extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));

        Scene scene = new Scene(root);
    
        stage.setTitle("Virtual Machine PS");
        stage.setScene(scene);
        stage.show();
    }

    public static void show(String[] args){
        launch(args);
    }
}
