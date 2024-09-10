package com.taskmaster.Controllers.Leader;

import com.taskmaster.Controllers.Manager.SetLeaderController;
import com.taskmaster.Models.Model;
import com.taskmaster.Models.Task;
import com.taskmaster.Models.Project;
import com.taskmaster.Utils.DataFetcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class LeaderProjectDetailsController {


    @FXML
    private TableView<Task> tasksTableView;

    @FXML
    private TableColumn<Task, String> taskNameColumn;

    @FXML
    private TableColumn<Task, String> taskDeveloperColumn;

    @FXML
    private TableColumn<Task, String> taskStatusColumn;

    @FXML
    private TableColumn<Task, LocalDate> taskDueDateColumn;

    @FXML
    private Button editTaskButton;

    @FXML
    private Button setTaskCompletedButton;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button backButton;

    private ObservableList<Task> tasksList = FXCollections.observableArrayList();
    private Project currentProject;

    public void initialize() {
        clearTable();
        setupListeners(); // Set up listeners for button actions
        setupTableColumns();
        showProjectDetails();
    }

    private void clearTable() {
        tasksTableView.getItems().clear();
    }

    private void showProjectDetails() {
        currentProject = Model.getInstance().getCurrentProject();


        if (currentProject == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No project selected!");
            return;
        }

        DataFetcher dataFetcher = new DataFetcher();
        Project project = dataFetcher.getProjectDetails(currentProject.getId());
        System.out.println(project.getName());

        if (project != null) {
            // Load tasks related to this project
            tasksList.clear();
            System.out.println(project.getTasks().size());
            tasksList.addAll(project.getTasks());
            tasksTableView.setItems(tasksList);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load project details.");
        }
    }

    private void setupTableColumns() {
        // Set up the columns for the tasks table view
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        taskDeveloperColumn.setCellValueFactory(new PropertyValueFactory<>("developer"));
        taskStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        taskDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("end_date"));

        // Initialize the table view with the task list
        tasksTableView.setItems(tasksList);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupListeners() {
        addTaskButton.setOnAction(event -> {
            try {
                handleAddTask();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        editTaskButton.setOnAction(event -> handleEditTask());
        setTaskCompletedButton.setOnAction(event -> handleSetTaskCompleted());
        tasksTableView.setOnMouseClicked(this::handleTaskSelection);
        backButton.setOnAction(event -> handleBackButton());
    }

    private void handleTaskSelection(MouseEvent event) {
        // Enable/Disable buttons based on task selection
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            editTaskButton.setDisable(false);
            setTaskCompletedButton.setDisable(false);
        } else {
            editTaskButton.setDisable(true);
            setTaskCompletedButton.setDisable(true);
        }
    }

    @FXML
    private void handleAddTask() throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Leader/AddProjectTaskDialog.fxml"));
        AnchorPane setLeaderPane = loader.load();

        AddProjectTaskController controller = loader.getController();
        controller.setCurrentProject(currentProject);

        Stage modalStage = new Stage();
        modalStage.initOwner(tasksTableView.getScene().getWindow());
        modalStage.setTitle("Set Leader");
        Scene scene = new Scene(setLeaderPane);
        modalStage.setScene(scene);
        modalStage.showAndWait();


    }

    private void refreshData() {
        DataFetcher dataFetcher = new DataFetcher();
        Project updatedTasks = dataFetcher.getProjectDetails(currentProject.getId());
        if(updatedTasks != null) {tasksTableView.setItems(updatedTasks);}
        else {
            System.out.println("projects is null");
        }
    }


    private void handleEditTask() {
        DataFetcher dataFetcher = new DataFetcher();

        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Task updatedTask = showTaskDialog(selectedTask);
            if (updatedTask != null) {
                dataFetcher.updateTask(updatedTask); // Update task in the database
                tasksTableView.refresh();            // Refresh the table view
            }
        }
    }

    private void handleSetTaskCompleted() {
        DataFetcher dataFetcher = new DataFetcher();
        Task selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setStatus("Completed");
            dataFetcher.updateTask(selectedTask);  // Update status in the database
            tasksTableView.refresh();              // Refresh the table view
        }
    }

    private Task showTaskDialog(Task task) {
        // Logic to show a dialog for adding or editing a task
        // Return the newly created or updated task
        // For now, this can be a placeholder method
        return null;
    }

    public void setCurrentProject(Project project) {
        this.currentProject = project;
        showProjectDetails(); // Load project details when set
    }

    private void handleBackButton() {

        Model.getInstance().getViewFactory().getLeaderProjectsViewController().showProjectsView();
    }

    public void initializeDetails() {
        // Reinitialize tasks or other details specific to the current project
        loadTasksForCurrentProject();
    }

    private void loadTasksForCurrentProject() {
        Project currentProject = Model.getInstance().getCurrentProject();
        if (currentProject != null) {
            DataFetcher dataFetcher = new DataFetcher();
            int id = currentProject.getId();
            ObservableList<Task> tasks = (ObservableList<Task>) dataFetcher.getProjectDetails(id);
            tasksTableView.setItems(tasks);
        }
    }
}
