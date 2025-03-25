package com.teamAF.app.Model;

import com.google.gson.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieServiceTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    private MovieService movieService;
    private List<Movie> allMovies;

    private Movie movieWithFullProps;
    private String jsonMovieListWithFullProps;
    private String jsonMovieWithFullProps;

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

        movieWithFullProps =  new Movie(
                "81d317b0-29e5-4846-97a6-43c07f3edf4a",
                "The Godfather",
                "The aging patriarch of an organized crime dynasty transfers control of " +
                        "his clandestine empire to his reluctant son.",
                new ArrayList<String>() {{
                    add("DRAMA");
                }},
                1972,
                "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                175,
                new ArrayList<String>() {{
                    add("Francis Ford Coppola");
                }},
                new ArrayList<String>() {{
                    add("Mario Puzo");
                    add("Francis Ford Coppola");
                }},
                new ArrayList<String>() {{
                    add("Marlon Brando");
                    add("Al Pacino");
                    add("James Caan");
                }},
                9.2
        );

        jsonMovieListWithFullProps = "[{\"id\":\"81d317b0-29e5-4846-97a6-43c07f3edf4a\",\"title\":\"The Godfather\",\"description\":\"The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.\",\"genres\":[\"DRAMA\"],\"releaseYear\":1972,\"imgUrl\":\"https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg\",\"lengthInMinutes\":175,\"directors\":[\"Francis Ford Coppola\"],\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"],\"mainCast\":[\"Marlon Brando\",\"Al Pacino\",\"James Caan\"],\"rating\":9.2}]";
        jsonMovieWithFullProps = "{\"id\":\"81d317b0-29e5-4846-97a6-43c07f3edf4a\",\"title\":\"The Godfather\",\"description\":\"The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.\",\"genres\":[\"DRAMA\"],\"releaseYear\":1972,\"imgUrl\":\"https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg\",\"lengthInMinutes\":175,\"directors\":[\"Francis Ford Coppola\"],\"writers\":[\"Mario Puzo\",\"Francis Ford Coppola\"],\"mainCast\":[\"Marlon Brando\",\"Al Pacino\",\"James Caan\"],\"rating\":9.2}";

    }

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        movieService = new MovieService(Movie.initializeMoviesDummyMoviesFromJson());
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void getAllMovies() {
        assertEquals(movieService.getAllMovies(),  allMovies);
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
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "Matrix",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withMatchingDescription() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "hacker",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withPartialMatch() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "Dark",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(1, filtered.size());
        assertEquals("The Dark Knight", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatch() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "nonexistant",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(0, filtered.size());
    }

    @Test
    void filterMovies_withEmptyQuery() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(5, filtered.size());
    }

    @Test
    void filterMovies_withMatchingGenreAndQuery() {
        List<Movie> filtered = movieService.filterMovies(List.of("SCIENCE_FICTION"), "Matrix",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withNoMatchingGenreAndQuery() {
        List<Movie> filtered = movieService.filterMovies(List.of("COMEDY"), "Matrix",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(0, filtered.size());
    }

    @Test
    void filterMovies_withMatchingGenreOnly() {
        List<Movie> filtered = movieService.filterMovies(List.of("SCIENCE_FICTION"), "",
                Collections.emptyList(), Collections.emptyList());

        // In the test data, that should match "Inception" and "The Matrix"
        assertEquals(2, filtered.size());
        assertEquals("Inception", filtered.get(0).getTitle());
        assertEquals("The Matrix", filtered.get(1).getTitle());
    }

    @Test
    void filterMovies_withMatchingQueryOnly() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "Matrix",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(1, filtered.size());
        assertEquals("The Matrix", filtered.get(0).getTitle());
    }

    @Test
    void filterMovies_withEmptyGenreAndQuery() {
        List<Movie> filtered = movieService.filterMovies(Collections.emptyList(), "",
                Collections.emptyList(), Collections.emptyList());

        assertEquals(5, filtered.size());
    }

    @Test
    void filterMovies_with2Genres() {
        List<Movie> filtered = movieService.filterMovies(List.of("SCIENCE_FICTION", "ACTION"), "",
                Collections.emptyList(), Collections.emptyList());
        assertEquals(3, filtered.size());
        assertEquals("Inception", filtered.get(0).getTitle());
        assertEquals("The Dark Knight", filtered.get(1).getTitle());
        assertEquals("The Matrix", filtered.get(2).getTitle());
    }

    @Test
    void filterMovies_withNoMatchingGenreAndEmptyQuery() {
        List<Movie> filtered = movieService.filterMovies(List.of("ANIMATION"), "",
                Collections.emptyList(), Collections.emptyList());
        assertEquals(0, filtered.size());
    }

    @Test
    void filterMovies_withNoMatchingGenreAndMatchingQuery() {
        List<Movie> filtered = movieService.filterMovies(List.of("ANIMATION"), "Matrix",
                Collections.emptyList(), Collections.emptyList()); {
            assertEquals(0, filtered.size());
        }
    }

    @Test
    void fromJson2List(){
        List<Movie> moviesFromjson = movieService.fromJson2List(jsonMovieListWithFullProps);
        List<Movie> expectedResult = new ArrayList<>();
        expectedResult.add(movieWithFullProps);
        assertEquals(moviesFromjson, expectedResult);
    }

    @Test
    void fromJson2ListWrongJson(){
        List<Movie> moviesFromjson = movieService.fromJson2List(jsonMovieWithFullProps);
        List<Movie> expectedResult = new ArrayList<>();
        expectedResult.add(movieWithFullProps);
        assertEquals(moviesFromjson, expectedResult);
    }

    @Test
    void fromJson2Movie(){
        Movie movieFromjson = movieService.fromJson2Movie(jsonMovieWithFullProps);
        assertEquals(movieFromjson, movieWithFullProps);
    }

    @Test
    void fromJson2MovieIncorrectJson() {
        movieService.fromJson2Movie("asdf");

        assertTrue(outContent.toString().contains("Failed to parse JSON"));
    }

    @Test
    void  fromList2Json(){
        List<Movie> source = new ArrayList<>();
        source.add(movieWithFullProps);
        String jsonFromList = movieService.fromList2Json(source);
        assertEquals(jsonMovieListWithFullProps, jsonFromList);

    }
}
