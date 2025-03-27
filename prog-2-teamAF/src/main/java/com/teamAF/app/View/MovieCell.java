package com.teamAF.app.View;

import com.teamAF.app.Model.Movie;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final VBox layout = new VBox(title, detail, genre);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText(
                    movie.getGenres()!= null
                            ? movie.getGenres()
                            : "No genres available"
            );

            // Apply modern styling
            title.getStyleClass().add("title-label");
            detail.getStyleClass().add("detail-label");
            genre.getStyleClass().add("genre-label");

            // Layout
            layout.setPadding(new Insets(15));
            layout.setSpacing(8);
            layout.setMaxWidth(Double.MAX_VALUE);
            detail.setMaxWidth(Double.MAX_VALUE);
            detail.setWrapText(true);
            setGraphic(layout);
        }
    }
}

