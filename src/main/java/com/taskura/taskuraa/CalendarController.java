package com.taskura.taskuraa;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.List;

public class CalendarController {

    @FXML
    private Button homeBtn;

    @FXML
    private Button calendarBtn;

    @FXML
    private Button tasksBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label leftNameLabel;

    @FXML
    private Label leftRoleLabel;

    @FXML
    private StackPane calendarContainer; // placeholder in FXML

    private User user;

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
        // Highlight current page nav button
        setSelectedNavButton(calendarBtn);

        // Create CalendarFX view
        CalendarView calendarView = new CalendarView();

        // Optional: hide add/print buttons
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);

        // Create a CalendarFX Calendar to hold entries
        Calendar taskCalendar = new Calendar("Tasks");

        // Load all tasks into the Calendar
        List<Task> tasks = InMemoryData.TASKS;
        for (Task t : tasks) {
            if (t.getDeadline() != null) {
                Entry<String> entry = new Entry<>(t.getTitle());
                entry.setInterval(t.getDeadline().atStartOfDay(ZoneId.systemDefault()).toLocalDate(),
                        t.getDeadline().atStartOfDay(ZoneId.systemDefault()).toLocalDate());
                entry.setUserObject(t.getId()); // optional, store the Task
                // set color based on tag
                switch (t.getTagColor()) {
                    case "red":
                        entry.setCalendar(taskCalendar);
                        taskCalendar.setStyle("-fx-background-color: #ff6b6b;");
                        break;
                    case "orange":
                        entry.setCalendar(taskCalendar);
                        taskCalendar.setStyle("-fx-background-color: #ffa94d;");
                        break;
                    default:
                        entry.setCalendar(taskCalendar);
                        taskCalendar.setStyle("-fx-background-color: #6bd26b;");
                        break;
                }
                taskCalendar.addEntry(entry);
            }
        }

        // Add the calendar to the CalendarView
        calendarView.getCalendarSources().add(new com.calendarfx.model.CalendarSource("My Tasks") {{
            getCalendars().add(taskCalendar);
        }});

        // Add CalendarView to the FXML container
        calendarContainer.getChildren().add(calendarView);
    }

    /* ------------------ NAVIGATION ------------------ */

    @FXML
    private void onHome(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/taskura/taskuraa/dashboard.fxml"));
            Parent root = loader.load();
            DashboardController ctrl = loader.getController();
            ctrl.setUser(this.user);
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Home");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onCalendar(ActionEvent e) {
        setSelectedNavButton(calendarBtn); // already on calendar
    }

    @FXML
    private void onTasks(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/taskura/taskuraa/taskpage.fxml"));
            Parent root = loader.load();
            TaskPageController ctrl = loader.getController();
            ctrl.setUser(this.user);
            Stage stage = (Stage) tasksBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Taskura - Tasks");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setSelectedNavButton(Button selected) {
        String selectedStyle = "-fx-background-color: #eef2f6; -fx-background-radius:8; -fx-padding:8 12;";
        String normalStyle = "-fx-background-color: transparent; -fx-padding:8 12;";
        if (homeBtn != null) homeBtn.setStyle(normalStyle);
        if (calendarBtn != null) calendarBtn.setStyle(normalStyle);
        if (tasksBtn != null) tasksBtn.setStyle(normalStyle);
        if (selected != null) selected.setStyle(selectedStyle);
    }
}