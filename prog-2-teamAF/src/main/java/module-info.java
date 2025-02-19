module com.teamAF.app {
    requires javafx.fxml;

    requires com.jfoenix;
    requires org.json;
    requires org.controlsfx.controls;

    opens com.teamAF.app to javafx.fxml;
    exports com.teamAF.app;
}