package com.taskura.taskuraa;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Task {
    private String id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean completed;

    public Task() {}

    public Task(String id, String title, String description, LocalDate deadline, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    /**
     * Returns a tag string color name: "red", "orange", or "green".
     * Thresholds:
     * - red: deadline within 1 day (<=1)
     * - orange: deadline within 3 days (<=3)
     * - green: more than 3 days away
     * Overdue tasks are considered red.
     */
    public String getTagColor() {
        if (deadline == null) return "green";
        long days = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        if (days <= 1) return "red";
        if (days <= 3) return "orange";
        return "green";
    }

    @Override
    public String toString() {
        return title;
    }
}