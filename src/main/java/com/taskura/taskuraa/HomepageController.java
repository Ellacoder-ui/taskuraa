package com.taskura.taskuraa;

import com.taskura.taskuraa.InMemoryData;
import com.taskura.taskuraa.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class HomepageController {

    @FXML
    private TextField usernameField;

    @FXML
    private ChoiceBox<String> roleChoice;

    @FXML
    private void initialize() {
        // Updated role choices
        roleChoice.getItems().addAll("Student", "Teacher", "Other");
        roleChoice.setValue("Student");
    }

    @FXML
    private void onEnter() {
        String name = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String role = roleChoice.getValue();

        if (name.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Missing Input");
            a.setHeaderText(null);
            a.setContentText("Please enter your name before continuing.");
            a.showAndWait();
            return;
        }

        // Store user in memory
        User user = new User(name, role);
        InMemoryData.USERS.add(user);

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Welcome!");
        a.setHeaderText("Hello, " + name + "!");
        a.setContentText("Role selected: " + role + "\n\n(Replace with scene switch once dashboard is ready.)");
        a.showAndWait();
    }
}
