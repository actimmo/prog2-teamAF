package com.teamAF.app.Controller;

import com.github.eventmanager.EventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import com.jfoenix.controls.*;
import com.teamAF.app.Model.Movie;
import com.teamAF.app.Model.MovieService;
import com.teamAF.app.View.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

    private MovieService movieService;
    public ObservableList<String> selectedGenres = FXCollections.observableArrayList();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    private EventManager eventManager;

    //No-Arg Constructor for FXML
    public HomeController() {
    }

    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize EventManager with INFO logging level and console output enabled
        LogHandler logHandler = new LogHandler("configPath");
        logHandler.getConfig().getEvent().setPrintToConsole(true);
        logHandler.getConfig().getEvent().setInformationalMode(true);
        logHandler.getConfig().getInternalEvents().setEnabled(false);

        this.eventManager = new EventManager(logHandler);
        eventManager.logInfoMessage("Home Controller initialized");
        if (movieService == null) {
            movieService = new MovieService(this.eventManager);
        }

        observableMovies.setAll(movieService.getAllMovies());
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell());

        ArrayList<String> genres = new ArrayList<>(Arrays.asList(
                "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY", "CRIME",
                "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR",
                "MUSICAL", "MYSTERY", "ROMANCE", "SCIENCE_FICTION", "SPORT",
                "THRILLER", "WAR", "WESTERN"
        ));
        genreCheckComboBox.getItems().addAll(genres);
        genreCheckComboBox.setTitle("Filter by Genre");

        // Listen for checked genres and filter right away
        genreCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
            selectedGenres.clear();
            selectedGenres.addAll(genreCheckComboBox.getCheckModel().getCheckedItems());
            updateSelectedGenresLabel();
            filterMovies(selectedGenres, searchField.getText());
        });

        // Set up sort button
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                sortBtn.setText("Sort (desc)");
                sortMoviesAscending();
            } else {
                sortBtn.setText("Sort (asc)");
                sortMoviesDescending();
            }
        });

        // hitting enter while focus on searchField triggers filter
        searchField.setOnAction(this::handleSearch);
    }

    public void sortMoviesAscending() {
        eventManager.logInfoMessage("Sorting movies in ascending order");
        List<Movie> sorted = movieService.sortMoviesAscending(observableMovies);
        observableMovies.setAll(sorted);
    }

    public void sortMoviesDescending() {
        eventManager.logInfoMessage("Sorting movies in descending order");
        List<Movie> sorted = movieService.sortMoviesDescending(observableMovies);
        observableMovies.setAll(sorted);
    }

    public void filterMovies(List<String> selectedGenres, String searchQuery) {
        eventManager.logInfoMessage( "Filtering movies by genres - " + selectedGenres + " and search query - " + searchQuery);
        List<Movie> filtered = movieService.filterMovies(selectedGenres, searchQuery);
        observableMovies.setAll(filtered);
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
        String query = searchField.getText();
        filterMovies(selectedGenres, query);
    }


}