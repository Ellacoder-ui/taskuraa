package com.taskura.taskuraa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> roleComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> roles = FXCollections.observableArrayList(
                "Student",
                "Teacher",
                "Professional",
                "Other"
        );
        roleComboBox.setItems(roles);
        roleComboBox.getSelectionModel().select("Student");
    }

    @FXML
    protected void handleEnterButton() {
        try {
            // Load homepage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();

            // Switch window scene
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
