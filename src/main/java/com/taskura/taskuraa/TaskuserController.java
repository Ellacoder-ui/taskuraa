package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskuserController {

    @FXML
    private Button homeButton;

    @FXML
    private Button calendarButton;

    @FXML
    private Button tasksButton;

    @FXML
    private TextField searchTasksField;

    @FXML
    private Button newButton;

    @FXML
    private VBox taskListContainer;

    @FXML
    private Label activityTitleLabel;

    @FXML
    private Label deadlineLabel;

    private final List<String> allTasks = new ArrayList<>();

    @FXML
    public void initialize() {
        // Pre-populate some tasks
        allTasks.add("Finish JavaFX project");
        allTasks.add("Write documentation");
        allTasks.add("Submit assignment");
        allTasks.add("Read design patterns");
        allTasks.add("Clean code review");

        refreshTaskList(allTasks);

        // Button event handlers
        homeButton.setOnAction(e -> System.out.println("Home button clicked."));
        calendarButton.setOnAction(e -> System.out.println("Calendar button clicked."));
        tasksButton.setOnAction(e -> System.out.println("Tasks button clicked."));
        newButton.setOnAction(e -> onNewTaskClicked());

        // Search filter
        searchTasksField.textProperty().addListener((obs, oldText, newText) -> filterTasks(newText));
    }

    private void refreshTaskList(List<String> tasks) {
        taskListContainer.getChildren().clear();
        for (String task : tasks) {
            Label taskLabel = new Label(task);
            taskLabel.setStyle("-fx-padding: 5; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
            taskLabel.setOnMouseClicked(e -> {
                activityTitleLabel.setText(task);
                deadlineLabel.setText("Deadline: (No deadline set)");
            });
            taskListContainer.getChildren().add(taskLabel);
        }
    }

    private void filterTasks(String search) {
        if (search == null || search.isBlank()) {
            refreshTaskList(allTasks);
        } else {
            List<String> filtered = allTasks.stream()
                    .filter(task -> task.toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
            refreshTaskList(filtered);
        }
    }

    private void onNewTaskClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Task");
        dialog.setHeaderText("Add a new task");
        dialog.setContentText("Task name:");

        dialog.showAndWait().ifPresent(taskName -> {
            if (!taskName.isBlank()) {
                allTasks.add(taskName);
                refreshTaskList(allTasks);
                System.out.println("Added new task: " + taskName);
            }
        });
    }
}
