package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class DashboardController {

    @FXML
    private Button newButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private VBox dateContainer; // 🔵 new: container for selected date boxes

    @FXML
    public void initialize() {
        System.out.println("Dashboard loaded.");
    }

    // 🔵 This gets called when a date is clicked in the calendar
    public void setSelectedDate(LocalDate date) {
        Label box = new Label("Selected date: " + date);
        box.setStyle(
                "-fx-background-color: #F7F5FB; " +
                        "-fx-padding: 15; " +
                        "-fx-background-radius: 10; " +
                        "-fx-font-size: 16;"
        );
        dateContainer.getChildren().add(box);
    }

    @FXML
    private void handleNewButton() {
        System.out.println("+ New clicked!");
    }

    @FXML
    private void handleAddAppointment() {
        System.out.println("Add appointment clicked!");
    }
}
