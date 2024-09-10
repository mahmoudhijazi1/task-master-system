package com.taskmaster.Controllers.Manager;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectsViewController implements Initializable {
    public BorderPane projects_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code if needed
        showProjectsList();
    }

    public void showProjectsList() {
        projects_parent.setCenter(Model.getInstance().getViewFactory().getProjectsView());
    }

    public void showAddProjectView() {
        projects_parent.setCenter(Model.getInstance().getViewFactory().getAddProjectView());
    }

    public void showEditProjectView(Project project) {
        projects_parent.setCenter(Model.getInstance().getViewFactory().getEditProjectView(project));
    }

    
}
