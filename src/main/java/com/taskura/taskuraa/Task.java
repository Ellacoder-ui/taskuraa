package com.taskura.taskuraa;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Task model. Now supports an explicit tag (red/orange/green) that can be set by the user.
 * If tag is null, getTagColor() falls back to computing color from the deadline.
 */
public class Task {
    private String id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean completed;
    private String tag; // "red", "orange", "green" or null

    public Task() {}

    public Task(String id, String title, String description, LocalDate deadline, boolean completed, String tag) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.tag = tag;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public boolean isCompleted() { return completed; }
    public String getTag() { return tag; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setTag(String tag) { this.tag = tag; }

    /**
     * Returns a tag string color name: "red", "orange", or "green".
     * If explicit user-set tag exists, return it. Otherwise compute from deadline.
     */
    public String getTagColor() {
        if (tag != null && (tag.equals("red") || tag.equals("orange") || tag.equals("green"))) {
            return tag;
        }
        if (deadline == null) return "green";
        long days = ChronoUnit.DAYS.between(java.time.LocalDate.now(), deadline);
        if (days <= 1) return "red";
        if (days <= 3) return "orange";
        return "green";
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}