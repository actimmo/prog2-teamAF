package com.teamAF.app;

import com.teamAF.app.Controller.HomeController;
import com.teamAF.app.Model.Movie;
import com.teamAF.app.Model.MovieService;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    private HomeController homeController;
    private MovieService movieService;
    private List<Movie> allMovies;

    @BeforeEach
    void setUp() {
        allMovies = Arrays.asList(
                new Movie("Inception", "A mind-bending thriller",
                        Arrays.asList("SCIENCE_FICTION", "THRILLER")),
                new Movie("The Godfather", "A story about a crime family",
                        Arrays.asList("CRIME", "DRAMA")),
                new Movie("The Dark Knight", "A superhero film featuring Batman",
                        Arrays.asList("ACTION", "CRIME", "DRAMA")),
                new Movie("Forrest Gump", "The story of a man's extraordinary life",
                        Arrays.asList("DRAMA", "ROMANCE")),
                new Movie("The Matrix", "A computer hacker learns about reality",
                        Arrays.asList("SCIENCE_FICTION", "ACTION"))
        );

        movieService = new MovieService(allMovies);
        homeController = new HomeController(movieService);
        homeController.initialize(null, null);
    }

    @Test
    void sortMoviesAscending() {
        homeController.sortMoviesAscending();
        ObservableList<Movie> sortedMovies = homeController.getObservableMovies();

        assertEquals("Forrest Gump",  sortedMovies.get(0).getTitle());
        assertEquals("Inception",     sortedMovies.get(1).getTitle());
        assertEquals("The Dark Knight",  sortedMovies.get(2).getTitle());
        assertEquals("The Godfather",    sortedMovies.get(3).getTitle());
        assertEquals("The Matrix",       sortedMovies.get(4).getTitle());
    }

    @Test
    void sortMoviesDescending() {
        homeController.sortMoviesDescending();
        ObservableList<Movie> sortedMovies = homeController.getObservableMovies();

        assertEquals("The Matrix",    sortedMovies.get(0).getTitle());
        assertEquals("The Godfather", sortedMovies.get(1).getTitle());
        assertEquals("The Dark Knight", sortedMovies.get(2).getTitle());
        assertEquals("Inception",     sortedMovies.get(3).getTitle());
        assertEquals("Forrest Gump",  sortedMovies.get(4).getTitle());
    }

    @Test
    void filterMovies_withMatchingTitle() {
        homeController.filterMovies(Collections.emptyList(), "Matrix");

        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withMatchingDescription() {
        homeController.filterMovies(Collections.emptyList(), "hacker");

        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withPartialMatch() {
        homeController.filterMovies(Collections.emptyList(), "Dark");

        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Dark Knight", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatch() {
        homeController.filterMovies(Collections.emptyList(), "Nonexistent");

        assertEquals(0, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_withEmptyQuery() {
        homeController.filterMovies(Collections.emptyList(), "");

        assertEquals(5, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_withMatchingGenreAndQuery() {
        homeController.filterMovies(Arrays.asList("SCIENCE_FICTION"), "Matrix");

        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatchingGenreAndQuery() {
        homeController.filterMovies(Arrays.asList("COMEDY"), "Matrix");

        assertEquals(0, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_withMatchingGenreOnly() {
        homeController.filterMovies(Arrays.asList("SCIENCE_FICTION"), "");

        // In the test data, that should match "Inception" and "The Matrix"
        assertEquals(2, homeController.getObservableMovies().size());
        assertEquals("Inception", homeController.getObservableMovies().get(0).getTitle());
        assertEquals("The Matrix", homeController.getObservableMovies().get(1).getTitle());
    }

    @Test
    void filterMovies_withMatchingQueryOnly() {
        homeController.filterMovies(Collections.emptyList(), "Matrix");

        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withEmptyGenreAndQuery() {
        homeController.filterMovies(Collections.emptyList(), "");

        assertEquals(5, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_with2Genres() {
        homeController.filterMovies(Arrays.asList("SCIENCE_FICTION", "ACTION"), "");
        assertEquals(3, homeController.getObservableMovies().size());
        assertEquals("Inception", homeController.getObservableMovies().get(0).getTitle());
        assertEquals("The Dark Knight", homeController.getObservableMovies().get(1).getTitle());
        assertEquals("The Matrix", homeController.getObservableMovies().get(2).getTitle());
}
}