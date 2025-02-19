package com.teamAF.app;

import com.teamAF.app.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.collections.FXCollections;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    private HomeController homeController;
    private List<Movie> allMovies;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
        allMovies = Arrays.asList(
                new Movie("Inception", "A mind-bending thriller", Arrays.asList("SCIENCE_FICTION", "THRILLER")),
                new Movie("The Godfather", "A story about a powerful Italian-American crime family", Arrays.asList("CRIME", "DRAMA")),
                new Movie("The Dark Knight", "A superhero film featuring Batman", Arrays.asList("ACTION", "CRIME", "DRAMA")),
                new Movie("Forrest Gump", "The story of a man's extraordinary life", Arrays.asList("DRAMA", "ROMANCE")),
                new Movie("The Matrix", "A computer hacker learns about the true nature of reality", Arrays.asList("SCIENCE_FICTION", "ACTION"))
        );
        homeController.allMovies = allMovies;
        homeController.setObservableMovies(FXCollections.observableArrayList(allMovies));
    }

    @Test
    void sortMoviesAscending() {
        homeController.sortMoviesAscending();
        assertEquals("Forrest Gump", homeController.getObservableMovies().get(0).getTitle());
        assertEquals("Inception", homeController.getObservableMovies().get(1).getTitle());
        assertEquals("The Dark Knight", homeController.getObservableMovies().get(2).getTitle());
        assertEquals("The Godfather", homeController.getObservableMovies().get(3).getTitle());
        assertEquals("The Matrix", homeController.getObservableMovies().get(4).getTitle());
    }

    @Test
    void sortMoviesDescending() {
        homeController.sortMoviesDescending();
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
        assertEquals("The Godfather", homeController.getObservableMovies().get(1).getTitle());
        assertEquals("The Dark Knight", homeController.getObservableMovies().get(2).getTitle());
        assertEquals("Inception", homeController.getObservableMovies().get(3).getTitle());
        assertEquals("Forrest Gump", homeController.getObservableMovies().get(4).getTitle());
    }

    @Test
    void filterMovies_withMatchingTitle() {
        homeController.filterMovies("", "Matrix");
        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withMatchingDescription() {
        homeController.filterMovies("", "hacker");
        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withPartialMatch() {
        homeController.filterMovies("", "Dark");
        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Dark Knight", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatch() {
        homeController.filterMovies("", "Nonexistent");
        assertEquals(0, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_withEmptyQuery() {
        homeController.filterMovies("", "");
        assertEquals(5, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_withMatchingGenreAndQuery() {
        homeController.filterMovies("SCIENCE_FICTION", "Matrix");
        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatchingGenreAndQuery() {
        homeController.filterMovies("COMEDY", "Matrix");
        assertEquals(0, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_withMatchingGenreOnly() {
        homeController.filterMovies("SCIENCE_FICTION", "");
        assertEquals(2, homeController.getObservableMovies().size());
        assertEquals("Inception", homeController.getObservableMovies().get(0).getTitle());
        assertEquals("The Matrix", homeController.getObservableMovies().get(1).getTitle());
    }

    @Test
    void filterMovies_withMatchingQueryOnly() {
        homeController.filterMovies("", "Matrix");
        assertEquals(1, homeController.getObservableMovies().size());
        assertEquals("The Matrix", homeController.getObservableMovies().get(0).getTitle());
    }

    @Test
    void filterMovies_withEmptyGenreAndQuery() {
        homeController.filterMovies("", "");
        assertEquals(5, homeController.getObservableMovies().size());
    }

    @Test
    void filterMovies_with2Genres() {
        homeController.filterMovies(Arrays.stream(new String[]{"SCIENCE_FICTION","ACTION"}).toList(), "");
        assertEquals(3, homeController.getObservableMovies().size());
        assertEquals("Inception", homeController.getObservableMovies().get(0).getTitle());
        assertEquals("The Dark Knight", homeController.getObservableMovies().get(1).getTitle());
        assertEquals("The Matrix", homeController.getObservableMovies().get(2).getTitle());
    }
}