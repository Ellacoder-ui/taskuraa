package com.taskura.taskuraa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            VBox loginRoot = loader.load();
            LoginController loginController = loader.getController();
            loginController.setStage(primaryStage);

            Scene scene = new Scene(loginRoot, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Taskura - Login");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
