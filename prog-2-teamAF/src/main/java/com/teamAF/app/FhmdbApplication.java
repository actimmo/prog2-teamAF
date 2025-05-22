package com.teamAF.app;

import com.teamAF.app.Controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("/home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("/styles.css")).toExternalForm());
        stage.setTitle("FHMDb");
        stage.setScene(scene);
        // Close the event manager when the application is closed
        stage.setOnCloseRequest(event -> {
            HomeController.eventManager.logInfoMessage("FHMDB Application Closed");
            HomeController.eventManager.stopPipeline();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}