package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ArchiveController {

    @FXML private Label nameLabel;
    @FXML private Button homeButton;
    @FXML private Button tasksButton;
    @FXML private VBox archiveListContainer;
    @FXML private Label activityTitleLabel;
    @FXML private TextArea deadlineLabel;
    @FXML private Button restoreAllButton;
    @FXML private Button deleteAllButton;
    @FXML private TextField searchTasksField; // new search field

    private String username;

    public void setUsername(String username) {
        this.username = username;
        nameLabel.setText(username);
    }

    @FXML
    public void initialize() {
        // Sidebar buttons
        homeButton.setOnAction(e -> switchToHome());
        tasksButton.setOnAction(e -> switchToTasks());

        // Restore all / Delete all
        restoreAllButton.setOnAction(e -> {
            TaskStorage.activeTasks.addAll(TaskStorage.archivedTasks);
            TaskStorage.archivedTasks.clear();
            loadArchivedTasks();
        });

        deleteAllButton.setOnAction(e -> {
            TaskStorage.archivedTasks.clear();
            loadArchivedTasks();
        });

        // Search filter
        searchTasksField.textProperty().addListener((obs, oldVal, newVal) -> filterArchivedTasks(newVal));

        loadArchivedTasks();
    }

    private void switchToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setUsername(username);
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void switchToTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tasks.fxml"));
            Parent root = loader.load();
            TasksController controller = loader.getController();
            controller.setUsername(username);
            Stage stage = (Stage) tasksButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadArchivedTasks() {
        archiveListContainer.getChildren().clear();

        for (TasksController.Task task : TaskStorage.archivedTasks) {
            VBox taskBox = new VBox(5);
            taskBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
            taskBox.setMaxWidth(Double.MAX_VALUE);

            Label name = new Label(task.getName());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label desc = new Label(task.getDescription());
            desc.setWrapText(false);
            desc.setMaxWidth(Double.MAX_VALUE);
            desc.setEllipsisString("...");
            desc.setTextOverrun(OverrunStyle.ELLIPSIS);
            desc.setStyle("-fx-font-size: 12;");

            HBox buttons = new HBox(10);
            Button deleteBtn = new Button("Delete");
            Button restoreBtn = new Button("Restore");
            buttons.getChildren().addAll(restoreBtn, deleteBtn);

            taskBox.getChildren().addAll(name, desc, buttons);

            // Right panel details
            taskBox.setOnMouseClicked(e -> {
                activityTitleLabel.setText(task.getName());
                deadlineLabel.setText(
                        "Description:\n" + task.getDescription() +
                                "\n\nDeadline:\n" + (task.getDeadline().isBlank() ? "No deadline" : task.getDeadline())
                );
            });

            restoreBtn.setOnAction(e -> {
                TaskStorage.archivedTasks.remove(task);
                TaskStorage.activeTasks.add(task);
                loadArchivedTasks();
            });

            deleteBtn.setOnAction(e -> {
                TaskStorage.archivedTasks.remove(task);
                loadArchivedTasks();
            });

            archiveListContainer.getChildren().add(taskBox);
        }
    }

    private void filterArchivedTasks(String text) {
        if (text == null || text.isBlank()) {
            loadArchivedTasks();
            return;
        }

        List<TasksController.Task> filtered = TaskStorage.archivedTasks.stream()
                .filter(t -> t.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());

        archiveListContainer.getChildren().clear();
        for (TasksController.Task task : filtered) {
            VBox taskBox = new VBox(5);
            taskBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
            taskBox.setMaxWidth(Double.MAX_VALUE);

            Label name = new Label(task.getName());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label desc = new Label(task.getDescription());
            desc.setWrapText(false);
            desc.setMaxWidth(Double.MAX_VALUE);
            desc.setEllipsisString("...");
            desc.setTextOverrun(OverrunStyle.ELLIPSIS);
            desc.setStyle("-fx-font-size: 12;");

            HBox buttons = new HBox(10);
            Button deleteBtn = new Button("Delete");
            Button restoreBtn = new Button("Restore");
            buttons.getChildren().addAll(restoreBtn, deleteBtn);

            taskBox.getChildren().addAll(name, desc, buttons);

            taskBox.setOnMouseClicked(e -> {
                activityTitleLabel.setText(task.getName());
                deadlineLabel.setText(
                        "Description:\n" + task.getDescription() +
                                "\n\nDeadline:\n" + (task.getDeadline().isBlank() ? "No deadline" : task.getDeadline())
                );
            });

            restoreBtn.setOnAction(e -> {
                TaskStorage.archivedTasks.remove(task);
                TaskStorage.activeTasks.add(task);
                filterArchivedTasks(searchTasksField.getText());
            });

            deleteBtn.setOnAction(e -> {
                TaskStorage.archivedTasks.remove(task);
                filterArchivedTasks(searchTasksField.getText());
            });

            archiveListContainer.getChildren().add(taskBox);
        }
    }
}