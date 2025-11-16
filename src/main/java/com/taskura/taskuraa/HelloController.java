package com.taskura.taskuraa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController{

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

    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("HelloController initialized successfully.");
    }


    @FXML
    protected void onHomeButtonClicked() {
        System.out.println("Home button clicked.");
    }

    @FXML
    protected void onTasksButtonClicked() {
        System.out.println("Tasks button clicked.");
    }

    @FXML
    protected void onNewTaskClicked() {
        System.out.println("'+ New' button clicked.");
    }

    @FXML
    protected void onAddMoreTasksClicked() {
        System.out.println("'+ Add more tasks' button clicked.");
    }
}