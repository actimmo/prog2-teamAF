package com.teamAF.app.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

}
