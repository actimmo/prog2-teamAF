package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.teamAF.app.Exceptions.DatabaseException;
import com.teamAF.app.Exceptions.MovieApiException;
import com.teamAF.app.Interfaces.Observable;
import com.teamAF.app.Interfaces.Observer;
import com.teamAF.app.Model.Movie;
import com.teamAF.app.View.AutoCloseAlert;
import javafx.scene.control.Alert;
import com.teamAF.app.Model.enums.MyEnum.RepoResponseCode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {
    private List<Observer> observers = new ArrayList<>();
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

    public RepoResponseCode addToWatchlist(MovieEntity movieEntity) throws DatabaseException {
        return addToWatchlist(movieEntity.apiId);
    }

    public RepoResponseCode addToWatchlist(Movie movie) throws DatabaseException {
        return addToWatchlist(movie.getId());
    }

    private RepoResponseCode addToWatchlist(String apiID) throws DatabaseException {
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
        try {
            queryBuilder.where().eq("apiId", apiID);
            List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());
            if (!result.isEmpty()) {
                notifyObserver(apiID, RepoResponseCode.ALREADY_EXISTS);
                return RepoResponseCode.ALREADY_EXISTS;
            } else {
                WatchlistMovieEntity watchListMovieEntity = new WatchlistMovieEntity();
                watchListMovieEntity.apiId = apiID;
                int res = dao.create(watchListMovieEntity);
                if (res > 0) {
                    notifyObserver(apiID, RepoResponseCode.SUCCESS);
                    return RepoResponseCode.SUCCESS;
                } else {
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
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("apiId", apiId);

            List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());

            if (!result.isEmpty()) {
                int res = dao.delete(result);
                if (res > 0) {
                    notifyObserver(apiId, RepoResponseCode.SUCCESS);
                    return RepoResponseCode.SUCCESS;
                } else {
                    notifyObserver(apiId, RepoResponseCode.ERROR);
                    return RepoResponseCode.ERROR;
                }
            } else {
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


