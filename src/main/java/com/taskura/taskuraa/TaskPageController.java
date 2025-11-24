package com.taskura.taskuraa;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

public class TaskPageController {

    @FXML private Label welcomeLabel;
    @FXML private Label leftNameLabel;
    @FXML private Label leftRoleLabel;

    @FXML private TextField searchField;
    @FXML private ListView<Task> taskListView;
    @FXML private Button addTaskBtn;
    @FXML private Button archivePageBtn;

    // VIEW mode labels (plain text preview)
    @FXML private Label viewTitleLabel;
    @FXML private Label viewDescLabel;
    @FXML private Label viewDeadlineLabel;

    // EDIT mode controls
    @FXML private TextField titleField;
    @FXML private TextArea descArea;
    @FXML private DatePicker deadlinePicker;
    @FXML private ChoiceBox<String> colorChoice;

    @FXML private Button saveBtn;
    @FXML private Button deleteBtn;
    @FXML private Button archiveBtn;
    @FXML private Button editBtn;
    @FXML private Label statusLabel;
    @FXML private Rectangle tagRect;
    @FXML private Label tagLabel;

    // Navigation buttons (styled like Home dashboard)
    @FXML private Button homeBtn;
    @FXML private Button calendarBtn;
    @FXML private Button tasksBtn;

