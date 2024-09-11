package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Task;
import com.taskmaster.Models.AssignedTask;  // You should have these models created to represent data
import com.taskmaster.Utils.DataFetcher; // Assuming you have a class to fetch data
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
    private TableColumn<AssignedTask, String> developerCol;

    private ObservableList<Task> tasksList;

    public void initialize() {
        // Initialize the columns
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        developerCol.setCellValueFactory(new PropertyValueFactory<>("developerName"));

        // Load data
        loadTasksData();
    }

    private void loadTasksData() {
        DataFetcher dataFetcher = new DataFetcher();  // Or use Dependency Injection if available
        tasksList = FXCollections.observableArrayList(dataFetcher.getTasksForLeader(Model.getInstance().getCurrentUserId()));
        tasksTable.setItems(tasksList);
    }
}
