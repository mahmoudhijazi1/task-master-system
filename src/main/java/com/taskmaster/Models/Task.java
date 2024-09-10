package com.taskmaster.Models;

import java.time.LocalDate;

public class Task {
    private int id;
    private int project_id;
    private String title;
    private String description;
    private String status;
    private LocalDate start_date;
    private LocalDate end_date;
    private int assigned_by;
    private String assigned_by_name;
    private String developer;

    public Task() {}

    public Task(int id, int project_id, String title, String description, String status, LocalDate start_date, LocalDate end_date, int assigned_by) {
        this.id = id;
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.start_date = start_date;
        this.end_date = end_date;
        this.assigned_by = assigned_by;


    }

    public Task(String title, int id, int project_id, String description, String status, LocalDate start_date, LocalDate end_date, String assigned_by_name) {
        this.title = title;
        this.id = id;
        this.project_id = project_id;
        this.description = description;
        this.status = status;
        this.start_date = start_date;
        this.end_date = end_date;
        this.assigned_by_name = assigned_by_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getAssigned_by() {
        return assigned_by;
    }

    public void setAssigned_by(int assigned_by) {
        this.assigned_by = assigned_by;
    }

    public String getAssigned_by_name() {
        return assigned_by_name;
    }

    public void setAssigned_by_name(String assigned_by_name) {
        this.assigned_by_name = assigned_by_name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }
}
