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
    }

    @Test
    void sortMoviesAscending() {
        List<Movie> sortedMovies = movieService.sortMoviesAscending(allMovies);

        assertEquals("Forrest Gump",  sortedMovies.get(0).getTitle());
        assertEquals("Inception",     sortedMovies.get(1).getTitle());
        assertEquals("The Dark Knight",  sortedMovies.get(2).getTitle());
        assertEquals("The Godfather",    sortedMovies.get(3).getTitle());
        assertEquals("The Matrix",       sortedMovies.get(4).getTitle());
    }

    @Test
    void sortMoviesDescending() {
        List<Movie> sortedMovies = movieService.sortMoviesDescending(allMovies);

        assertEquals("The Matrix",    sortedMovies.get(0).getTitle());
        assertEquals("The Godfather", sortedMovies.get(1).getTitle());
        assertEquals("The Dark Knight", sortedMovies.get(2).getTitle());
        assertEquals("Inception",     sortedMovies.get(3).getTitle());
        assertEquals("Forrest Gump",  sortedMovies.get(4).getTitle());
    }

    @Test
    void filterMovies_withMatchingTitle() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "Matrix");

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withMatchingDescription() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "hacker");

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withPartialMatch() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "Dark");

        assertEquals(1, filtered.size());
        assertEquals("The Dark Knight", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatch() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "nonexistant");

        assertEquals(0, filtered.size());
    }

    @Test
    void filterMovies_withEmptyQuery() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "");

        assertEquals(5, filtered.size());
    }

    @Test
    void filterMovies_withMatchingGenreAndQuery() {
        List<Movie> filtered = movieService.filterMovies(List.of("SCIENCE_FICTION"), "Matrix");

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatchingGenreAndQuery() {
        List<Movie> filtered = movieService.filterMovies(List.of("COMEDY"), "Matrix");

        assertEquals(0, filtered.size());
    }

    @Test
    void filterMovies_withMatchingGenreOnly() {
        List<Movie> filtered = movieService.filterMovies(List.of("SCIENCE_FICTION"), "");

        // In the test data, that should match "Inception" and "The Matrix"
        assertEquals(2, filtered.size());
        assertEquals("Inception", filtered.get(0).getTitle());
        assertEquals("The Matrix", filtered.get(1).getTitle());
    }

    @Test
    void filterMovies_withMatchingQueryOnly() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "Matrix");

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withEmptyGenreAndQuery() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "");

        assertEquals(5, filtered.size());
    }

    @Test
    void filterMovies_with2Genres() {
        List<Movie> filtered = movieService.filterMovies(List.of("SCIENCE_FICTION", "ACTION"), "");
        assertEquals(3, filtered.size());
        assertEquals("Inception", filtered.get(0).getTitle());
        assertEquals("The Dark Knight", filtered.get(1).getTitle());
        assertEquals("The Matrix", filtered.get(2).getTitle());
    }

    @Test
    void filterMovies_withNoMatchingGenreAndEmptyQuery() {
        List<Movie> filtered = movieService.filterMovies(List.of("ANIMATION"), "");
        assertEquals(0, filtered.size());
    }
}