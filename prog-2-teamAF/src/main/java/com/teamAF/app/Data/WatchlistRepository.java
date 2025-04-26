package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.teamAF.app.Exceptions.DatabaseException;
import com.teamAF.app.Exceptions.MovieApiException;
import com.teamAF.app.Model.Movie;
import com.teamAF.app.View.AutoCloseAlert;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {

    private Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Could not get the watchlist from the database.");
        }
    }

    public int addToWatchlist(MovieEntity movieEntity) throws MovieApiException {
        return addToWatchlist(movieEntity.apiId);
    }

    public int addToWatchlist(Movie movie) throws MovieApiException {
        return addToWatchlist(movie.getId());
    }

    public int addToWatchlist(String apiID) throws MovieApiException {

        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();


        try {
            queryBuilder.where().eq("apiId", apiID);
            List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());
            if (!result.isEmpty()) {
                System.out.println("The apiId exists in the DAO.");
                new AutoCloseAlert("INFO", "WachListMovies", "The movie is already in the Watchlist", Alert.AlertType.INFORMATION, 2).create();
            } else {
                WatchlistMovieEntity watchListMovieEntity = new WatchlistMovieEntity();
                watchListMovieEntity.apiId = apiID;
                return dao.create(watchListMovieEntity);
            }
            return -1;
        } catch (SQLException e) {
            throw new MovieApiException("Could not add the movie to the watchlist.");
        }
    }

    public int removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("apiId", apiId);

            List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());

            if (!result.isEmpty()) {
                return dao.delete(result);
            } else {
                System.out.println("The apiId does not exists in the DAO.");
            }
            return -1;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
