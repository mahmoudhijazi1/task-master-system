package com.taskmaster.Models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Task {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty projectId = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    private final IntegerProperty assignedBy = new SimpleIntegerProperty();
    private final StringProperty assignedByName = new SimpleStringProperty();
    private final StringProperty developer = new SimpleStringProperty();

    public Task() {}

    public Task(int id, int projectId, String title, String description, String status, LocalDate startDate, LocalDate endDate, int assignedBy) {
        this.id.set(id);
        this.projectId.set(projectId);
        this.title.set(title);
        this.description.set(description);
        this.status.set(status);
        this.startDate.set(startDate);
        this.endDate.set(endDate);
        this.assignedBy.set(assignedBy);
    }

    public Task(String title, int id, int projectId, String description, String status, LocalDate startDate, LocalDate endDate, String assignedByName) {
        this.title.set(title);
        this.id.set(id);
        this.projectId.set(projectId);
        this.description.set(description);
        this.status.set(status);
        this.startDate.set(startDate);
        this.endDate.set(endDate);
        this.assignedByName.set(assignedByName);
    }

    // Getters and Setters for IntegerProperties
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getProjectId() {
        return projectId.get();
    }

    public void setProjectId(int projectId) {
        this.projectId.set(projectId);
    }

    public IntegerProperty projectIdProperty() {
        return projectId;
    }

    public int getAssignedBy() {
        return assignedBy.get();
    }

    public void setAssignedBy(int assignedBy) {
        this.assignedBy.set(assignedBy);
    }

    public IntegerProperty assignedByProperty() {
        return assignedBy;
    }

    // Getters and Setters for StringProperties
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getAssignedByName() {
        return assignedByName.get();
    }

    public void setAssignedByName(String assignedByName) {
        this.assignedByName.set(assignedByName);
    }

    public StringProperty assignedByNameProperty() {
        return assignedByName;
    }

    public String getDeveloper() {
        return developer.get();
    }

    public void setDeveloper(String developer) {
        this.developer.set(developer);
    }

    public StringProperty developerProperty() {
        return developer;
    }

    // Getters and Setters for ObjectProperties
    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }
}
