package com.taskmaster.Views;

import com.taskmaster.Controllers.Leader.LeaderViewController;
import com.taskmaster.Controllers.Manager.ManagerController;
import com.taskmaster.Controllers.Manager.ProjectEditController;
import com.taskmaster.Controllers.Manager.ProjectsController;
import com.taskmaster.Controllers.Manager.ProjectsViewController;
import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    // MANAGER SECTION DECLARATIONS
    private final StringProperty managerSelectedMenuItem;
    private BorderPane projectsParentView;
    private AnchorPane dashboardView;
    private AnchorPane projectsView;
    private AnchorPane addProjectView;
    private AnchorPane editProjectView;
    private AnchorPane projectDetailsView;
    private ProjectsViewController projectsParentViewController;
    private ProjectsController projectsController;

    // LEADER SECTION DECLARATIONS
    private final StringProperty LeaderSelectedMenuItem;

    private AnchorPane leaderDashboardView;
    private AnchorPane leaderTasksView;
    private AnchorPane taskDetailsView;
    private AnchorPane employeeManagementView;

    // Constructor
    public ViewFactory() {
        this.managerSelectedMenuItem = new SimpleStringProperty("");
        this.LeaderSelectedMenuItem = new SimpleStringProperty("");
    }

    // MANAGER SECTION METHODS

    public StringProperty getManagerSelectedMenuItem() {
        return managerSelectedMenuItem;
    }
    public StringProperty getLeaderSelectedMenuItem() {
        return LeaderSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            dashboardView = loadView("/Fxml/Manager/Dashboard.fxml");
        }
        return dashboardView;
    }

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

    public ProjectsController getProjectsController() {
        if (projectsController == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Manager/Projects.fxml"));
            try {
                AnchorPane projectsPane = loader.load();
                projectsController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return projectsController;
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

    // LEADER SECTION METHODS

    public AnchorPane getLeaderDashboardView() {
        if (leaderDashboardView == null){
            leaderDashboardView = loadView("/Fxml/Leader/Dashboard.fxml");
        }
        return leaderDashboardView;
    }

    public AnchorPane getLeaderTasksView() {
        if (leaderTasksView == null) {
            leaderTasksView = loadView("/Fxml/Leader/LeaderTasksView.fxml");
        }
        return leaderTasksView;
    }

    public AnchorPane getTaskDetailsView() {
        if (taskDetailsView == null) {
            taskDetailsView = loadView("/Fxml/Leader/TaskDetailsView.fxml");
        }
        return taskDetailsView;
    }

    public AnchorPane getEmployeeManagementView() {
        if (employeeManagementView == null) {
            employeeManagementView = loadView("/Fxml/Leader/EmployeeManagementView.fxml");
        }
        return employeeManagementView;
    }

    // MAIN WINDOW METHODS
    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        showWindow(loader);
    }

    public void showManagerWindow() {
        if (Model.getInstance().getCurrentUserRole().equals("Manager")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Manager/Manager.fxml"));
            loader.setController(new ManagerController());
            showWindow(loader);
        } else {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to access this section.");
        }
    }

    public void showLeaderWindow() {
        if (Model.getInstance().getCurrentUserRole().equals("Leader")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Leader/LeaderView.fxml"));
            loader.setController(new LeaderViewController());
//            System.out.println(loader.getController().toString());
            showWindow(loader);
        } else {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to access this section.");
        }
    }

    // UTILITY METHODS

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

    private AnchorPane loadView(String fxmlPath) {
        try {
            return FXMLLoader.load(getClass().getResource(fxmlPath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }


}
