package com.taskmaster.Views;

import com.taskmaster.Controllers.Manager.ManagerController;
import com.taskmaster.Controllers.Manager.ProjectEditController;
import com.taskmaster.Controllers.Manager.ProjectsViewController;
import com.taskmaster.Models.Project;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    private final StringProperty managerSelectedMenuItem;

    private AnchorPane dashboardView;
    private AnchorPane projectDetailsView;
    private AnchorPane addProjectView;

    private BorderPane projectsParentView;
    private AnchorPane projectsView;
    private AnchorPane editProjectView;

    private ProjectsViewController projectsParentViewController;

    public ViewFactory() {
        this.managerSelectedMenuItem = new SimpleStringProperty("");
    }

    public StringProperty getManagerSelectedMenuItem() {
        return managerSelectedMenuItem;
    }

    // Get Dashboard view, load it if necessary
    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            dashboardView = loadView("/Fxml/Manager/Dashboard.fxml");
        }
        return dashboardView;
    }

    // Method to get the main parent view
    public BorderPane getProjectsParentView() {
        if (projectsParentView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Manager/ProjectsView.fxml"));
            try {
                projectsParentView = loader.load();
                projectsParentViewController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return projectsParentView;
    }

    public ProjectsViewController getProjectsParentViewController() {
        if (projectsParentView == null) {
            getProjectsParentView();  // Initialize if not already done
        }
        return projectsParentViewController;
    }

    public AnchorPane getProjectsView() {
        if (projectsView == null) {
            projectsView = loadView("/Fxml/Manager/Projects.fxml");
        }
        return projectsView;
    }

    public AnchorPane getAddProjectView() {
        if (addProjectView == null) {
            addProjectView = loadView("/Fxml/Manager/ProjectAdd.fxml");
        }
        return addProjectView;
    }

    public AnchorPane getEditProjectView(Project project) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Manager/ProjectEdit.fxml"));
        try {
            editProjectView = loader.load();
            ProjectEditController controller = loader.getController();
            controller.setProject(project);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return editProjectView;
    }

    public AnchorPane getProjectDetailsView() {
        if (projectDetailsView == null) {
            projectDetailsView = loadView("/Fxml/Manager/ProjectDetails.fxml");
        }
        return projectDetailsView;
    }

    // Show Login window
    public void showLoginWindow() {
        showWindow("/Fxml/Login.fxml");
    }

    // Show Manager window
    public void showManagerWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Manager/Manager.fxml"));
        loader.setController(new ManagerController());
        showWindow(loader);
    }

    // Utility method to create and show a new stage
    private void showWindow(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        showWindow(loader);
    }

    private void showWindow(FXMLLoader loader) {
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Task Master");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utility method to load views
    private AnchorPane loadView(String fxmlPath) {
        try {
            return FXMLLoader.load(getClass().getResource(fxmlPath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Close a stage
    public void closeStage(Stage stage) {
        stage.close();
    }
}
