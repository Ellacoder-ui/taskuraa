package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomepageController {

    @FXML
    private TextField usernameField;

    @FXML
    private ChoiceBox<String> roleChoice;

    @FXML
    private Button enterBtn;

    @FXML
    public void initialize() {
        // Populate role choices
        roleChoice.getItems().addAll("Student", "Developer", "Designer", "Manager", "Other");
        roleChoice.setValue("Student");
    }

    @FXML
    public void onEnter() {
        String username = usernameField.getText() != null ? usernameField.getText().trim() : "";
        String role = roleChoice.getValue();

        if (username.isEmpty()) {
            // Simple required check — visual hint only
            usernameField.setStyle("-fx-border-color: red;");
            return;
        }

        User user = new User(username, role);
        InMemoryData.USERS.add(user);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/taskura/taskuraa/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) enterBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Home");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}