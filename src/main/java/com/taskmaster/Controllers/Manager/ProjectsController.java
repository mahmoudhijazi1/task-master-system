package com.taskmaster.Controllers.Manager;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import com.taskmaster.Utils.DataFetcher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProjectsController implements Initializable {
    @FXML
    private AnchorPane projectsParent;

    @FXML
    private Button deleteBtn, addProjectBtn, editBtn;

    @FXML
    private TableView<Project> projectsTable;

    @FXML
    private TableColumn<Project, Integer> idCol;

    @FXML
    private TableColumn<Project, String> nameCol, descriptionCol;

    @FXML
    private TableColumn<Project, String> leaderCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showProjects();
        addListeners();
    }

    private void addListeners() {
        editBtn.setOnAction(event -> onEditBtn());
        deleteBtn.setOnAction(event -> {
            try {
                onDeleteBtn();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        addProjectBtn.setOnAction(event -> onAddProjectBtn());
    }

    // Assuming you have a method to refresh the data in your TableView
    public void refreshData() {
        DataFetcher dataFetcher = new DataFetcher();
        ObservableList<Project> updatedProjects = dataFetcher.getProjects();
        projectsTable.setItems(updatedProjects);
    }

    @FXML
    private void onAddProjectBtn() {
        Model.getInstance().getViewFactory().getProjectsParentViewController().showAddProjectView();
    }

    @FXML
    private void onEditBtn() {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            Model.getInstance().getViewFactory().getProjectsParentViewController().showEditProjectView(selectedProject);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a project to edit.");
        }
        refreshData();
    }

    @FXML
    private void onDeleteBtn() throws SQLException {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Project");
            confirmationAlert.setHeaderText("Are you sure you want to delete this project?");
            Optional<javafx.scene.control.ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                // Perform deletion from the database
                DataFetcher dataFetcher = new DataFetcher();
                int id = selectedProject.getId();
                dataFetcher.deleteProject(id);
                showProjects(); // Refresh the list after deletion
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a project to delete.");
        }
    }

    private void showProjects() {
        DataFetcher dataFetcher = new DataFetcher();
        ObservableList<Project> projects = dataFetcher.getProjects();
        projectsTable.setItems(projects);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        leaderCol.setCellValueFactory(new PropertyValueFactory<>("leader_name"));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
