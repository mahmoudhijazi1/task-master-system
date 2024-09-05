package com.taskmaster.Controllers.Manager;

import com.taskmaster.Models.Model;
import com.taskmaster.Models.Project;
import com.taskmaster.Utils.DataFetcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProjectEditController implements Initializable {

    @FXML
    private Button cancelBtn, saveBtn;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker startDate, endDate;

    @FXML
    private TextField name;

    private Project project;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        configureDatePickers();
    }

    private void addListeners() {
        cancelBtn.setOnAction(event -> cancelAction());
        saveBtn.setOnAction(event -> saveAction());
    }

    private void configureDatePickers() {
        StringConverter<LocalDate> dateConverter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string) : null;
            }
        };
        startDate.setConverter(dateConverter);
        endDate.setConverter(dateConverter);
    }

    @FXML
    private void cancelAction() {
        Model.getInstance().getViewFactory().getProjectsParentViewController().showProjectsList();
    }

    public void setProject(Project project) {
        this.project = project;
        if (project != null) {
            name.setText(project.getName());
            description.setText(project.getDescription());
            startDate.setValue(project.getStart_date());
            endDate.setValue(project.getEnd_date());
        }
    }

    @FXML
    private void saveAction() {
        if (project != null) {
            project.setName(name.getText());
            project.setDescription(description.getText());
            project.setStart_date(getStartDate());
            project.setEnd_date(getEndDate());

            // Save to database or perform other actions
            DataFetcher dataFetcher = new DataFetcher();
            dataFetcher.updateProject(project);

            // Navigate back to Projects view
            cancelAction();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project updated successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the project.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public LocalDate getStartDate() {
        return startDate.getValue();
    }

    public LocalDate getEndDate() {
        return endDate.getValue();
    }
}
