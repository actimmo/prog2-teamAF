package com.teamAF.app.Controller;
import com.teamAF.app.Model.Movie;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeControllerTest {

    private HomeController controller;
    private List<Movie> movies;

    @BeforeEach
    void setUp() {
        controller = new HomeController();
        movies = new ArrayList<>();

        // String id, String title, String description, List<String> genres, int releaseYear, String imgUrl,
        //                 int lengthInMinutes, List<String> directors, List<String> writers, List<String> mainCast,
        //                 double rating)

        movies = Arrays.asList(
                new Movie("86642997-ee66-4102-ade1-54941a1d3a6e",
                        "Inception",
                        "A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.",
                        Arrays.asList("ACTION", "SCIENCE_FICTION"),
                        2010,
                        "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_FMjpg_UX1000_.jpg",
                        148,
                        List.of("Christopher Nolan"),
                        List.of("Christopher Nolan"),
                        Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"),
                        8.8),

                new Movie("e2d9913d-3869-454c-9fbf-a63aaf57bedf",
                        "The Matrix",
                        "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                        Arrays.asList("ACTION", "SCIENCE_FICTION"),
                        1999,
                        "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                        136,
                        List.of("Lana Wachowski", "Lilly Wachowski"),
                        List.of("Lana Wachowski", "Lilly Wachowski"),
                        Arrays.asList("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
                        8.7),

                new Movie("d95d6912-b281-4e08-86b8-f9101f5f2c15",
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList("BIOGRAPHY", "COMEDY", "CRIME", "DRAMA"),
                        2013,
                        "https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_FMjpg_UX1000_.jpg",
                        180,
                        List.of("Martin Scorsese"),
                        List.of("Martin Scorsese"),
                        Arrays.asList("Leonardo DiCaprio", "Jonah Hill", "Margot Robbie"),
                        8.2),

                new Movie("d95d6912-b281-4e08-86b8-f9101f5f2c15",
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList("BIOGRAPHY", "COMEDY", "CRIME", "DRAMA"),
                        2013,
                        "https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_FMjpg_UX1000_.jpg",
                        180,
                        List.of("Martin Scorsese"),
                        List.of("Martin Scorsese"),
                        List.of(),
                        8.2)
        );

    }

    @Test
    void testGetMostPopularActor() {
        String mostPopularActor = controller.getMostPopularActor(movies);
        assertEquals("Leonardo DiCaprio", mostPopularActor);
    }

    @Test
    void testGetMostPopularActor_MulitpleMostPopularActor() {
        String mostPopularActor = controller.getMostPopularActor(movies.subList(0, 2));
        assertEquals(("Leonardo DiCaprio, Carrie-Anne Moss, Keanu Reeves, Joseph Gordon-Levitt, " +
                "Laurence Fishburne, Elliot Page"), mostPopularActor);
    }

    @Test
    void testGetMostPopularActor_noActorsReceived() {
        String mostPopularActor = controller.getMostPopularActor(movies.subList(3, 3));
        assertEquals("No Actors Found", mostPopularActor);
    }

    @Test
    void testGetLongestMovieTitle_withExistingMovies() {
        int result = controller.getLongestMovieTitle(movies);
        assertEquals(23, result,
                "Should be the length of 'The Wolf of Wall Street' (23).");
    }

    @Test
    void testGetLongestMovieTitle_withNoMovies() {
        List<Movie> emptyList = new ArrayList<>();
        int result = controller.getLongestMovieTitle(emptyList);
        assertEquals(0, result, "Expected 0 when no movies are given.");
    }

    @Test
    void testGetLongestMovieTitle_sameLengthTitles() {
        List<Movie> customMovies = new ArrayList<>(movies);
        customMovies.add(
                new Movie("test-id", "Another Wolf of Wall Street",
                        "Test description...", new ArrayList<>(), 2013,
                        "", 100, new ArrayList<>(), new ArrayList<>(),
                        new ArrayList<>(), 7.0)
        );

        int length = controller.getLongestMovieTitle(customMovies);
        assertEquals(27, length,
                "In case of a tie max length is returned..");
    }

    @Test
    void testCountMoviesFrom_singleDirector() {
        long count = controller.countMoviesFrom(movies, "Christopher Nolan");
        assertEquals(1, count);
    }

    @Test
    void testCountMoviesFrom_directorNotPresent() {
        long count = controller.countMoviesFrom(movies, "Steven Spielberg");
        assertEquals(0, count, "If the director is not present 0 is returned");
    }

    @Test
    void testCountMoviesFrom_multipleMatches() {

        long countExact = controller.countMoviesFrom(movies, "Martin Scorsese");
        assertEquals(2, countExact);
    }

    @Test
    void testCountMoviesFrom_emptyMovieList() {
        List<Movie> emptyList = new ArrayList<>();
        long count = controller.countMoviesFrom(emptyList, "Christopher Nolan");
        assertEquals(0, count, "If list is empty, should be 0.");
    }

    @Test
    void testGetMoviesBetweenYears_inclusiveRange() {
        List<Movie> result = controller.getMoviesBetweenYears(movies, 2010, 2013);
        assertEquals(3, result.size());
    }

    @Test
    void testGetMoviesBetweenYears_noneInRange() {
        List<Movie> result = controller.getMoviesBetweenYears(movies, 2020, 2022);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMoviesBetweenYears_emptyMovieList() {
        List<Movie> emptyList = new ArrayList<>();
        List<Movie> result = controller.getMoviesBetweenYears(emptyList, 1990, 2020);
        assertEquals(0, result.size());
    }

    @Test
    void testGetMoviesBetweenYears_startYearGreaterThanEndYear() {
        List<Movie> result = controller.getMoviesBetweenYears(movies, 2015, 2010);
        assertTrue(result.isEmpty());
    }

}
