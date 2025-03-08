module com.teamAF.app {
    requires javafx.fxml;

    requires com.jfoenix;
    requires org.json;
    requires org.controlsfx.controls;
    requires com.google.gson;



    opens com.teamAF.app to javafx.fxml;
    exports com.teamAF.app;
    exports com.teamAF.app.Controller;
    opens com.teamAF.app.Controller to javafx.fxml;
    opens com.teamAF.app.Model to com.google.gson;
}