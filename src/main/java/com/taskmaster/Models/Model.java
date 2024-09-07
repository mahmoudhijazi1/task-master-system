package com.taskmaster.Models;

import com.taskmaster.Views.ViewFactory;
import javafx.stage.Stage;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private String currentUser;
    private String currentUserRole;

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

    public void setCurrentUser(String username, String role) {
        this.currentUser = username;
        this.currentUserRole = role;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }

    public void logout() {
        currentUser = null;
        currentUserRole = null;

        viewFactory.showLoginWindow();
    }

}
