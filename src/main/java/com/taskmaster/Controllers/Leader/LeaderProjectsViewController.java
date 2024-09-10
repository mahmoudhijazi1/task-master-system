package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderProjectsViewController implements Initializable {
    public BorderPane leader_projects_parent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showProjectsView();
    }

    public void showProjectsView(){
       leader_projects_parent.setCenter(Model.getInstance().getViewFactory().getLeaderProjects());
    }

    public void showProjectDetails(){
        leader_projects_parent.setCenter(Model.getInstance().getViewFactory().getLeaderProjectDetailsView());
    }



}
