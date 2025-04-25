package com.teamAF.app.Model;

import com.teamAF.app.Data.MovieEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieEntityTest {

    private Movie movie;
    private MovieEntity movieEntity;

    @BeforeEach
    void setUp() {

        movie = new Movie("e2d9913d-3869-454c-9fbf-a63aaf57bedf",
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

        movieEntity = new MovieEntity();
        movieEntity.apiId = movie.getId();
        movieEntity.title = movie.getTitle();
        movieEntity.description = movie.getDescription();
        movieEntity.genres = String.join(",", movie.getGenreList());
        movieEntity.releaseYear = movie.getReleaseYear();
        movieEntity.imgUrl = movie.getImgUrl();
        movieEntity.lengthInMinutes = movie.getLengthInMinutes();
        movieEntity.rating = movie.getRating();
    }

    @Test
    void genresToStringTest(){
        Assertions.assertEquals(movieEntity.genres, MovieEntity.genresToString(movie.getGenreList()));
    }

    @Test()
    void fromMoviesTest(){
        List<MovieEntity> movieEntitiesList = new ArrayList<MovieEntity>();
        movieEntitiesList.add(movieEntity);
        List<MovieEntity> movieEntitiesList2 = MovieEntity.fromMovies(Arrays.asList(movie));
        Assertions.assertIterableEquals(movieEntitiesList,movieEntitiesList2);

    }

    @Test()
    void toMoviesTest(){
        List<Movie> movieList = new ArrayList<Movie>();
        Movie movietest = new Movie();
        movietest.setId(movie.getId());
        movietest.setTitle(movie.getTitle());
        movietest.setDescription(movie.getDescription());
        movietest.setGenres(movie.getGenreList());
        movietest.setReleaseYear(movie.getReleaseYear());
        movietest.setImgUrl(movie.getImgUrl());
        movietest.setLengthInMinutes(movie.getLengthInMinutes());
        movietest.setRating(movie.getRating());

        movieList.add(movietest);
        List<Movie> movieList2 = MovieEntity.toMovies(Arrays.asList(movieEntity));
        Assertions.assertIterableEquals(movieList,movieList2);

    }
}
