package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import com.taskmaster.Utils.DataFetcher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LeaderProjectsController implements Initializable {

    @FXML
    private TableView<Project> projectsTable;


    @FXML
    private TableColumn<Project, String> nameCol, descriptionCol;
    @FXML
    public TableColumn<Project, LocalDate> startDateCol,EndDateCol;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showProjects();
        addDoubleClickEventHandler();
    }



    private void showProjects() {
        DataFetcher dataFetcher = new DataFetcher();
        ObservableList<Project> projects = dataFetcher.getProjectsByUserId(Model.getInstance().getCurrentUserId());
        projectsTable.setItems(projects);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        EndDateCol.setCellValueFactory(new PropertyValueFactory<>("end_date"));
    }

    // Add an event handler for double-clicks
    private void addDoubleClickEventHandler() {
        projectsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
                if (selectedProject != null) {
                    showProjectDetails(selectedProject);
                }
            }
        });
    }

    // Method to show project details
    private void showProjectDetails(Project selectedProject) {
        // Set the current project in the Model
        Model.getInstance().setCurrentProject(selectedProject);

        Model.getInstance().getViewFactory().getLeaderProjectsViewController().showProjectDetails();
    }


    
}
