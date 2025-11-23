package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {

    @FXML private Label nameLabel;
    @FXML private Button homeButton;
    @FXML private Button tasksButton;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        nameLabel.setText(username);
    }

    @FXML
    public void initialize() {

        // Sidebar buttons
        homeButton.setOnAction(e -> {
            // Already on Home, do nothing
        });

        tasksButton.setOnAction(e -> switchToTasks());
    }

    private void switchToTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tasks.fxml"));
            Parent root = loader.load();

            // Pass username to TasksController
            TasksController controller = loader.getController();
            controller.setUsername(username);

            // Replace scene root
            Stage stage = (Stage) tasksButton.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}