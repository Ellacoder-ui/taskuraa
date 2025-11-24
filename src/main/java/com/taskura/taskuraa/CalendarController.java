package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        // Highlight the calendar button for this page
        setSelectedNavButton(calendarBtn);
    }

    /* -----------------------------------------------
     * NAVIGATION
     * --------------------------------------------- */

    @FXML
    private void onHome(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/taskura/taskuraa/dashboard.fxml")
            );
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
        // Already on the calendar page
        setSelectedNavButton(calendarBtn);
    }

    @FXML
    private void onTasks(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/taskura/taskuraa/taskpage.fxml")
            );
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

    /* -----------------------------------------------
     * STYLE HELPER: Highlight current nav button
     * --------------------------------------------- */
    private void setSelectedNavButton(Button selected) {
        String selectedStyle =
                "-fx-background-color: #eef2f6; -fx-background-radius:8; -fx-padding:8 12;";
        String normalStyle =
                "-fx-background-color: transparent; -fx-padding:8 12;";

        if (homeBtn != null) homeBtn.setStyle(normalStyle);
        if (calendarBtn != null) calendarBtn.setStyle(normalStyle);
        if (tasksBtn != null) tasksBtn.setStyle(normalStyle);

        if (selected != null) selected.setStyle(selectedStyle);
    }
}
