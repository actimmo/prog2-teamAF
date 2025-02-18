package com.teamAF.app;

import com.teamAF.app.models.Movie;
import com.teamAF.app.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

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

    public List<Movie> allMovies = Movie.initializeMovies();

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
        ArrayList<String> genres = new ArrayList<>(Arrays.asList("ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY", "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR", "MUSICAL", "MYSTERY", "ROMANCE", "SCIENCE_FICTION", "SPORT", "THRILLER", "WAR", "WESTERN" ));
        genreComboBox.getItems().addAll(genres);

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here

//        // Sort button example:
//        sortBtn.setOnAction(actionEvent -> {
//            if(sortBtn.getText().equals("Sort (asc)")) {
//                // TODO sort observableMovies ascending
//                sortBtn.setText("Sort (desc)");
//            } else {
//                // TODO sort observableMovies descending
//                sortBtn.setText("Sort (asc)");

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

    public void searchQuery(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();

        // ObservableList leeren
        observableMovies.clear();

        // Wenn der Suchbegriff leer ist, zeige alle Filme
        if (searchQuery.isEmpty()) {
            observableMovies.addAll(allMovies);
            return;
        }

        // Set mit gefundenen Titeln, um Duplikate zu vermeiden
        Set<String> seenTitles = new HashSet<>();
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : allMovies) {
            // Überprüfen, ob der Suchbegriff im Titel oder der Beschreibung vorkommt
            if ((movie.getTitle().toLowerCase().contains(searchQuery) ||
                    (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(searchQuery))) &&
                    seenTitles.add(movie.getTitle().toLowerCase())) {
                filteredMovies.add(movie);
            }
        }

        // Gefilterte Filme zur ObservableList hinzufügen
        observableMovies.addAll(filteredMovies);
    }

    public void filterMoviesByGenre(String selectedGenre, String searchQuery) {
        // ObservableList leeren
        observableMovies.clear();

        // Set mit gefundenen Titeln, um Duplikate zu vermeiden
        Set<String> seenTitles = new HashSet<>();
        List<Movie> filteredMovies = new ArrayList<>();

        // Iteriere über alle Filme in der Liste
        for (Movie movie : allMovies) {
            // Überprüfe, ob Film Genre entspricht
            boolean matchesGenre = selectedGenre == null || movie.getGenres().toLowerCase().contains(selectedGenre.toLowerCase());

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
}