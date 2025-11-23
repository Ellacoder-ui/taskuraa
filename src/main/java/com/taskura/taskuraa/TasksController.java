package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TasksController {

    @FXML private Label nameLabel;
    @FXML private Button homeButton;
    @FXML private Button tasksButton;

    @FXML private VBox taskListContainer;
    @FXML private Label activityTitleLabel;
    @FXML private TextArea deadlineLabel;
    @FXML private Button newButton;
    @FXML private Button archiveButton;
    @FXML private TextField searchTasksField;

    private String username;
    private final List<Task> allTasks = TaskStorage.activeTasks;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setUsername(String username) {
        this.username = username;
        nameLabel.setText(username);
    }

    @FXML
    public void initialize() {

        // Sidebar navigation
        homeButton.setOnAction(e -> switchToHome());
        tasksButton.setOnAction(e -> { /* Already here */ });

        // Load list
        refreshTaskList(allTasks);

        // New task button
        newButton.setOnAction(e -> onNewTaskClicked());

        archiveButton.setOnAction(e -> switchToArchive());

        // Search filter
        searchTasksField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterTasks(newVal);
        });

        // Setup right pane
        deadlineLabel.setEditable(false);
        deadlineLabel.setWrapText(true);
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

    private void switchToArchive() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Archive.fxml"));
            Parent root = loader.load();

            ArchiveController controller = loader.getController();
            controller.setUsername(username);

            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void refreshTaskList(List<Task> tasks) {
        taskListContainer.getChildren().clear();
        for (Task task : tasks) {

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
            Button updateBtn = new Button("Update");
            Button deleteBtn = new Button("Delete");
            buttons.getChildren().addAll(updateBtn, deleteBtn);

            taskBox.getChildren().addAll(name, desc, buttons);

            // Right panel details
            taskBox.setOnMouseClicked(e -> {
                activityTitleLabel.setText(task.getName());
                deadlineLabel.setText(
                        "Description:\n" + task.getDescription() +
                                "\n\nDeadline:\n" + (task.getDeadline().isBlank() ? "No deadline" : task.getDeadline())
                );
            });

            // Delete
            deleteBtn.setOnAction(e -> {
                allTasks.remove(task);
                TaskStorage.archivedTasks.add(task);
                refreshTaskList(allTasks);
            });

            // Update
            updateBtn.setOnAction(e -> onUpdateTaskClicked(task));

            taskListContainer.getChildren().add(taskBox);
        }
    }

    private void filterTasks(String text) {
        if (text == null || text.isBlank()) {
            refreshTaskList(allTasks);
            return;
        }

        List<Task> filtered = allTasks.stream()
                .filter(t -> t.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());

        refreshTaskList(filtered);
    }

    private void onNewTaskClicked() {

        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("New Task");
        dialog.setHeaderText("Add a new task");

        ButtonType addType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextArea descField = new TextArea();
        descField.setWrapText(true);
        DatePicker deadlinePicker = new DatePicker();

        VBox box = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Description:"), descField,
                new Label("Deadline:"), deadlinePicker
        );

        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(btn -> {
            if (btn == addType) {
                String deadline = (deadlinePicker.getValue() == null) ?
                        "" : deadlinePicker.getValue().format(formatter);

                return new Task(
                        nameField.getText().trim(),
                        descField.getText().trim(),
                        deadline
                );
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

        ButtonType updateType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateType, ButtonType.CANCEL);

        TextField nameField = new TextField(task.getName());
        TextArea descArea = new TextArea(task.getDescription());
        descArea.setWrapText(true);

        DatePicker picker = new DatePicker();
        if (!task.getDeadline().isBlank()) {
            picker.setValue(java.time.LocalDate.parse(task.getDeadline(), formatter));
        }

        VBox box = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Description:"), descArea,
                new Label("Deadline:"), picker
        );

        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(btn -> {
            if (btn == updateType) {
                String d = picker.getValue() == null ? "" : picker.getValue().format(formatter);
                return new Task(nameField.getText(), descArea.getText(), d);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            task.setName(updated.getName());
            task.setDescription(updated.getDescription());
            task.setDeadline(updated.getDeadline());
            refreshTaskList(allTasks);
        });
    }

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
        public String getDescription() { return description; }
        public String getDeadline() { return deadline; }

        public void setName(String n) { name = n; }
        public void setDescription(String d) { description = d; }
        public void setDeadline(String d) { deadline = d; }
    }
}