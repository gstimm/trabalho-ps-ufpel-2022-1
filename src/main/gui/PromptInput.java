package main.gui;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;;

public class PromptInput extends Application {
    @FXML
    Button input_enter;
    @FXML
    TextField input_value;

    short value;

    public PromptInput(){
        value = 0;
        initialize();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("InputPopUP.fxml"));
        
        Scene scene = new Scene(root);
    
        stage.setTitle("Enter a number");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void initialize() {
    }

    private void getInputField(){
        try{
            value = Short.parseShort(input_value.getText());
        }
        catch (NumberFormatException e){
            value = (short) 0;
        }
        try {
            stop();
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public short getShort(){
        return this.value;
    }
    public void showWindow(String args){
        launch(args);
    }
}
