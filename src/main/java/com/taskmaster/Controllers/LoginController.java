package com.taskmaster.Controllers;

import com.taskmaster.Models.Model;
import com.taskmaster.Utils.DataFetcher;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        // Fetch roles from the database or define them statically
        DataFetcher dataFetcher = new DataFetcher();
        List<String> roles = dataFetcher.getRoles();
        roleChoiceBox.getItems().addAll(roles);
        loginButton.setOnAction(event -> handleLogin());
        // Handle Enter key press on the username and password fields
        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire(); // Trigger the login button action
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            return;
        }

        DataFetcher dataFetcher = new DataFetcher();
        boolean isAuthenticated = dataFetcher.validateUser(username, password, role);
        if (isAuthenticated) {
            int userId = dataFetcher.getUserIdByUsername(username);
            if (userId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "User ID not found!");
                return;
            }

            // Set up session and navigate to the appropriate section
            Model.getInstance().setCurrentUser(username, role, userId); // Ensure setCurrentUser can handle user ID
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);

            // Redirect based on the user's role
            switch (role) {
                case "Manager":
                    Model.getInstance().getViewFactory().showManagerWindow();
                    break;
                case "Leader":
                    Model.getInstance().getViewFactory().getLeaderProjectsController().showProjects();
                    Model.getInstance().getViewFactory().getLeaderTasksController().loadTasksData();
                    Model.getInstance().getViewFactory().showLeaderWindow();
                    break;
                case "Developer":
                    // Implement the method to show the Employee window
                    Model.getInstance().getViewFactory().showDeveloperWindow();
                    break;
                default:
                    showAlert(Alert.AlertType.ERROR, "Error", "Unknown role!");
                    break;
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid credentials!");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
