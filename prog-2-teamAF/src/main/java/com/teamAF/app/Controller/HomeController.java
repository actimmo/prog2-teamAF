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
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public CheckComboBox<String> genreCheckComboBox;

    @FXML
    public CheckComboBox<String> yearCheckComboBox;

    @FXML
    public CheckComboBox<String> ratingCheckComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    private Label selectedGenresLabel;

    @FXML
    private Label selectedYearsLabel;

    @FXML
    private Label selectedRatingsLabel;

    private MovieService movieService;
    public ObservableList<String> selectedGenres = FXCollections.observableArrayList();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    private EventManager eventManager;

    //No-Arg Constructor for FXML
    public HomeController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize EventManager with INFO logging level and console output enabled
        LogHandler logHandler = new LogHandler("configPath");
        logHandler.getConfig().getEvent().setPrintToConsole(true);
        logHandler.getConfig().getEvent().setInformationalMode(true);
        logHandler.getConfig().getInternalEvents().setEnabled(false);

        // Create EventManager instance
        this.eventManager = new EventManager(logHandler);
        eventManager.logInfoMessage("Home Controller initialized");

        // Initialize MovieService if not already initialized
        if (movieService == null) {
            movieService = new MovieService(this.eventManager);
        }

        // Set all movies to observableMovies and bind to movieListView
        observableMovies.setAll(movieService.getAllMovies());
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(listView -> new MovieCell());

        // Initialize genreCheckComboBox with predefined genres
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
            filterMovies(selectedGenres, searchField.getText(), new ArrayList<>(yearCheckComboBox.getCheckModel().getCheckedItems()), new ArrayList<>(ratingCheckComboBox.getCheckModel().getCheckedItems()));        });

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

        // Initialize year and rating CheckComboBoxes
        Set<String> years = new HashSet<>();
        Set<String> ratings = new HashSet<>();
        for (Movie movie : movieService.getAllMovies()) {
            years.add(String.valueOf(movie.getReleaseYear()));
            ratings.add(String.valueOf(movie.getRating()));
        }

        // Sort years and ratings in ComboBox that are being retrieved from API
        List<String> sortedYears = new ArrayList<>(years);
        List<String> sortedRatings = new ArrayList<>(ratings);
        Collections.sort(sortedYears);
        Collections.sort(sortedRatings);

        yearCheckComboBox.getItems().addAll(sortedYears);
        yearCheckComboBox.setTitle("Filter by Release Year");
        ratingCheckComboBox.getItems().addAll(sortedRatings);
        ratingCheckComboBox.setTitle("Filter by Rating");

        // hitting enter while focus on searchField triggers filter
        searchField.setOnAction(this::handleSearch);
        yearCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
            updateSelectedYearsLabel();
            handleSearch(null);
        });
        ratingCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
            updateSelectedRatingsLabel();
            handleSearch(null);
        });
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

    public void filterMovies(List<String> selectedGenres, String searchQuery, List<String> years, List<String> ratings) {
        eventManager.logInfoMessage("Filtering movies by genres - " + selectedGenres + ", search query - " + searchQuery + ", years - " + years + ", ratings - " + ratings);
        List<Movie> filtered = movieService.filterMovies(selectedGenres, searchQuery, years, ratings);
        observableMovies.setAll(filtered);
    }

    // for debugging purposes in the UI
    private void updateSelectedGenresLabel() {
        if (selectedGenres.isEmpty())
            selectedGenresLabel.setText("Selected Genres: ");
        else
            selectedGenresLabel.setText("Selected Genres: " + String.join(", ", selectedGenres));
    }

    // for debugging purposes in the UI
    private void updateSelectedYearsLabel() {
        List<String> selectedYears = yearCheckComboBox.getCheckModel().getCheckedItems();
        if (selectedYears.isEmpty())
            selectedYearsLabel.setText("Selected Years: ");
        else
            selectedYearsLabel.setText("Selected Years: " + String.join(", ", selectedYears));
    }
    // for debugging purposes in the UI
    private void updateSelectedRatingsLabel() {
        List<String> selectedRatings = ratingCheckComboBox.getCheckModel().getCheckedItems();
        if (selectedRatings.isEmpty())
            selectedRatingsLabel.setText("Selected Ratings: ");
        else
            selectedRatingsLabel.setText("Selected Ratings: " + String.join(", ", selectedRatings));
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        List<String> years = new ArrayList<>(yearCheckComboBox.getCheckModel().getCheckedItems());
        List<String> ratings = new ArrayList<>(ratingCheckComboBox.getCheckModel().getCheckedItems());
        filterMovies(selectedGenres, query, years, ratings);
    }

    /**
     * Returns the name of the actor who appears most often across all movies' main casts.
     */
    public String getMostPopularActor(List<Movie> movies) {
                Map<String, Long> counts = movies.stream()
                        .flatMap(movie -> movie.getMainCast().stream())
                        .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

                if (counts.isEmpty()) {
                    return "No Actors Found";
                }

                long maxActorCount = counts.values().stream()
                        .mapToLong(Long::longValue)
                        .max()
                        .orElse(0L);

                List<String> topActors = counts.entrySet().stream()
                        .filter(e -> e.getValue() == maxActorCount)
                        .map(Map.Entry::getKey)
                        .toList();

                if (topActors.size() == 1) {
                    return topActors.get(0);
                } else {
                    return String.join(", ", topActors);
                }
    }



}