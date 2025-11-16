package com.taskura;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AppointmentController {

    @FXML
    private Button newButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    public void initialize() {
        System.out.println("Appointment Panel Loaded Successfully!");
    }

    @FXML
    private void handleNewButton() {
        System.out.println("+ New clicked!");
        // Add new schedule creation logic here
    }

    @FXML
    private void handleAddAppointment() {
        System.out.println("Add appointment clicked!");
        // Add appointment logic here
    }
}
