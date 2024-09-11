package com.taskmaster.Controllers.Manager;

import com.taskmaster.Utils.DataFetcher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserController implements Initializable {
    @FXML public TextField nameField;
    @FXML public TextField usernameField;
    @FXML public TextField passwordField;
    @FXML public ComboBox<String> roleCombo;
    @FXML public Button createBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize ComboBox with roles
        roleCombo.setItems(FXCollections.observableArrayList( "Leader", "Developer"));

        // Add action handler for the Create button
        createBtn.setOnAction(event -> createUser());
    }

    private void createUser() {
        // Get data from fields
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleCombo.getValue();
        int roleId = (roleCombo.getSelectionModel().getSelectedIndex())+2;
        System.out.println(roleId);

        // Check for empty fields
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill out all fields.");
            return;
        }

        DataFetcher dataFetcher = new DataFetcher();
        dataFetcher.createUser(name,username,password,roleId);

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "User created successfully.");
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
}
