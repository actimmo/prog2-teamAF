module com.teamAF.app {
    requires javafx.fxml;

    requires com.jfoenix;
    requires org.json;
    requires org.controlsfx.controls;
    requires java.net.http;
    requires com.github.eventmanager;
    requires static lombok;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires ormlite.jdbc;


    opens com.teamAF.app to javafx.fxml;
    exports com.teamAF.app;
    exports com.teamAF.app.Controller;
    opens com.teamAF.app.Controller to javafx.fxml;


    exports com.teamAF.app.Model;
    opens com.teamAF.app.Model to com.google.gson;
    exports com.teamAF.app.Data;
    opens com.teamAF.app.Data to com.google.gson;
}