package com.taskmaster.Controllers.Developer;

import com.taskmaster.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DeveloperViewController implements Initializable {
    public BorderPane developerParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getDeveloperSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "Tasks" -> developerParent.setCenter(Model.getInstance().getViewFactory().getDeveloperTasksView());
//                case "Projects" -> developerParent.setCenter(Model.getInstance().getViewFactory().getDeveloperProjectsView());
                default -> developerParent.setCenter(Model.getInstance().getViewFactory().getDeveloperDashboardView());
            }
        });
    }
}
