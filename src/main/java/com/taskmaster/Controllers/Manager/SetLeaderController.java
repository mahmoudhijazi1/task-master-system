package com.taskmaster.Controllers.Manager;

import com.taskmaster.Utils.DataFetcher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class SetLeaderController {

    @FXML
    private ComboBox<String> leaderComboBox;

    private int projectId; // To store the ID of the selected project

    @FXML
    public void initialize() {
        DataFetcher dataFetcher = new DataFetcher();
        ObservableList<String> leaders = dataFetcher.getLeaders();
        leaderComboBox.setItems(leaders);
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @FXML
    private void setLeader() {
        String selectedLeader = leaderComboBox.getValue();
        if (selectedLeader != null) {
            DataFetcher dataFetcher = new DataFetcher();
            int leaderId = dataFetcher.getLeaderIdByName(selectedLeader);
            if (leaderId != -1) {
                // Update the project with the selected leader
                dataFetcher.updateProjectLeader(projectId, leaderId);

                // Close the modal
                Stage stage = (Stage) leaderComboBox.getScene().getWindow();
                stage.close();
            }
        }
    }
}
