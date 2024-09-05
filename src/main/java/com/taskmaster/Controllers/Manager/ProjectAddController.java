package com.taskmaster.Controllers.Manager;

import com.taskmaster.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectAddController implements Initializable {
    @FXML
//    private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
//        backBtn.setOnAction(event -> onBackBtn());
    }

    private void onBackBtn() {
        Model.getInstance().getViewFactory().getProjectsParentViewController().showProjectsList();
    }


}
