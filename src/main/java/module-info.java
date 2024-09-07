module com.taskmaster.taskmastersystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires mysql.connector.j;


    opens com.taskmaster.Controllers.Manager to javafx.fxml; // Add this line
    opens com.taskmaster.Controllers.Leader to javafx.fxml;
    opens com.taskmaster.Controllers to javafx.fxml;
    opens com.taskmaster.Models to javafx.base;

    exports com.taskmaster.Controllers;
    exports com.taskmaster.Models;
    exports com.taskmaster;

}