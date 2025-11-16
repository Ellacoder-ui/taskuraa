package com.taskura.taskuraa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> roleComboBox;

    /**
     * Initializes the controller class.
     * Populates the ComboBox with role options.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> roles = FXCollections.observableArrayList(
                "Student",
                "Teacher",
                "Professional",
                "Other"
        );
        roleComboBox.setItems(roles);
        // Set "Student" as the default selected item, matching the visual example's prompt text.
        roleComboBox.getSelectionModel().select("Student");
    }

    /**
     * Handles the action when the 'x' button next to the name field is clicked.
     */
    @FXML
    protected void clearNameField() {
        nameField.clear();
    }

    /**
     * Handles the action when the 'Enter' button is clicked.
     * In a real application, this would trigger navigation or data submission.
     */
    @FXML
    protected void handleEnterButton() {
        String name = nameField.getText();
        String role = roleComboBox.getSelectionModel().getSelectedItem();

        // Simple console output for demonstration
        System.out.println("User Name: " + name);
        System.out.println("User Role: " + role);

        // Add your application logic here (e.g., transition to a new scene)
    }
}