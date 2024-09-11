package com.taskmaster.Views;

import com.taskmaster.Controllers.Developer.DeveloperTasksViewController;
import com.taskmaster.Controllers.Developer.DeveloperViewController;
import com.taskmaster.Controllers.Leader.*;
import com.taskmaster.Controllers.Manager.ManagerController;
import com.taskmaster.Controllers.Manager.ProjectEditController;
import com.taskmaster.Controllers.Manager.ProjectsController;
import com.taskmaster.Controllers.Manager.ProjectsViewController;
import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private AnchorPane usersView;

    // LEADER SECTION DECLARATIONS
    private final StringProperty LeaderSelectedMenuItem;

    private AnchorPane leaderDashboardView;
    private AnchorPane taskDetailsView;
    private AnchorPane employeeManagementView;
    private AnchorPane leaderProjectsList;
    private AnchorPane leaderProjectDetailsView;

    private BorderPane leaderParentView;
    private LeaderViewController leaderParentViewController;
    private BorderPane leaderProjectsView;
    private LeaderProjectsViewController leaderProjectsViewController;
    private BorderPane leaderTasksView;
    private LeaderTasksViewController leaderTasksViewController;
    private LeaderProjectDetailsController leaderProjectDetailsController;
    private AnchorPane addProjectTaskDialog;


    // Constructor
    public ViewFactory() {
        this.managerSelectedMenuItem = new SimpleStringProperty("");
        this.LeaderSelectedMenuItem = new SimpleStringProperty("");
        this.DeveloperSelectedMenuItem = new SimpleStringProperty("");
    }


    public StringProperty getManagerSelectedMenuItem() {return managerSelectedMenuItem;}
    public StringProperty getLeaderSelectedMenuItem() { return LeaderSelectedMenuItem;}
    public StringProperty getDeveloperSelectedMenuItem() { return DeveloperSelectedMenuItem;}

    // MANAGER SECTION METHODS

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
    public AnchorPane getUsersView() {
        if (usersView == null) {
            usersView = loadView("/Fxml/Manager/Users.fxml");
        }
        return usersView;
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

    public LeaderProjectsViewController getLeaderProjectsViewController() {
        if (leaderProjectsViewController == null) {
            getLeaderProjectsView();  // Initialize if not already done
        }
        return leaderProjectsViewController;
    }
    public LeaderProjectDetailsController getLeaderProjectsDetailsController() {
        if (leaderProjectDetailsController == null) {

            getLeaderProjectDetailsView();
        }
        return leaderProjectDetailsController;
    }

    public LeaderTasksViewController getLeaderTasksViewController() {
        if (leaderTasksViewController == null) {
            getLeaderTasksView();  // Initialize if not already done
        }
        return leaderTasksViewController;
    }

    public AnchorPane getLeaderDashboardView() {
        if (leaderDashboardView == null) {
            leaderDashboardView = loadView("/Fxml/Leader/LeaderDashboardView.fxml");
        }
        return leaderDashboardView;
    }

    public BorderPane getLeaderProjectsView() {
        if (leaderProjectsView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Leader/LeaderProjectsView.fxml"));
            try {
                leaderProjectsView = loader.load();
                leaderProjectsViewController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return leaderProjectsView;
    }

    public BorderPane getLeaderTasksView() {
        if (leaderTasksView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Leader/LeaderTasksView.fxml"));
            try {
                leaderTasksView = loader.load();
                leaderTasksViewController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return leaderTasksView;
    }

    public AnchorPane getLeaderProjects() {
        if (leaderProjectsList == null) {
            leaderProjectsList = loadView("/Fxml/Leader/LeaderProjects.fxml");
        }
        return leaderProjectsList;
    }

    public AnchorPane getLeaderProjectDetailsView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Leader/LeaderProjectDetails.fxml"));
        try {
            leaderProjectDetailsView = loader.load();
            LeaderProjectDetailsController controller = loader.getController();
            controller.setCurrentProject(Model.getInstance().getCurrentProject());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaderProjectDetailsView;
    }
    public void showAddProjectTaskDialog(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Leader/AddProjectTaskDialog.fxml"));
        try {
            addProjectTaskDialog = loader.load();
            AddProjectTaskController controller = loader.getController();
            controller.setCurrentProject(Model.getInstance().getCurrentProject());
            showWindow(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            showWindow(loader);
        } else {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not have permission to access this section.");
        }
    }
    public void showDeveloperWindow() {
        if (Model.getInstance().getCurrentUserRole().equals("Developer")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Developer/DeveloperView.fxml"));
            loader.setController(new DeveloperViewController());
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

//    private final StringProperty LeaderSelectedMenuItem;
//
//    private AnchorPane leaderDashboardView;
//    private AnchorPane taskDetailsView;
//    private AnchorPane employeeManagementView;
//    private AnchorPane leaderProjectsList;
//    private AnchorPane leaderProjectDetailsView;
//
//    private BorderPane leaderParentView;
//    private LeaderViewController leaderParentViewController;
//    private BorderPane leaderProjectsView;
//    private LeaderProjectsViewController leaderProjectsViewController;
//    private BorderPane leaderTasksView;
//    private LeaderTasksViewController leaderTasksViewController;
//    private LeaderProjectDetailsController leaderProjectDetailsController;
//    private AnchorPane addProjectTaskDialog;

    private final StringProperty DeveloperSelectedMenuItem;

    private AnchorPane developerDashboardView;
    private BorderPane developerTasksView;
    private BorderPane developerProjectsView;
    private DeveloperTasksViewController developerTasksViewController;



    public AnchorPane getDeveloperDashboardView() {
        if (developerDashboardView == null) {
            developerDashboardView = loadView("/Fxml/Developer/DeveloperDashboardView.fxml");
        }
        return developerDashboardView;
    }

    public BorderPane getDeveloperTasksView() {
        if (developerTasksView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Developer/DeveloperTasksView.fxml"));
            try {
                developerTasksView = loader.load();
                developerTasksViewController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return developerTasksView;
    }
    public DeveloperTasksViewController getDeveloperTasksViewController() {
        if (developerTasksViewController == null) {
            getDeveloperTasksView();  // Initialize if not already done
        }
        return developerTasksViewController;
    }
}
