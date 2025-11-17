package com.taskura.taskuraa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    // ----- Login Screen -----


    // ----- Hello Screen Fields -----
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> roleComboBox;

    // ---------------------------
    // Scene Switch
    // ---------------------------


    // ---------------------------
    // Initialize ComboBox
    // ---------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (roleComboBox != null) {  // only runs on Hello.fxml
            ObservableList<String> roles = FXCollections.observableArrayList(
                    "Student",
                    "Teacher",
                    "Professional",
                    "Other"
            );

            roleComboBox.setItems(roles);
            roleComboBox.getSelectionModel().select("Student");
        }
    }

    // ---------------------------
    // Clear Name Field
    // ---------------------------
    @FXML
    protected void clearNameField() {
        if (nameField != null) {
            nameField.clear();
        }
    }

    // ---------------------------
    // Enter Button Action
    // ---------------------------
    @FXML
    protected void handleEnterButton() {
        if (nameField == null || roleComboBox == null) return;

        String name = nameField.getText();
        String role = roleComboBox.getSelectionModel().getSelectedItem();

        System.out.println("User Name: " + name);
        System.out.println("User Role: " + role);
    }
}
