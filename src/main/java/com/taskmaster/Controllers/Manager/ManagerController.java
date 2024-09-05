package com.taskmaster.Controllers.Manager;

import com.taskmaster.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    public BorderPane manager_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getManagerSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "Projects" -> manager_parent.setCenter(Model.getInstance().getViewFactory().getProjectsParentView());
                default -> manager_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
            }
        });
    }
}
