package com.taskmaster.Controllers.Manager;

import com.taskmaster.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerMenuController implements Initializable {

    public Button dashboardBtn;
    public Button projectsBtn;
    public Button logoutBtn;
    public Button usersBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        dashboardBtn.setOnAction(event -> onDashboard());
        projectsBtn.setOnAction(event -> onProjects());
        logoutBtn.setOnAction(event -> onLogout());
        usersBtn.setOnAction(event -> onUsers());
    }



    private void onLogout() {
        Stage stage  = (Stage) logoutBtn.getScene().getWindow();
        stage.close();
        Model.getInstance().logout();
    }


    private void onDashboard() {
        Model.getInstance().getViewFactory().getManagerSelectedMenuItem().set("Dashboard");
    }

    private void onProjects() {
        Model.getInstance().getViewFactory().getManagerSelectedMenuItem().set("Projects");
    }

    private void onUsers() {Model.getInstance().getViewFactory().getManagerSelectedMenuItem().set("Users");}
}

