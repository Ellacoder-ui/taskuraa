    package com.taskura.taskuraa;

    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.stage.Stage;

    import java.io.IOException;

    public class HelloApplication extends Application {

        @Override
        public void start(Stage stage) throws IOException {
            // Make sure the path and filename are correct and match the FXML location
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HelloApplication.class.getResource("task-user.fxml")
            );
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 1000, 700);
            stage.setTitle("Taskura");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }
