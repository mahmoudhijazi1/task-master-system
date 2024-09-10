package com.taskmaster.Controllers.Leader;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import com.taskmaster.Models.Task;
import com.taskmaster.Utils.DBConnection;
import com.taskmaster.Utils.DataFetcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class AddProjectTaskController {

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

        // Check if all fields are filled
        if (title.isEmpty() || description.isEmpty() || startDate == null || endDate == null) {
            // Handle validation error
            return;
        }

        Task task = new Task();
        task.setProject_id(currentProject.getId());
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus("Pending"); // Default status or handle as needed
        task.setStart_date(LocalDate.parse(startDate.toString()));
        task.setEnd_date(LocalDate.parse(endDate.toString()));
        task.setAssigned_by(Model.getInstance().getCurrentUserId());

        // Add task to the database
        DataFetcher dataFetcher = new DataFetcher();
        boolean success = dataFetcher.addTask(task);
        if (success) {

            // Handle success: Update UI or show a confirmation message
        } else {
            // Handle failure: Show error message or handle as needed
        }
    }
}
