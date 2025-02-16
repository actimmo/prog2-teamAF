module com.teamAF.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires org.json;
    requires java.desktop;

    opens com.teamAF.app to javafx.fxml;
    exports com.teamAF.app;
}