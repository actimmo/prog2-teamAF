package com.teamAF.app.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MovieTest {

    private Movie movie;
    private Movie moviefullprops;

    @BeforeEach
    void setUp() {
        movie = new Movie("Inception", "A mind-bending thriller",
                        Arrays.asList("SCIENCE_FICTION", "THRILLER"));

        moviefullprops = new Movie("e2d9913d-3869-454c-9fbf-a63aaf57bedf",
                "The Matrix",
                "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                Arrays.asList("ACTION", "SCIENCE_FICTION"),
                1999,
                "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                136,
                List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Lana Wachowski", "Lilly Wachowski"),
                Arrays.asList("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
                8.7);
    }

    @Test
    void getTitle() {
        assertEquals("Inception", movie.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("A mind-bending thriller", movie.getDescription());
    }


    @Test
    void getGenreList() {
        assertEquals(Arrays.asList("SCIENCE_FICTION", "THRILLER"), movie.getGenreList());
    }

    @Test
    void getGenres() {
        assertEquals("SCIENCE_FICTION, THRILLER", movie.getGenres());
    }


    @Test
    void initializeMoviesDummyMoviesFromJson() {
        assertEquals(25, Movie.initializeMoviesDummyMoviesFromJson().size());

    }

    @Test
    void equalsTrue() {
        assertEquals(movie, movie);

    }

    @Test
    void equalsFullPropsTrue() {
        Movie moviefullpropstest = new Movie("e2d9913d-3869-454c-9fbf-a63aaf57bedf",
                "The Matrix",
                "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                Arrays.asList("ACTION", "SCIENCE_FICTION"),
                1999,
                "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                136,
                List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Lana Wachowski", "Lilly Wachowski"),
                Arrays.asList("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
                8.7);

        assertEquals(moviefullprops, moviefullpropstest);

    }

    @Test
    void equalsFullPropsFalse() {
        Movie moviefullpropstest = new Movie("e2d9913d-3869-454c-9fbf-a63aaf57bedf",
                "The Matrix",
                "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                Arrays.asList("ACTION", "SCIENCE_FICTION"),
                1999,
                "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                136,
                List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Lana Wachowski", "Lilly Wachowski"),
                Arrays.asList("Arnold Schwarzenegger", "Silvester Stallone"),
                8.7);
        assertNotEquals(moviefullprops, moviefullpropstest);

    }



}
