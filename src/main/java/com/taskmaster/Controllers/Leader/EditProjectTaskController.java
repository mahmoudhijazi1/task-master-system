package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Project;
import com.taskmaster.Models.Task;
import com.taskmaster.Utils.DataFetcher;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditProjectTaskController {
    @FXML
    public TextField titleField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public ComboBox<String> statusCombo;
    @FXML
    public DatePicker startDateField;
    @FXML
    public DatePicker endDateField;
    @FXML
    public Button submitBtn;


    private Project currentProject;
    private Task currentTask; // The task being edited

    public void initialize() {
        // Initialize status combo box with possible values
        statusCombo.getItems().addAll("Pending", "In Progress", "Completed");

        // Set up the submit button action
        submitBtn.setOnAction(event -> handleSubmit());

    }

    public void setCurrentProject(Project project) {
        this.currentProject = project;
        System.out.println("current project: " + currentProject);
    }

    public void setTaskToEdit(Task task) {
        this.currentTask = task;
        populateFieldsWithTaskData();
        System.out.println("task: " + currentTask);
    }

    private void populateFieldsWithTaskData() {
        if (currentTask != null) {
            titleField.setText(currentTask.getTitle());
            descriptionField.setText(currentTask.getDescription());
            statusCombo.setValue(currentTask.getStatus());
            startDateField.setValue(currentTask.getStartDate());
            endDateField.setValue(currentTask.getEndDate());
        }
    }

    private void handleSubmit() {
        if (validateInput()) {
            currentTask.setTitle(titleField.getText());
            currentTask.setDescription(descriptionField.getText());
            currentTask.setStatus(statusCombo.getValue());
            currentTask.setStartDate(startDateField.getValue());
            currentTask.setEndDate(endDateField.getValue());

            // Update the task in the database
            DataFetcher dataFetcher = new DataFetcher();
            dataFetcher.updateTask(currentTask, currentProject.getId());
//            DataFetcher.updateTask(currentTask);

            // Close the dialog
            closeDialog();
        }
    }

    private boolean validateInput() {
        // Basic input validation logic
        if (titleField.getText().isEmpty()) {
            showAlert("Validation Error", "Title cannot be empty.");
            return false;
        }
        if (statusCombo.getValue() == null) {
            showAlert("Validation Error", "Please select a status.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) submitBtn.getScene().getWindow();
        stage.close();
    }

    private void handleCancel() {
        closeDialog();
    }
}
