package com.teamAF.app.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTest {


    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie("Inception", "A mind-bending thriller",
                        Arrays.asList("SCIENCE_FICTION", "THRILLER"));
    }

    @Test
    void getTitle() {
        assertEquals(movie.getTitle(),  "Inception");
    }

    @Test
    void getDescription() {
        assertEquals(movie.getDescription(),  "A mind-bending thriller");
    }


    @Test
    void getGenreList() {
        assertEquals(movie.getGenreList(),  Arrays.asList("SCIENCE_FICTION", "THRILLER"));
    }

    @Test
    void getGenres() {
        assertEquals(movie.getGenres(),  "SCIENCE_FICTION, THRILLER");
    }


    @Test
    void initializeMoviesDummyMoviesFromJson() {
        assertEquals(Movie.initializeMoviesDummyMoviesFromJson().size(),  25);

    }

}
