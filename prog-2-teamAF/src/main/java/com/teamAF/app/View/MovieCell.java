package com.teamAF.app.View;

import com.teamAF.app.Controller.HomeController;
import com.teamAF.app.Model.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Button showDetailsBtn = new Button("Show Details");
    private final Button watchlistBtn = new Button("Watchlist");
    private final Button removeBtn = new Button("Remove");
    private final HBox titleBar = new HBox();
    private final VBox layout = new VBox();
    public static final java.util.Set<String> addedToWatchlist = new java.util.HashSet<>();    private final HomeController controller;
    private final boolean isWatchlistCell;

    public MovieCell(HomeController controller, boolean isWatchlistCell) {
        this.controller = controller;
        this.isWatchlistCell = isWatchlistCell;
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        showDetailsBtn.setStyle("-fx-background-color: #f5c518; -fx-text-fill: black;");
        watchlistBtn.setStyle("-fx-background-color: #f5c518; -fx-text-fill: black;");
        removeBtn.setStyle("-fx-background-color: #f5c518; -fx-text-fill: black;");

        title.getStyleClass().add("text-yellow");
        title.setFont(Font.font(20));

        // Buttons depending on mode
        if (isWatchlistCell) {
            titleBar.getChildren().addAll(title, spacer, showDetailsBtn, removeBtn);
        } else {
            titleBar.getChildren().addAll(title, spacer, showDetailsBtn, watchlistBtn);
        }
        titleBar.setSpacing(10);
        titleBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        layout.getChildren().addAll(titleBar, detail, genre);
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
        layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        detail.getStyleClass().add("text-white");
        detail.setWrapText(true);

        genre.getStyleClass().add("text-white");
        genre.setFont(Font.font("Arial", FontPosture.ITALIC, 12));
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            title.setText(movie.getTitle());
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");
            genre.setText(movie.getGenres() != null ? movie.getGenres() : "No genres available");
            detail.setMaxWidth(this.getScene().getWidth() - 30);

            if (isWatchlistCell) {
                removeBtn.setText("Remove");
                removeBtn.setDisable(false);
                removeBtn.setOnAction(e -> controller.removeFromWatchlist(movie));
            } else {
                if (addedToWatchlist.contains(movie.getTitle())) {
                    watchlistBtn.setText("Added");
                    watchlistBtn.setDisable(true);
                } else {
                    watchlistBtn.setText("Watchlist");
                    watchlistBtn.setDisable(false);
                    watchlistBtn.setOnAction(e -> {
                        addedToWatchlist.add(movie.getTitle());
                        watchlistBtn.setText("Added");
                        watchlistBtn.setDisable(true);
                        controller.addToWatchlist(movie);
                    });
                }
            }
            setGraphic(layout);
        }
    }
}