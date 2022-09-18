package main.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerUserIntercation {
    @FXML
    Label current_mode;

    public void changeMode(){
        if (current_mode.getText() == "Continuous"){current_mode.setText("Step-By-Step");}
        else if (current_mode.getText() == "Step-By-Step") {current_mode.setText("Continuous");}
    }
}
