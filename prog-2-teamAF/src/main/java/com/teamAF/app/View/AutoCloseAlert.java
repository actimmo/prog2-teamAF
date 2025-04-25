package com.teamAF.app.View;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AutoCloseAlert  {
    private AlertType alertType;
    private String title;
    private String header;
    private String content;
    private int seconds;
    public AutoCloseAlert(String title, String header, String content, AlertType alertType,int seconds) {
        this.title = title;
        this.header = header;
        this.content = content;
        this.alertType = alertType;
        this.seconds = seconds;
    }


    public void create() {
        // Create an alert
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Show the alert
        alert.show();

        // Create a PauseTransition to close the alert after 5 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(event -> alert.close());
        delay.play();
    }


}