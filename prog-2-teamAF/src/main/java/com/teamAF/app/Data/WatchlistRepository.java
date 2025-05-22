package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.teamAF.app.Exceptions.DatabaseException;
import com.teamAF.app.Interfaces.Observable;
import com.teamAF.app.Interfaces.Observer;
import com.teamAF.app.Model.Movie;
import com.teamAF.app.Model.enums.MyEnum.RepoResponseCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing watchlist entries in the database.
 * Implements the Singleton pattern to ensure only one instance exists throughout the application.
 * Also implements the Observable interface to notify observers of changes to the watchlist.
 * Handles CRUD operations for watchlist data using ORMLite for database access.
 */
public class WatchlistRepository implements Observable {
    private static WatchlistRepository instance;
    private List<Observer> observers = new ArrayList<>();
    private Dao<WatchlistMovieEntity, Long> dao;

    /**
     * Private constructor to enforce the Singleton pattern.
     *
     * @param dao Data access object for WatchlistMovieEntity persistence
     */
    private WatchlistRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    /**
     * Gets the singleton instance of WatchlistRepository.
     * Creates a new instance if one doesn't exist yet.
     *
     * @param dao Data access object for WatchlistMovieEntity persistence
     * @return The singleton instance of WatchlistRepository
     */
    public static WatchlistRepository getInstance(Dao<WatchlistMovieEntity, Long> dao) {
        if (instance == null) {
            instance = new WatchlistRepository(dao);
        }
        return instance;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Could not get the watchlist from the database.");
        }
    }

    public RepoResponseCode addToWatchlist(MovieEntity movieEntity) throws DatabaseException {
        return addToWatchlist(movieEntity.apiId);
    }

    public RepoResponseCode addToWatchlist(Movie movie) throws DatabaseException {
        return addToWatchlist(movie.getId());
    }

    private RepoResponseCode addToWatchlist(String apiID) throws DatabaseException {
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
        try {
            // Check if movie already exists in watchlist
            queryBuilder.where().eq("apiId", apiID);
            List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());

            if (!result.isEmpty()) {
                // Movie is already in watchlist
                notifyObserver(apiID, RepoResponseCode.ALREADY_EXISTS);
                return RepoResponseCode.ALREADY_EXISTS;
            } else {
                // Create new watchlist entry
                WatchlistMovieEntity watchListMovieEntity = new WatchlistMovieEntity();
                watchListMovieEntity.apiId = apiID;
                int res = dao.create(watchListMovieEntity);

                if (res > 0) {
                    // Success - movie added
                    notifyObserver(apiID, RepoResponseCode.SUCCESS);
                    return RepoResponseCode.SUCCESS;
                } else {
                    // Failed to add movie
                    notifyObserver(apiID, RepoResponseCode.ERROR);
                    return RepoResponseCode.ERROR;
                }
            }
        } catch (SQLException e) {
            notifyObserver(apiID, RepoResponseCode.ERROR);
            throw new DatabaseException("Could not add the movie to the watchlist.");
        }
    }

    public RepoResponseCode removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            // Find the movie in the watchlist
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("apiId", apiId);
            List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());

            if (!result.isEmpty()) {
                // Movie found - attempting to delete
                int res = dao.delete(result);
                if (res > 0) {
                    // Successfully removed
                    notifyObserver(apiId, RepoResponseCode.SUCCESS);
                    return RepoResponseCode.SUCCESS;
                } else {
                    // Failed to remove
                    notifyObserver(apiId, RepoResponseCode.ERROR);
                    return RepoResponseCode.ERROR;
                }
            } else {
                // Movie not found in watchlist
                notifyObserver(apiId, RepoResponseCode.NOT_FOUND);
                return RepoResponseCode.NOT_FOUND;
            }
        } catch (SQLException e) {
            notifyObserver(apiId, RepoResponseCode.ERROR);
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String apiID, RepoResponseCode responseCode) {
        for (Observer observer : observers) {
            observer.update(apiID, responseCode);
        }
    }
}