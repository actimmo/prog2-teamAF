package com.teamAF.app;

import com.jfoenix.controls.*;
import com.teamAF.app.models.Movie;
import com.teamAF.app.ui.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;

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
    CheckComboBox<String> genreCheckComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    private Label selectedGenresLabel;

    public List<Movie> allMovies = Movie.initializeMovies();

    public ObservableList<String> selectedGenres = FXCollections.observableArrayList();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1) Initialize the observableMovies with allMovies
        observableMovies.addAll(allMovies);
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell());

        // 2) Populate the CheckComboBox with genre items
        ArrayList<String> genres = new ArrayList<>(Arrays.asList(
                "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY", "CRIME",
                "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR",
                "MUSICAL", "MYSTERY", "ROMANCE", "SCIENCE_FICTION", "SPORT",
                "THRILLER", "WAR", "WESTERN"
        ));
        genreCheckComboBox.getItems().addAll(genres);
        genreCheckComboBox.setTitle("Filter by Genre");

        // 3) Listen for changes in the “checked” genres
        genreCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
            selectedGenres.clear();
            selectedGenres.addAll(genreCheckComboBox.getCheckModel().getCheckedItems());
            updateSelectedGenresLabel();
            // Trigger filtering immediately, if desired:
            filterMovies(selectedGenres, searchField.getText());
        });

        // 4) Handle sort button
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                sortBtn.setText("Sort (desc)");
                sortMoviesAscending();
            } else {
                sortBtn.setText("Sort (asc)");
                sortMoviesDescending();
            }
        });

        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                sortBtn.setText("Sort (desc)");
                sortMoviesAscending();
            } else {
                sortBtn.setText("Sort (asc)");
                sortMoviesDescending();
            }
        });
    }

    public void sortMoviesAscending() {
        FXCollections.sort(observableMovies, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                return movie1.getTitle().compareToIgnoreCase(movie2.getTitle());
            }
        });
    }

    public void sortMoviesDescending() {
        FXCollections.sort(observableMovies, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
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
        for (Movie m : allMovies) {
            System.out.println(m.getTitle());
        }

        System.out.println("filterMovies called with " + selectedGenre + ", '" + searchQuery + "'");

        Set<String> seenTitles = new HashSet<>();
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : allMovies) {
            boolean matchesGenre = selectedGenre == null || selectedGenre.isEmpty() || selectedGenre.get(0).isEmpty();

            for (int i=0;i< movie.getGenreList().size()&&(!matchesGenre);i++) {
                matchesGenre = selectedGenre.contains(movie.getGenreList().get(i));
            }

            boolean matchesQuery = searchQuery == null || searchQuery.isEmpty() || movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase())
                    || (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(searchQuery.toLowerCase()));

            if (matchesGenre && matchesQuery && seenTitles.add(movie.getTitle().toLowerCase())) {
                filteredMovies.add(movie);
            }
        }
        observableMovies.clear();
        observableMovies.addAll(filteredMovies);

        for (Movie m : filteredMovies) {
            System.out.println(m.getTitle());
        }

    }

    public ObservableList<Movie> getObservableMovies() {
        return observableMovies;
    }

    public void setObservableMovies(ObservableList<Movie> movies) {
        this.observableMovies.setAll(movies);
    }

    private void updateSelectedGenresLabel() {
        if (selectedGenres.isEmpty())
            selectedGenresLabel.setText("Selected Genres: ");
        else
         selectedGenresLabel.setText("Selected Genres: " + String.join(", ", selectedGenres));
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        System.out.println(searchField.getText());
        filterMovies(selectedGenres, searchField.getText());
    }


}