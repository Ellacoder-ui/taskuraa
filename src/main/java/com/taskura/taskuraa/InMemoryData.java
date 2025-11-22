package com.taskura.taskuraa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class InMemoryData {
    public static final ObservableList<User> USERS = FXCollections.observableArrayList();
    public static final ObservableList<Task> TASKS = FXCollections.observableArrayList();

    static {
        // Sample tasks for demonstration. You can remove or replace these with your real app data.
        TASKS.add(new Task("t1", "Finish your tasks today", "Built-in reminder: wrap up your tasks before the day ends.", LocalDate.now(), false));
        TASKS.add(new Task("t2", "Finish Activity 1 today!", "Complete Activity 1 — this is your most urgent task.", LocalDate.now().plusDays(1), false));
        TASKS.add(new Task("t3", "Developing", "Continue development tasks and review PRs.", LocalDate.now().plusDays(4), false));
        TASKS.add(new Task("t4", "Project 1", "Draft project 1 documentation.", LocalDate.now().plusDays(7), false));
        TASKS.add(new Task("t5", "OOP Assignment", "Finish object-oriented assignment for school.", LocalDate.now().plusDays(3), true));
    }
}