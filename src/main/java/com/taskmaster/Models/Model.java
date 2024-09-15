package com.taskmaster.Models;

import com.taskmaster.Views.ViewFactory;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private String currentUser;
    private String currentRole;
    private int currentUserId;
    private Project currentProject;



    public Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public void setCurrentUser(String username, String role, int userId) {
        this.currentUser = username;
        this.currentRole = role;
        this.currentUserId = userId;
    }

    public String getCurrentUser() {
        return currentUser;
    }
    public void clearCurrentUser() {
        this.currentUser = null;  // Clear user data
    }

    public String getCurrentUserRole() {
        return currentRole;
    }

    public void logout() {
        currentUser = null;
        currentRole = null;
        currentUserId = 0;
        viewFactory.showLoginWindow();
    }



    public String getCurrentRole() {
        return currentRole;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }
}


