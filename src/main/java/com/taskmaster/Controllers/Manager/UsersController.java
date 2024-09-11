package com.taskmaster.Controllers.Manager;

import com.taskmaster.Controllers.Leader.AddProjectTaskController;
import com.taskmaster.Models.Model;
import com.taskmaster.Utils.DataFetcher;
import com.taskmaster.Models.User; // Make sure you have a User model class
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UsersController {
    @FXML
    public ListView<User> leadersList;
    @FXML
    public ListView<User> developersList;
    @FXML
    public Button createUserBtn;

    @FXML
    private void initialize() {
        // Load users
        loadUsers();

        // Set button actions
        createUserBtn.setOnAction(event -> {
            try {
                handleCreateUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadUsers() {
        DataFetcher dataFetcher = new DataFetcher();
        List<User> leaders = dataFetcher.getUsersByRole(2); // Role ID for Leader
        List<User> developers = dataFetcher.getUsersByRole(3); // Role ID for Developer

        leadersList.getItems().setAll(leaders);
        developersList.getItems().setAll(developers);
    }

    private void handleCreateUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Manager/CreateUserDialog.fxml"));
        AnchorPane createUserPane = loader.load();

        CreateUserController controller = loader.getController();

        Stage modalStage = new Stage();
        modalStage.initOwner(leadersList.getScene().getWindow());
        modalStage.setTitle("Add Task");
        Scene scene = new Scene(createUserPane);
        modalStage.setScene(scene);
        modalStage.showAndWait();

        leadersList.refresh();
        developersList.refresh();
    }


}
