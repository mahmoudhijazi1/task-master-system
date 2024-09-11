package com.taskmaster.Controllers.Developer;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import com.taskmaster.Models.Task;
import com.taskmaster.Utils.DataFetcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeveloperTasksController implements Initializable {
    @FXML public TableView<Task> tasksTable;
    @FXML public TableColumn<Task, String> titleCol;
    @FXML public TableColumn<Task, String> statusCol;
    @FXML public TableColumn<Task, String> projectCol;
    @FXML public TableColumn<Task, String> leaderCol;
    @FXML public TableColumn<Task, String> endDateCol;
    @FXML public Button setAsCompletedBtn;
    @FXML public ComboBox<String> filterCombo;

    private FilteredList<Task> filteredTasks;
    private ObservableList<Task> tasksList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        projectCol.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        leaderCol.setCellValueFactory(new PropertyValueFactory<>("assignedByName"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Populate the filterCombo with options
        filterCombo.setItems(FXCollections.observableArrayList("All", "Completed", "In Progress", "Pending"));

        loadTasks();

        // Add listener to the ComboBox to filter the list
        filterCombo.setOnAction(event -> filterTasks());
        setAsCompletedBtn.setOnAction(event -> setAsCompleted());
    }

    private void setAsCompleted() {
        DataFetcher dataFetcher = new DataFetcher();
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setStatus("Completed");
            dataFetcher.setTaskStatus(selectedTask);  // Update status in the database
            tasksTable.refresh();              // Refresh the table view
        }
    }

    private void loadTasks() {
        DataFetcher dataFetcher = new DataFetcher();
        tasksList = FXCollections.observableArrayList(dataFetcher.getDeveloperTasks(Model.getInstance().getCurrentUserId()));

        // Use FilteredList to enable filtering
        filteredTasks = new FilteredList<>(tasksList, p -> true);
        tasksTable.setItems(filteredTasks);
    }

    private void filterTasks() {
        String selectedFilter = filterCombo.getValue();

        if (selectedFilter == null || selectedFilter.equals("All")) {
            filteredTasks.setPredicate(task -> true); // Show all tasks
        } else {
            filteredTasks.setPredicate(task -> task.getStatus().equalsIgnoreCase(selectedFilter));
        }
    }
    private void refreshData() {
        DataFetcher dataFetcher = new DataFetcher();

            ObservableList<Task> updatedTasks = (ObservableList<Task>) dataFetcher.getDeveloperTasks(Model.getInstance().getCurrentUserId());
            if (updatedTasks != null) {
                tasksTable.setItems(updatedTasks); // Update the TableView with the tasks
            } else {
                System.out.println("Tasks list is null");
            }

    }
}
