package com.taskmaster.Controllers.Manager;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import com.taskmaster.Utils.DataFetcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.time.LocalDate;

public class ProjectAddController {

    @FXML
    private AnchorPane addProjectPane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField descriptionField;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> leaderComboBox;

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;

    private DataFetcher dataFetcher;

    @FXML
    private void initialize() {
        dataFetcher = new DataFetcher();
        populateLeaderComboBox();
        addListeners();
    }

    private void addListeners() {
        cancelButton.setOnAction(event -> cancelAction());
        saveButton.setOnAction(event -> saveAction());
    }

    private void populateLeaderComboBox() {
        ObservableList<String> leaders = dataFetcher.getLeaders(); // Fetch leaders from the database
        leaderComboBox.setItems(leaders);
    }

    private void cancelAction() {
        Model.getInstance().getViewFactory().getProjectsParentViewController().showProjectsList();
    }

    @FXML
    private void saveAction() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String leaderName = leaderComboBox.getValue();

        if (name.isEmpty() || description.isEmpty() || startDate == null || endDate == null || leaderName == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled out.");
            return;
        }

        // Fetch leader's ID from the database based on the selected leader's name
        int leaderId = dataFetcher.getLeaderIdByName(leaderName);

        Project newProject = new Project(name, description, leaderId, startDate, endDate);

        // Add the new project to the database
        dataFetcher.addProject(newProject);

        // Navigate back to Projects view and refresh data
        refreshProjectList(); // Ensure the projects list is refreshed
        cancelAction();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Project added successfully.");
    }

    private void refreshProjectList() {
        Model.getInstance().getViewFactory().getProjectsController().refreshData();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
