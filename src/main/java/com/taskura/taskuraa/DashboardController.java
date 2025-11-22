package com.taskura.taskuraa;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ListView<Task> taskListView;

    @FXML
    private TextField searchField;

    // changed to Label (wrapping) instead of Text
    @FXML
    private Label detailTitle;

    @FXML
    private Label detailDesc;

    @FXML
    private Label detailDeadline;

    @FXML
    private Rectangle tagRect;

    @FXML
    private Label completedCountLabel;

    @FXML
    private VBox topCardUrgentTitle; // placeholder if needed

    private FilteredList<Task> filteredTasks;

    private User user;

    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText("Hi, " + user.getUsername());
    }

    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
        // prepare filtered list
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
            updateProgress(); // optional
            // auto-select first filtered item
            if (!filteredTasks.isEmpty()) {
                taskListView.getSelectionModel().select(filteredTasks.get(0));
            } else {
                clearDetails();
            }
        });

        updateProgress();
    }

    private void showTaskInDetail(Task task) {
        detailTitle.setText(task.getTitle());
        detailDesc.setText(task.getDescription());
        detailDesc.setWrapText(true);
        detailDeadline.setText("Deadline: " + task.getDeadline().format(DateTimeFormatter.ofPattern("d MMM yyyy")));
        switch (task.getTagColor()) {
            case "red": tagRect.setStyle("-fx-fill: #ff6b6b; -fx-arc-width:8; -fx-arc-height:8;"); break;
            case "orange": tagRect.setStyle("-fx-fill: #ffa94d; -fx-arc-width:8; -fx-arc-height:8;"); break;
            default: tagRect.setStyle("-fx-fill: #6bd26b; -fx-arc-width:8; -fx-arc-height:8;"); break;
        }
    }

    private void clearDetails() {
        detailTitle.setText("");
        detailDesc.setText("");
        detailDeadline.setText("");
        tagRect.setStyle("-fx-fill: transparent;");
    }

    private void updateProgress() {
        AtomicInteger completed = new AtomicInteger();
        InMemoryData.TASKS.forEach(t -> { if (t.isCompleted()) completed.getAndIncrement(); });
        int total = InMemoryData.TASKS.size() == 0 ? 1 : InMemoryData.TASKS.size();
        int completedCount = completed.get();
        completedCountLabel.setText(String.format("%d of %d completed", completedCount, InMemoryData.TASKS.size()));
        double prog = (double) completedCount / total;
        progressBar.setProgress(prog);
        progressLabel.setText(String.format("%d of %d completed", completedCount, InMemoryData.TASKS.size()));
    }
}