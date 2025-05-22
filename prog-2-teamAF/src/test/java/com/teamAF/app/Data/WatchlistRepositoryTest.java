package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.teamAF.app.Exceptions.DatabaseException;
import com.teamAF.app.Exceptions.MovieApiException;
import com.teamAF.app.Model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class WatchlistRepositoryTest {

    private Movie movie;
    private MovieEntity movieEntity;

    private DatabaseManager dbm;
    Dao<WatchlistMovieEntity,Long> dao;
    WatchlistRepository repo;
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

        try {
            dbm =  DatabaseManager.getTestInstance();
            dao = dbm.getWatchlistDao();
            repo = WatchlistRepository.getInstance(dao);
            for (WatchlistMovieEntity wme : repo.getWatchlist()){
                repo.removeFromWatchlist(wme.apiId);
            }

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void addToWatchlistTest() {
        try {
            repo.addToWatchlist(movieEntity);
            Assertions.assertEquals(1, repo.getWatchlist().size());
            Assertions.assertEquals(movieEntity.apiId, repo.getWatchlist().get(0).apiId);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeFromWatchlistTest() {
        try {
            repo.addToWatchlist(movieEntity);
            Assertions.assertEquals(1, repo.getWatchlist().size());
            repo.removeFromWatchlist(movieEntity.apiId);
            Assertions.assertEquals(0, repo.getWatchlist().size());
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
