package com.teamAF.app.Controller;

import com.github.eventmanager.EventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import com.github.eventmanager.filehandlers.config.OutputEntry;
import com.jfoenix.controls.*;
import com.teamAF.app.Data.*;
import com.teamAF.app.Exceptions.DatabaseException;
import com.teamAF.app.Exceptions.MovieApiException;
import com.teamAF.app.Model.Movie;
import com.teamAF.app.Model.MovieService;
import com.teamAF.app.Model.enums.MyEnum;
import com.teamAF.app.View.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import com.teamAF.app.Interfaces.Observer;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable, Observer {
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

    @FXML
    private VBox sidebar;

    @FXML
    private Button hamburgerBtn;

    @FXML
    private Button closeSidebarBtn;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnWatchlist;

    @FXML
    private Button btnAbout;

    @FXML
    private VBox homeView;

    @FXML
    private VBox watchlistView;

    @FXML
    public JFXListView<Movie> watchlistListView;

    @FXML
    private VBox aboutView;

    @FXML
    private TextArea aboutTextArea;

    private final ObservableList<Movie> watchlistMovies = FXCollections.observableArrayList();
    private MovieService movieService;
    public ObservableList<String> selectedGenres = FXCollections.observableArrayList();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    public static EventManager eventManager;

    //No-Arg Constructor for FXML
    public HomeController() {
    }

    private DatabaseManager _instance;
    private MovieRepository _movieRepo;
    private WatchlistRepository _watchRepo;

    // Click Handler to add/remove Movies from Watchlist
    private final ClickEventHandler<Movie> onAddToWatchlistClicked = movie -> {
        try {
            _watchRepo.addToWatchlist(movie);
            //refreshWatchList();
            //addToWatchlist(movie);
        } catch (DatabaseException e) {
            this.eventManager.logErrorMessage("DatabaseException caught: " + e.getMessage());
            callErrorAlert(e, "Error when adding to watchlist");
        }
    };

    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = movie -> {
        try {
            _watchRepo.removeFromWatchlist(movie.getId());
            removeFromWatchlist(movie);
        } catch (DatabaseException e) {
            this.eventManager.logErrorMessage("DatabaseException caught: " + e.getMessage());
            callErrorAlert(e, "Error when removing from watchlist");
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize EventManager with DEBUG logging level and console output enabled
        LogHandler logHandler = new LogHandler("configPath");
        logHandler.getConfig().getEvent().setDebuggingMode(true);
        logHandler.getConfig().getInternalEvents().setEnabled(false);

        OutputEntry outputEntry = new OutputEntry();
        outputEntry.setName("PrintOutput");
        logHandler.getConfig().getOutputs().add(outputEntry);

        // Create EventManager instance
        this.eventManager = new EventManager(logHandler);
        eventManager.logInfoMessage("Home Controller initialized");

        MovieSortingContext movieSortingContext = new MovieSortingContext();

        try {
            _instance = DatabaseManager.getInstance();
            _movieRepo = MovieRepository.getInstance(_instance.getMovieDao());
            _watchRepo = WatchlistRepository.getInstance(_instance.getWatchlistDao());
        } catch (DatabaseException e) {
            this.eventManager.logErrorMessage("DatabaseException caught: " + e.getMessage());
            callErrorAlert(e, "Error when initializing database");
        }

        // Initialize MovieService if not already initialized
        if (movieService == null) {
            try {
                movieService = new MovieService(this.eventManager, this._movieRepo);
            } catch (DatabaseException e) {
                this.eventManager.logErrorMessage("DatabaseException caught: " + e.getMessage());
                callErrorAlert(e, "Error when initializing MovieService due to database problems");
            } catch (MovieApiException e) {
                this.eventManager.logErrorMessage("MovieApiException caught: " + e.getMessage());
                callErrorAlert(e, "Error when initializing MovieService due to API problems");
            }
        }

        try {
            List<WatchlistMovieEntity> wl = _watchRepo.getWatchlist();
            for (Movie m : movieService.getAllMovies().stream().filter(x -> wl.stream().map(w -> w.apiId).toList().contains(x.getId())).toList()) {
                com.teamAF.app.View.MovieCell.addedToWatchlist.add(m.getTitle());
            }
        } catch (DatabaseException e) {
            this.eventManager.logErrorMessage("DatabaseException caught: " + e.getMessage());
            callErrorAlert(e, "Error when initializing watchlist due to database problems");
        }

        // Set all movies to observableMovies and bind to movieListView
        observableMovies.setAll(movieService.getAllMovies());
        movieListView.setCellFactory(listView -> new MovieCell(onAddToWatchlistClicked));
        movieListView.setItems(observableMovies);
        watchlistListView.setItems(watchlistMovies);
        watchlistListView.setCellFactory(listView -> new MovieCell(onRemoveFromWatchlistClicked, true));

        // add to oberserver
        _watchRepo.addObserver(this);


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
            filterMovies(selectedGenres, searchField.getText(),
                    new ArrayList<>(yearCheckComboBox.getCheckModel().getCheckedItems()),
                    new ArrayList<>(ratingCheckComboBox.getCheckModel().getCheckedItems()));
        });

        // Set up sort button
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                sortBtn.setText("Sort (desc)");
                movieSortingContext.switchOrder();
            } else {
                sortBtn.setText("Sort (asc)");
                movieSortingContext.switchOrder();
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

        // Hamburger button shows sidebar and hides itself, X appears
        hamburgerBtn.setOnAction(e -> {
            sidebar.setVisible(true);
            hamburgerBtn.setVisible(false);
            closeSidebarBtn.setVisible(true);
        });

        // X button closes sidebar and hides itself, hamburger appears
        closeSidebarBtn.setOnAction(e -> {
            sidebar.setVisible(false);
            hamburgerBtn.setVisible(true);
            closeSidebarBtn.setVisible(false);
        });
        btnHome.setOnAction(e -> {
            showHome();
            sidebar.setVisible(false);
            hamburgerBtn.setVisible(true);
            closeSidebarBtn.setVisible(false);
        });
        btnWatchlist.setOnAction(e -> {
            showWatchlist();
            sidebar.setVisible(false);
            hamburgerBtn.setVisible(true);
            closeSidebarBtn.setVisible(false);
        });
        btnAbout.setOnAction(e -> {
            showAbout();
            sidebar.setVisible(false);
            hamburgerBtn.setVisible(true);
            closeSidebarBtn.setVisible(false);
        });
        showHome();
    }

    private static void callErrorAlert(Exception e, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(e.getClass().getSimpleName());
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    // Show home view, hide watchlist and about
    private void showHome() {
        homeView.setVisible(true);
        watchlistView.setVisible(false);
        aboutView.setVisible(false);
    }

    private void showWatchlist() {
        homeView.setVisible(false);
        //refreshWatchList();
        watchlistView.setVisible(true);
        aboutView.setVisible(false);
    }

    public void addToWatchlist(Movie movie) {
        if (!watchlistMovies.contains(movie)) {
            watchlistMovies.add(movie);
        }
    }

    public void refreshWatchList() {
        try {
            List<WatchlistMovieEntity> watchlistMovieEntityList = _watchRepo.getWatchlist();
            watchlistMovies.clear();
            // Get all apiIds from the watchlistMovieEntityList to compare them in the next stream
            List<String> apiIDList = watchlistMovieEntityList.stream()
                    .map(w -> w.apiId)
                    .toList();
            // Get all movies from the movieService and filter them by the apiIDList
            List<Movie> movieList = movieService.getAllMovies().stream()
                    .filter(x -> apiIDList.contains(x.getId()))
                    .toList();
            watchlistMovies.addAll(movieList);
        } catch (DatabaseException e) {
            watchlistMovies.clear();
            this.eventManager.logErrorMessage("DatabaseException caught: " + e.getMessage());
            callErrorAlert(e, "Error when refreshing watchlist");
        }
    }

    public void removeFromWatchlist(Movie movie) {
        watchlistMovies.remove(movie);
        // Remove the title from the static set variable in MovieCell
        com.teamAF.app.View.MovieCell.addedToWatchlist.remove(movie.getTitle());
        movieListView.refresh();
    }

    private void showAbout() {
        homeView.setVisible(false);
        watchlistView.setVisible(false);
        aboutView.setVisible(true);
        try {
            var inputStream = getClass().getResourceAsStream("/About.txt");
            if (inputStream == null) {
                aboutTextArea.setText("About.txt konnte nicht gefunden werden.");
                return;
            }
            String aboutText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            aboutTextArea.setText(aboutText);
        } catch (IOException e) {
            aboutTextArea.setText("About.txt konnte nicht geladen werden.");
        }
    }

    /*public void sortMoviesAscending() {
        eventManager.logInfoMessage("Sorting movies in ascending order");
        List<Movie> sorted = movieService.sortMoviesAscending(observableMovies);
        observableMovies.setAll(sorted);
    }

    public void sortMoviesDescending() {
        eventManager.logInfoMessage("Sorting movies in descending order");
        List<Movie> sorted = movieService.sortMoviesDescending(observableMovies);
        observableMovies.setAll(sorted);
    }*/

    public void filterMovies(List<String> selectedGenres, String searchQuery, List<String> years, List<String> ratings) {
        eventManager.logInfoMessage("Filtering movies by genres - " + selectedGenres + ", search query - " + searchQuery + ", years - " + years + ", ratings - " + ratings);
        List<Movie> filtered;
        try {
            filtered = movieService.filterMovies(selectedGenres, searchQuery, years, ratings);
            this.eventManager.logInfoMessage("Filtered movies successfully");
        } catch (MovieApiException e) {
            this.eventManager.logErrorMessage(e);
            callErrorAlert(e, "Error when filtering movies");
            filtered = movieService.getAllMovies();
        }
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
     * Returns the name of the actor who appears most often across all movies' main casts. If
     * no actor contained an error is thrown. If multiple actors are equally popular a list is returned.
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

    /**
     * Filters out the movie with the longest title and returns the number of characters
     * in that title.
     */
    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    /**
     * Counts the number of movies directed by a specific director.
     */
    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    /**
     * Returns a list of all movies released between startYear and endYear (inclusive).
     */
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear
                        && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }


    // Helper: map API Movie object to persistence MovieEntity
    private MovieEntity toEntity(Movie m) {
        MovieEntity e = new MovieEntity();
        e.apiId = m.getId();
        e.title = m.getTitle();
        e.description = m.getDescription();
        e.genres = m.getGenres();
        e.releaseYear = m.getReleaseYear();
        e.imgUrl = m.getImgUrl();
        e.lengthInMinutes = m.getLengthInMinutes();
        e.rating = m.getRating();
        return e;
    }

    @Override
    public void update(String apiID, MyEnum.RepoResponseCode responseCode) {
        switch(responseCode) {
            case ALREADY_EXISTS -> showWatchlistAlert("Film bereits in Watchlist.");
            case ERROR -> showWatchlistAlert("Fehler beim Bearbeiten.");
            case NOT_FOUND -> showWatchlistAlert("Film nicht gefunden.");
        }

        refreshWatchList();
    }

    private void showWatchlistAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Watchlist");
        alert.setHeaderText("Watchlist");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The MovieSortingContext class is used to manage the state of the movie sorting process.
     */
    private class MovieSortingContext{
        private State currentState;

        public MovieSortingContext() {
            this.currentState = new UnsortedState();
            eventManager.logInfoMessage("Movies displayed in unsorted order");
        }

        private interface State{
            void handle();
        }

        private class UnsortedState implements State {
            @Override
            public void handle() {
                MovieSortingContext.this.currentState = new AscendingState();
                MovieSortingContext.this.currentState.handle();
            }
        }

        private class AscendingState implements State {
            @Override
            public void handle() {
                eventManager.logInfoMessage("Sorting movies in ascending order");
                List<Movie> sorted = movieService.sortMoviesAscending(observableMovies);
                observableMovies.setAll(sorted);
                MovieSortingContext.this.currentState = new DescendingState();
            }
        }

        private class DescendingState implements State {
            @Override
            public void handle() {
                eventManager.logInfoMessage("Sorting movies in descending order");
                List<Movie> sorted = movieService.sortMoviesDescending(observableMovies);
                observableMovies.setAll(sorted);
                // Updating the nex state to AscendingState
                MovieSortingContext.this.currentState = new AscendingState();
            }
        }

        public void switchOrder() {
            this.currentState.handle();
        }
    }
}