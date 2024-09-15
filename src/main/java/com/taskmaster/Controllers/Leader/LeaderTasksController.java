package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Task;
import com.taskmaster.Models.AssignedTask;  // You should have these models created to represent data
import com.taskmaster.Utils.DataFetcher; // Assuming you have a class to fetch data
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LeaderTasksController {

    @FXML
    private TableView<Task> tasksTable;

    @FXML
    private TableColumn<Task, String> titleCol;

    @FXML
    private TableColumn<Task, String> statusCol;

    @FXML
    private TableColumn<Task, String> startDateCol;

    @FXML
    private TableColumn<Task, String> endDateCol;

    @FXML
    private TableColumn<Task, String> developerCol;

    private ObservableList<Task> tasksList;
    @FXML
    public ComboBox<String> filterCombo;

    private FilteredList<Task> filteredTasks;

    public void initialize() {
        // Initialize the columns
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        developerCol.setCellValueFactory(new PropertyValueFactory<>("developerName"));

        filterCombo.setItems(FXCollections.observableArrayList("All", "Completed", "In Progress", "Pending"));

        filterCombo.setOnAction(event -> filterTasks());

        // Load data
        loadTasksData();
    }

    public void loadTasksData() {
        System.out.println("Loading Tasks");
        // Clear existing data
        clearData();

        // Fetch new data
        DataFetcher dataFetcher = new DataFetcher();  // Or use Dependency Injection if available
        tasksList = FXCollections.observableArrayList(dataFetcher.getTasksForLeader(Model.getInstance().getCurrentUserId()));

        // Use FilteredList to enable filtering
        filteredTasks = new FilteredList<>(tasksList, p -> true);

        // Update TableView
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

    public void clearData() {
        // Clear the filteredTasks and tasksList
        if (filteredTasks != null) {
            filteredTasks.setPredicate(task -> false); // Effectively clears the filtered list
        }

        if (tasksList != null) {
            tasksList.clear(); // Clear the observable list
        }

        if (tasksTable != null) {
            tasksTable.setItems(FXCollections.observableArrayList()); // Reset the TableView
        }
    }
}
