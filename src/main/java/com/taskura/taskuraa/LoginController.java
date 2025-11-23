package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField nameField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleEnterButton() {
        String username = nameField.getText().trim();
        if (username.isEmpty()) username = "Guest";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            BorderPane root = loader.load();

            HomeController controller = loader.getController();
            controller.setUsername(username);

            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}