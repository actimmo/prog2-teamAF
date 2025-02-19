package com.teamAF.app;

import com.jfoenix.controls.*;
import com.teamAF.app.models.Movie;
import com.teamAF.app.ui.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<String> genreComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    private Label selectedGenresLabel;

    public List<Movie> allMovies = Movie.initializeMovies();

    public ObservableList<String> selectedGenres = FXCollections.observableArrayList();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        // TODO add an option for no genre

        genreComboBox.setPromptText("Filter by Genre");
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("Select a genre", "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY", "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR", "MUSICAL", "MYSTERY", "ROMANCE", "SCIENCE_FICTION", "SPORT", "THRILLER", "WAR", "WESTERN"));

        genreComboBox.setItems(FXCollections.observableArrayList(genres));
        genreComboBox.getSelectionModel().select(0);

        genreComboBox.setOnAction(event -> {
            if(genreComboBox.getSelectionModel().getSelectedIndex()==0) return;
            String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();

            if (selectedGenres.contains(selectedGenre)) {
                selectedGenres.remove(selectedGenre);
            } else {
                selectedGenres.add(selectedGenre);
            }
            updateSelectedGenresLabel();
            EventHandler<ActionEvent> eh=genreComboBox.getOnAction();
            genreComboBox.setOnAction ( null );
            genreComboBox.getSelectionModel().select(0);
            genreComboBox.setOnAction ( eh );

        });

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {

                // TODO sort observableMovies ascending
                sortBtn.setText("Sort (desc)");
            } else {
                // TODO sort observableMovies descending
                sortBtn.setText("Sort (asc)");

            }
        });
    }

    public void sortMoviesAscending() {
        // Sortiere Liste in asc. Reihenfolge nach Titel
        FXCollections.sort(observableMovies, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                // Vergleiche Titel normaler Reihenfolge (asc.)
                return movie1.getTitle().compareToIgnoreCase(movie2.getTitle());
            }
        });
    }

    public void sortMoviesDescending() {
        // Sortiere Liste in desc. Reihenfolge nach Titel
        FXCollections.sort(observableMovies, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                // Vergleiche Titel in umgekehrter Reihenfolge (desc.)
                return movie2.getTitle().compareToIgnoreCase(movie1.getTitle());
            }
        });
    }

    public void filterMovies(String selectedGenre, String searchQuery){
        List<String> genres = new ArrayList<>();
        genres.add(selectedGenre);
        filterMovies(genres, searchQuery);
    }

    public void filterMovies(List<String> selectedGenre, String searchQuery) {
        // ObservableList leeren
        observableMovies.clear();

        // Set mit gefundenen Titeln, um Duplikate zu vermeiden
        Set<String> seenTitles = new HashSet<>();
        List<Movie> filteredMovies = new ArrayList<>();

        // Iteriere über alle Filme in der Liste
        for (Movie movie : allMovies) {
            // Überprüfe, ob Film Genre entspricht
            boolean matchesGenre = selectedGenre == null || selectedGenre.isEmpty() || selectedGenre.get(0).isEmpty();

            for (int i=0;i< movie.getGenreList().size()&&(!matchesGenre);i++) {
                matchesGenre = selectedGenre.contains(movie.getGenreList().get(i));
            }

            // Überprüfe, ob Film Suchbegriff entspricht
            boolean matchesQuery = searchQuery == null || searchQuery.isEmpty() || movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase())
                    || (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(searchQuery.toLowerCase()));

            // Wenn Film dem Genre und Suchbegriff entspricht und nicht in Liste, füge hinzu
            if (matchesGenre && matchesQuery && seenTitles.add(movie.getTitle().toLowerCase())) {
                filteredMovies.add(movie);
            }
        }

        observableMovies.addAll(filteredMovies);
    }

    public ObservableList<Movie> getObservableMovies() {
        return observableMovies;
    }

    public void setObservableMovies(ObservableList<Movie> movies) {
        this.observableMovies.setAll(movies);
    }

    private void updateSelectedGenresLabel() {
        if (selectedGenres.size()==0)
            selectedGenresLabel.setText("Selected Genres: ");
        else
         selectedGenresLabel.setText("Selected Genres: " + String.join(", ", selectedGenres));
    }


}