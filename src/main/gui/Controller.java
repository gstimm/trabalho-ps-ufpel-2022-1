package main.gui;
import javafx.fxml.*;
import javafx.scene.control.*;

public class Controller {
    
    
    @FXML
    Label current_mode;

    public void changeMode(){
        if (current_mode.getText().equals(ExecutionMode.CONTINUOUS.toString())){
            current_mode.setText(ExecutionMode.STEP.toString());
        }
        else {
            current_mode.setText(ExecutionMode.CONTINUOUS.toString());
        }
    }
    public void exitApplication(){
        System.exit(0);
    }
}
