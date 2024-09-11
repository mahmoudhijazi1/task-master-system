package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import com.taskmaster.Models.Task;
import com.taskmaster.Utils.DBConnection;
import com.taskmaster.Utils.DataFetcher;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class AddProjectTaskController {

    @FXML public ComboBox<String> developerCombo;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;
    @FXML private Button submitBtn;

    private Project currentProject;

    @FXML
    public void initialize() {
        // Initialize any necessary UI components or event handlers
        submitBtn.setOnAction(event -> handleSubmitButton());
        loadDevelopers();
    }

    private void loadDevelopers() {
        DataFetcher dataFetcher = new DataFetcher();
        // Assume getDevelopers() returns a List<String> of developer names
        List<String> developers = dataFetcher.getDevelopers();
        for (String developer : developers) {
            System.out.println(developer);
        }
        developerCombo.getItems().addAll(developers);
    }

    public void setCurrentProject(Project project) {
        this.currentProject = project;
    }



    private void handleSubmitButton() {
        if (currentProject == null ) {
            // Handle error: No project or user selected
            return;
        }

        String title = titleField.getText();
        String description = descriptionField.getText();
        LocalDate startDate = startDateField.getValue();
        LocalDate endDate = endDateField.getValue();
        String developer = developerCombo.getValue();

        // Check if all fields are filled
        if (title.isEmpty() || description.isEmpty() || startDate == null || endDate == null) {
            // Handle validation error
            return;
        }

        Task task = new Task();
        task.setProjectId(currentProject.getId());
        task.setTitle(title);
        task.setDescription(description);
        task.setDeveloperName(developer);
        task.setStatus("Pending"); // Default status or handle as needed
        task.setStartDate(LocalDate.parse(startDate.toString()));
        task.setEndDate(LocalDate.parse(endDate.toString()));
        task.setAssignedBy(Model.getInstance().getCurrentUserId());

        // Add task to the database
        DataFetcher dataFetcher = new DataFetcher();
        boolean success = dataFetcher.addTask(task, developer);
        if (success) {
            System.out.println("added task successfully");
            // Handle success: Update UI or show a confirmation message
        } else {
            System.out.println("failed to add task");
            // Handle failure: Show error message or handle as needed
        }
        Stage stage = (Stage) submitBtn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
