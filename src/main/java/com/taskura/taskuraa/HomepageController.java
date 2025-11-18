package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {
    @FXML
    private Button taskBtn;


    @FXML
    private void handleTaskClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("task-user.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) taskBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
