module com.teamAF.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires org.json;

    opens com.teamAF.app to javafx.fxml;
    exports com.teamAF.app;
}