    private FilteredList<Task> filteredTasks;
    private Task selectedTask = null;
    private User user;
    private boolean editing = false; // whether currently in edit mode (or creating)

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            welcomeLabel.setText("Hi, " + user.getUsername());
            leftNameLabel.setText(user.getUsername());
            leftRoleLabel.setText(user.getRole());
        }
    }

    @FXML
    public void initialize() {
        // color choices
        colorChoice.getItems().addAll("red", "orange", "green");
        colorChoice.setValue("green");

        // filtered list backed by InMemoryData.TASKS
        filteredTasks = new FilteredList<>(InMemoryData.TASKS, p -> true);
        taskListView.setItems(filteredTasks);

        // build custom cell factory so each row has delete/archive icons
        taskListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox root = new HBox(10);
                    root.setStyle("-fx-padding:8; -fx-alignment:center-left;");
                    Circle tag = new Circle(8);
                    switch (item.getTagColor()) {
                        case "red": tag.setFill(Color.web("#ff6b6b")); break;
                        case "orange": tag.setFill(Color.web("#ffa94d")); break;
                        default: tag.setFill(Color.web("#6bd26b")); break;
                    }

                    VBox texts = new VBox(2);
                    Label title = new Label(item.getTitle());
                    title.setStyle("-fx-font-weight:600;");
                    Label small = new Label(item.getDescription() == null ? "" : (item.getDescription().length() > 40 ? item.getDescription().substring(0, 40) + "..." : item.getDescription()));
                    small.setStyle("-fx-font-size:11px; -fx-text-fill:#666666;");
                    texts.getChildren().addAll(title, small);

                    // action buttons
                    Button del = new Button("🗑");
                    del.setStyle("-fx-background-color: transparent;");
                    del.setOnAction(ev -> {
                        ev.consume();
                        deleteTask(item);
                    });

                    Button arc = new Button("📦");
                    arc.setStyle("-fx-background-color: transparent;");
                    arc.setOnAction(ev -> {
                        ev.consume();
                        archiveTask(item);
                    });

                    // clicking anywhere on the row will select and load details
                    root.getChildren().addAll(tag, texts, new HBox(), del, arc);
                    HBox.setHgrow(root.getChildren().get(2), javafx.scene.layout.Priority.ALWAYS);
                    setGraphic(root);
                }
            }
        });

        // selection listener
        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            selectedTask = newV;
            enterViewMode(); // always show preview (non-editable) on selection
            loadSelectedTask();
        });

        // search/filter
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            filteredTasks.setPredicate(task -> {
                if (q.isEmpty()) return true;
                return (task.getTitle() != null && task.getTitle().toLowerCase().contains(q))
                        || (task.getDescription() != null && task.getDescription().toLowerCase().contains(q));
            });
            if (!filteredTasks.isEmpty()) {
                taskListView.getSelectionModel().select(filteredTasks.get(0));
            } else {
                clearDetailFields();
            }
        });

        // default selected nav for this page
        setSelectedNavButton(tasksBtn);

        // pre-select first
        if (!InMemoryData.TASKS.isEmpty()) {
            taskListView.getSelectionModel().select(0);
        } else {
            clearDetailFields();
        }

        // initial UI state: not editing, hide save, show view labels
        enterViewMode();
    }

    private void loadSelectedTask() {
        if (selectedTask == null) {
            clearDetailFields();
            return;
        }
        // populate view labels
        viewTitleLabel.setText(selectedTask.getTitle());
        viewDescLabel.setText(selectedTask.getDescription() == null ? "" : selectedTask.getDescription());
        viewDeadlineLabel.setText(selectedTask.getDeadline() != null ? selectedTask.getDeadline().format(DateTimeFormatter.ofPattern("d MMM yyyy")) : "No deadline");

        // also populate edit controls so toggling to edit immediately shows correct values
        titleField.setText(selectedTask.getTitle());
        descArea.setText(selectedTask.getDescription());
        deadlinePicker.setValue(selectedTask.getDeadline());
        colorChoice.setValue(selectedTask.getTagColor());
        updateTagPreview(selectedTask.getTagColor());
        statusLabel.setText("Deadline: " + (selectedTask.getDeadline() != null ? selectedTask.getDeadline().format(DateTimeFormatter.ofPattern("d MMM yyyy")) : "Not set"));
    }

    private void clearDetailFields() {
        selectedTask = null;
        viewTitleLabel.setText("");
        viewDescLabel.setText("");
        viewDeadlineLabel.setText("");
        titleField.setText("");
        descArea.setText("");
        deadlinePicker.setValue(null);
        colorChoice.setValue("green");
        updateTagPreview("green");
        statusLabel.setText("");
    }

    private void updateTagPreview(String tag) {
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

    // Enter editing mode: enable fields and show Save button
    @FXML
    private void onEdit(ActionEvent e) {
        // if no selected task, treat as "create new" (we still enable editing)
        if (selectedTask == null) {
            // create mode
            clearDetailFields();
        }
        editing = true;
        setEditingState(true);
        statusLabel.setText("Editing... make changes and click Save.");
    }

    // Save handler: creates or updates task, then go back to view mode and hide Save
    @FXML
    private void onSave(ActionEvent e) {
        String title = titleField.getText() == null ? "" : titleField.getText().trim();
        String desc = descArea.getText();
        LocalDate dl = deadlinePicker.getValue();
        String color = colorChoice.getValue();

        if (title.isEmpty()) {
            statusLabel.setText("Title is required.");
            return;
        }

        if (selectedTask == null) {
            // create new
            String id = UUID.randomUUID().toString();
            Task t = new Task(id, title, desc, dl, false, color);
            InMemoryData.TASKS.add(0, t);
            taskListView.getSelectionModel().select(t);
            statusLabel.setText("Task created.");
        } else {
            // update existing
            selectedTask.setTitle(title);
            selectedTask.setDescription(desc);
            selectedTask.setDeadline(dl);
            selectedTask.setTag(color);
            taskListView.refresh();
            statusLabel.setText("Task saved.");
        }

        // exit editing mode
        editing = false;
        setEditingState(false);
        // update preview tag and labels
        updateTagPreview(color);
        loadSelectedTask();
    }

    // Delete selected task (also from the preview Delete button)
    @FXML
    private void onDelete(ActionEvent e) {
        deleteTask(selectedTask);
    }

    private void deleteTask(Task task) {
        if (task == null) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Delete Task");
        a.setHeaderText("Delete \"" + task.getTitle() + "\"?");
        a.setContentText("This action cannot be undone.");
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            InMemoryData.TASKS.remove(task);
            if (task.equals(selectedTask)) clearDetailFields();
            statusLabel.setText("Task deleted.");
        }
    }

    // Archive selected task
    @FXML
    private void onArchive(ActionEvent e) {
        archiveTask(selectedTask);
    }

    private void archiveTask(Task task) {
        if (task == null) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Archive Task");
        a.setHeaderText("Archive \"" + task.getTitle() + "\"?");
        a.setContentText("Archived tasks can be viewed in Archives.");
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            InMemoryData.TASKS.remove(task);
            InMemoryData.ARCHIVES.add(0, task);
            if (task.equals(selectedTask)) clearDetailFields();
            statusLabel.setText("Task archived.");
        }
    }

    @FXML
    private void onAddTask(ActionEvent e) {
        // prepare the form for creating a new task
        taskListView.getSelectionModel().clearSelection();
        clearDetailFields();
        editing = true;
        setEditingState(true);
        statusLabel.setText("Creating new task. Fill fields and click Save.");
    }

    @FXML
    private void openArchives(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/taskura/taskuraa/archive.fxml"));
            Parent root = loader.load();
            ArchiveController ctrl = loader.getController();
            ctrl.setUser(this.user);
            Stage stage = (Stage) addTaskBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Archives");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goHome(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/taskura/taskuraa/dashboard.fxml"));
            Parent root = loader.load();
            DashboardController ctrl = loader.getController();
            ctrl.setUser(this.user);
            Stage stage = (Stage) addTaskBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Home");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goCalendar() {
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
        // remain on tasks page but set selected nav style and focus list
        setSelectedNavButton(tasksBtn);
        taskListView.requestFocus();
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

    // UI state helpers

    /**
     * Enter non-edit/view mode: show plain text labels, hide edit controls and Save button.
     */
    private void enterViewMode() {
        editing = false;
        // show view labels
        viewTitleLabel.setVisible(true);
        viewTitleLabel.setManaged(true);
        viewDescLabel.setVisible(true);
        viewDescLabel.setManaged(true);
        viewDeadlineLabel.setVisible(true);
        viewDeadlineLabel.setManaged(true);

        // hide edit controls
        titleField.setVisible(false);
        titleField.setManaged(false);
        descArea.setVisible(false);
        descArea.setManaged(false);
        deadlinePicker.setVisible(false);
        deadlinePicker.setManaged(false);
        colorChoice.setVisible(false);
        colorChoice.setManaged(false);

        // Save button hidden
        saveBtn.setVisible(false);
        saveBtn.setManaged(false);

        // Edit button enabled if a task is selected (or enabled for create)
        editBtn.setDisable(false);

        // delete/archive enabled
        deleteBtn.setDisable(false);
        archiveBtn.setDisable(false);
    }

    /**
     * Enable or disable editing UI (shows edit controls and Save button when on=true).
     */
    private void setEditingState(boolean on) {
        editing = on;

        // view labels hidden while editing
        viewTitleLabel.setVisible(!on);
        viewTitleLabel.setManaged(!on);
        viewDescLabel.setVisible(!on);
        viewDescLabel.setManaged(!on);
        viewDeadlineLabel.setVisible(!on);
        viewDeadlineLabel.setManaged(!on);

        // edit controls shown/hidden
        titleField.setVisible(on);
        titleField.setManaged(on);
        descArea.setVisible(on);
        descArea.setManaged(on);
        deadlinePicker.setVisible(on);
        deadlinePicker.setManaged(on);
        colorChoice.setVisible(on);
        colorChoice.setManaged(on);

        // Save button visible only in edit mode
        saveBtn.setVisible(on);
        saveBtn.setManaged(on);

        // disable edit button while editing
        editBtn.setDisable(on);

        // prevent delete/archive while editing
        deleteBtn.setDisable(on);
        archiveBtn.setDisable(on);
    }
}