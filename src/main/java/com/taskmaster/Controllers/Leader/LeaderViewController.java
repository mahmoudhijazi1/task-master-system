package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderViewController implements Initializable {
    public BorderPane leaderParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getLeaderSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "Tasks" -> leaderParent.setCenter(Model.getInstance().getViewFactory().getLeaderTasksView());
                case "Projects" -> leaderParent.setCenter(Model.getInstance().getViewFactory().getLeaderProjectsView());
                default -> leaderParent.setCenter(Model.getInstance().getViewFactory().getLeaderDashboardView());
            }
        });
    }
}
