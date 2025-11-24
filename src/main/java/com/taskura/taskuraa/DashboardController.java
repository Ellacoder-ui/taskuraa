package com.taskura.taskuraa;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ListView<Task> taskListView;

    @FXML
    private TextField searchField;

    // detail panel fields
    @FXML
    private Label detailTitle;

    @FXML
    private Label detailDesc;

    @FXML
    private Label detailDeadline;

    @FXML
    private Rectangle tagRect;

    // dynamic tag label (textual description of urgency)
    @FXML
    private Label tagLabel;

    @FXML
    private Label completedCountLabel;

    // left-side profile labels
    @FXML
    private Label leftNameLabel;

    @FXML
    private Label leftRoleLabel;

    // navigation buttons
    @FXML
    private Button homeBtn;

    @FXML
    private Button calendarBtn;

    @FXML
    private Button tasksBtn;

    @FXML
    private VBox topCardUrgentTitle; // placeholder if needed

    private FilteredList<Task> filteredTasks;

    private User user;

    public void setUser(User user) {
        this.user = user;
        String uname = user != null && user.getUsername() != null && !user.getUsername().isEmpty()
                ? user.getUsername() : "User";
        String urole = user != null && user.getRole() != null ? user.getRole() : "";

        welcomeLabel.setText("Hi, " + uname);
        if (leftNameLabel != null) leftNameLabel.setText(uname);
        if (leftRoleLabel != null) leftRoleLabel.setText(urole);
    }

    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
        filteredTasks = new FilteredList<>(InMemoryData.TASKS, p -> true);
        taskListView.setItems(filteredTasks);
        taskListView.setCellFactory(list -> new TaskCell());

        // show first by default
        if (!InMemoryData.TASKS.isEmpty()) {
            taskListView.getSelectionModel().select(0);
            showTaskInDetail(InMemoryData.TASKS.get(0));
        } else {
            clearDetails();
        }

        // react to selection
        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) showTaskInDetail(newV);
        });

        // search filter
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            filteredTasks.setPredicate(task -> {
                if (q.isEmpty()) return true;
                return (task.getTitle() != null && task.getTitle().toLowerCase().contains(q)) ||
                        (task.getDescription() != null && task.getDescription().toLowerCase().contains(q));
            });
            // auto-select first filtered item
            if (!filteredTasks.isEmpty()) {
                taskListView.getSelectionModel().select(filteredTasks.get(0));
            } else {
                clearDetails();
            }
        });
    }

    // Navigation button actions

    @FXML
    private void onHome(ActionEvent e) {
        setSelectedNavButton(homeBtn);
        if (!filteredTasks.isEmpty()) {
            taskListView.getSelectionModel().select(0);
            taskListView.requestFocus();
        }
    }

    @FXML
    private void onCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/taskura/taskuraa/Calendar.fxml")
            );
            Parent root = loader.load();

            CalendarController ctrl = loader.getController();
            ctrl.setUser(this.user);

            Stage stage = (Stage) calendarBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Calendar");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onTasks(ActionEvent e) {
        // navigate to Task page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/taskura/taskuraa/taskpage.fxml"));
            Parent root = loader.load();
            TaskPageController ctrl = loader.getController();
            ctrl.setUser(this.user);
            Stage stage = (Stage) taskListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Tasks");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // helper to visually mark which nav button is active
    private void setSelectedNavButton(Button selected) {
        String selectedStyle = "-fx-background-color: #eef2f6; -fx-background-radius:8; -fx-padding:8 12;";
        String normalStyle = "-fx-background-color: transparent; -fx-padding:8 12;";

        if (homeBtn != null) homeBtn.setStyle(normalStyle);
        if (calendarBtn != null) calendarBtn.setStyle(normalStyle);
        if (tasksBtn != null) tasksBtn.setStyle(normalStyle);

        if (selected != null) selected.setStyle(selectedStyle);
    }

    private void showTaskInDetail(Task task) {
        detailTitle.setText(task.getTitle());
        detailDesc.setText(task.getDescription());
        detailDesc.setWrapText(true);
        detailDeadline.setText("Deadline: " + (task.getDeadline() != null ? task.getDeadline().format(DateTimeFormatter.ofPattern("d MMM yyyy")) : ""));
        String tag = task.getTagColor();
        switch (tag) {
            case "red":
                tagRect.setStyle("-fx-fill: #ff6b6b; -fx-arc-width:8; -fx-arc-height:8;");
                tagLabel.setText("urgent");
                break;
            case "orange":
                tagRect.setStyle("-fx-fill: #ffa94d; -fx-arc-width:8; -fx-arc-height:8;");
                tagLabel.setText("not so urgent");
                break;
            default:
                tagRect.setStyle("-fx-fill: #6bd26b; -fx-arc-width:8; -fx-arc-height:8;");
                tagLabel.setText("not urgent");
                break;
        }
    }

    private void clearDetails() {
        detailTitle.setText("");
        detailDesc.setText("");
        detailDeadline.setText("");
        tagRect.setStyle("-fx-fill: transparent;");
        if (tagLabel != null) tagLabel.setText("");
    }
}