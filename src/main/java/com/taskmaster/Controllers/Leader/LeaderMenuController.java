package com.taskmaster.Controllers.Leader;

import com.taskmaster.App;
import com.taskmaster.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderMenuController implements Initializable {
    @FXML
    public Button projectsBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button tasksBtn;

    @FXML
    private Button logoutBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("LeaderMenuController is initialized.");
        addListeners();
    }


    private void addListeners() {
        dashboardBtn.setOnAction(event -> {
            System.out.println("Dashboard Button Clicked"); // Debugging line
            handleDashboardBtnClick();
        });
        projectsBtn.setOnAction(event -> {
            System.out.println("Projects Button Clicked"); // Debugging line
            handleProjectsBtnClick();
        });
        tasksBtn.setOnAction(event -> {
            System.out.println("Tasks Button Clicked"); // Debugging line
            handleTasksBtnClick();
        });
        logoutBtn.setOnAction(event -> {
            System.out.println("Logout Button Clicked"); // Debugging line
            handleLogoutBtnClick();
        });
    }



    private void handleDashboardBtnClick() {
        Model.getInstance().getViewFactory().getLeaderSelectedMenuItem().set("Dashboard");
    }

    private void handleProjectsBtnClick() {
        Model.getInstance().getViewFactory().getLeaderSelectedMenuItem().set("Projects");
    }

    private void handleTasksBtnClick() {
        Model.getInstance().getViewFactory().getLeaderSelectedMenuItem().set("Tasks");

    }

    private void handleLogoutBtnClick() {
        Stage stage  = (Stage) logoutBtn.getScene().getWindow();
        stage.close();
        Model.getInstance().logout();
        Model.getInstance().getViewFactory().getLeaderProjectsController().clearData();
        Model.getInstance().getViewFactory().getLeaderTasksController().clearData();

    }
}
