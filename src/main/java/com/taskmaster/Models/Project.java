package com.taskmaster.Models;

import java.sql.Date;
import java.time.LocalDate;

public class Project {
    private int id;
    private String name;
    private String description;
    private int manager_id;
    private int leader_id;
    private String leader_name; // New field to store the leader's name
    private LocalDate start_date;
    private LocalDate end_date;

    public Project(int id, String name, String description, int leaderId, String leaderName, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.leader_id = leaderId;
        this.leader_name = leaderName; // Initialize the new field
        this.start_date = startDate;
        this.end_date = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    public int getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public String getLeader_name() {
        return leader_name; // Getter for the new field
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name; // Setter for the new field
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

    // Convert LocalDate to SQL Date
    public Date toSqlDate(LocalDate localDate) {
        return (localDate != null) ? Date.valueOf(localDate) : null;
    }

    // Convert SQL Date to LocalDate
    public LocalDate toLocalDate(Date sqlDate) {
        return (sqlDate != null) ? sqlDate.toLocalDate() : null;
    }
}
