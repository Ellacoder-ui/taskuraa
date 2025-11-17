package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
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
    private TextArea deadlineLabel; // Display description + deadline

    private final List<Task> allTasks = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        refreshTaskList(allTasks);

        homeButton.setOnAction(e -> System.out.println("Home button clicked."));
        calendarButton.setOnAction(e -> System.out.println("Calendar button clicked."));
        tasksButton.setOnAction(e -> System.out.println("Tasks button clicked."));
        newButton.setOnAction(e -> onNewTaskClicked());

        searchTasksField.textProperty().addListener((obs, oldText, newText) -> filterTasks(newText));
    }

    private void refreshTaskList(List<Task> tasks) {
        taskListContainer.getChildren().clear();

        for (Task task : tasks) {
            VBox taskBox = new VBox(5);
            taskBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

            Label taskName = new Label(task.getName());
            taskName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label taskDesc = new Label(task.getDescription());
            taskDesc.setWrapText(true);

            // Buttons below task info
            Button updateBtn = new Button("Update");
            Button deleteBtn = new Button("Delete");
            HBox buttonsBox = new HBox(10, updateBtn, deleteBtn);

            taskBox.getChildren().addAll(taskName, taskDesc, buttonsBox);

            // Click task to show details in TextArea
            taskBox.setOnMouseClicked(e -> {
                activityTitleLabel.setText(task.getName());
                String displayText = "Description:\n" + task.getDescription() +
                        "\n\nDeadline:\n" + (task.getDeadline().isBlank() ? "No deadline set" : task.getDeadline());
                deadlineLabel.setText(displayText);
            });

            // Delete task
            deleteBtn.setOnAction(e -> {
                allTasks.remove(task);
                refreshTaskList(allTasks);
            });

            // Update task
            updateBtn.setOnAction(e -> onUpdateTaskClicked(task));

            taskListContainer.getChildren().add(taskBox);
        }
    }

    private void filterTasks(String search) {
        if (search == null || search.isBlank()) {
            refreshTaskList(allTasks);
        } else {
            List<Task> filtered = allTasks.stream()
                    .filter(task -> task.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
            refreshTaskList(filtered);
        }
    }

    private void onNewTaskClicked() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("New Task");
        dialog.setHeaderText("Add a new task");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Task name");

        TextArea descArea = new TextArea();
        descArea.setPromptText("Task description");
        descArea.setWrapText(true);

        DatePicker deadlinePicker = new DatePicker();

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Description:"), descArea,
                new Label("Deadline:"), deadlinePicker
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String deadlineStr = (deadlinePicker.getValue() != null) ? deadlinePicker.getValue().format(formatter) : "";
                return new Task(nameField.getText().trim(), descArea.getText().trim(), deadlineStr);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(task -> {
            allTasks.add(task);
            refreshTaskList(allTasks);
        });
    }

    private void onUpdateTaskClicked(Task task) {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Update Task");
        dialog.setHeaderText("Update the task");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(task.getName());
        TextArea descArea = new TextArea(task.getDescription());
        descArea.setWrapText(true);

        DatePicker deadlinePicker = new DatePicker();
        if (!task.getDeadline().isBlank()) {
            deadlinePicker.setValue(java.time.LocalDate.parse(task.getDeadline(), formatter));
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Description:"), descArea,
                new Label("Deadline:"), deadlinePicker
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                String deadlineStr = (deadlinePicker.getValue() != null) ? deadlinePicker.getValue().format(formatter) : "";
                return new Task(nameField.getText().trim(), descArea.getText().trim(), deadlineStr);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedTask -> {
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            task.setDeadline(updatedTask.getDeadline());
            refreshTaskList(allTasks);
        });
    }

    // Inner Task class
    public static class Task {
        private String name;
        private String description;
        private String deadline;

        public Task(String name, String description, String deadline) {
            this.name = name;
            this.description = description;
            this.deadline = deadline;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getDeadline() { return deadline; }
        public void setDeadline(String deadline) { this.deadline = deadline; }
    }
}
