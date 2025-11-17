package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ArchiveController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}