package com.teamAF.app.View;

import com.teamAF.app.Controller.ClickEventHandler;
import com.teamAF.app.Model.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 * Custom ListCell for displaying Movie objects with show/hide details and watchlist functionality.
 */
public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Button showDetailsBtn = new Button("Show Details");
    private final Button watchlistBtn = new Button("Watchlist");
    private final Button removeBtn = new Button("Remove");
    private final HBox titleBar = new HBox();
    private final VBox layout = new VBox();
    public static final java.util.Set<String> addedToWatchlist = new java.util.HashSet<>();
    private final ClickEventHandler<Movie> clickHandler;
    private final boolean isWatchlistCell;

    // State for details toggle
    private boolean collapsedDetails = true;
    private VBox detailsBox;

    public MovieCell(ClickEventHandler<Movie> clickHandler, boolean isWatchlistCell) {
        this.clickHandler = clickHandler;
        this.isWatchlistCell = isWatchlistCell;
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        showDetailsBtn.setStyle("-fx-background-color: #f5c518; -fx-text-fill: black;");
        watchlistBtn.setStyle("-fx-background-color: #f5c518; -fx-text-fill: black;");
        removeBtn.setStyle("-fx-background-color: #f5c518; -fx-text-fill: black;");

        title.getStyleClass().add("text-yellow");
        title.setFont(Font.font(20));

        // Add buttons depending on mode (watchlist or normal)
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

    public MovieCell(ClickEventHandler<Movie> clickHandler) {
        this(clickHandler, false);
    }

    /**
     * Creates a VBox containing additional movie details.
     */
    private VBox getDetails(Movie movie) {
        VBox box = new VBox();
        box.setSpacing(8);
        box.setPadding(new Insets(12));
        box.setStyle(
                "-fx-background-color: #232323;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: #f5c518;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-effect: dropshadow(gaussian, #00000055, 8, 0.2, 0, 2);"
        );

        Label year = new Label("Release Year: " + movie.getReleaseYear());
        Label rating = new Label("Rating: " + movie.getRating());
        Label length = new Label("Length: " + (movie.getLengthInMinutes() > 0 ? movie.getLengthInMinutes() + " min" : "Unknown"));
        Label directors = new Label("Directors: " + (movie.getDirectors() != null && !movie.getDirectors().isEmpty() ? String.join(", ", movie.getDirectors()) : "Unknown"));
        Label writers = new Label("Writers: " + (movie.getWriters() != null && !movie.getWriters().isEmpty() ? String.join(", ", movie.getWriters()) : "Unknown"));
        Label mainCast = new Label("Main Cast: " + (movie.getMainCast() != null && !movie.getMainCast().isEmpty() ? String.join(", ", movie.getMainCast()) : "Unknown"));

        // Set text color and font for all labels
        for (Label label : new Label[]{year, rating, length, directors, writers, mainCast}) {
            label.setStyle("-fx-text-fill: #f5c518; -fx-font-size: 14px;");
        }

        box.getChildren().addAll(year, rating, length, directors, writers, mainCast);
        return box;
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

            // Initialize details box for the current movie
            detailsBox = getDetails(movie);

            // Show/Hide Details Button logic
            showDetailsBtn.setText(collapsedDetails ? "Show Details" : "Hide Details");
            showDetailsBtn.setOnMouseClicked(mouseEvent -> {
                if (collapsedDetails) {
                    layout.getChildren().add(detailsBox);
                    collapsedDetails = false;
                    showDetailsBtn.setText("Hide Details");
                } else {
                    layout.getChildren().remove(detailsBox);
                    collapsedDetails = true;
                    showDetailsBtn.setText("Show Details");
                }
                setGraphic(layout);
            });

            // Watchlist and Remove button logic depending on mode
            if (isWatchlistCell) {
                removeBtn.setText("Remove");
                removeBtn.setDisable(false);
                removeBtn.setOnAction(e -> clickHandler.onClick(movie));
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
                        clickHandler.onClick(movie);
                    });
                }
            }

            // Always collapse details when updating the cell
            if (!collapsedDetails && layout.getChildren().contains(detailsBox)) {
                layout.getChildren().remove(detailsBox);
                collapsedDetails = true;
                showDetailsBtn.setText("Show Details");
            }

            setGraphic(layout);
        }
    }
}