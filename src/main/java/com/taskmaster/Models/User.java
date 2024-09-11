package com.taskmaster.Models;

public class User {
    private int id;
    private String name;
    private String username;

    // Constructors
    public User() {}

    public User(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    // Getters and setters
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return name + " (" + username + ")";
    }
}
