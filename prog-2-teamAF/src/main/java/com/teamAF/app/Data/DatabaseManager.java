package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.teamAF.app.Exceptions.DatabaseException;

import java.sql.SQLException;

public class DatabaseManager {

    private static String DB_URL = "jdbc:h2:file:./dbMovie";
    private static String username = "user";
    private static String password = "pass";

    private static ConnectionSource conn;

    private Dao<MovieEntity, Long> movieDao;

    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    private static DatabaseManager instance;

    private  DatabaseManager() throws DatabaseException {
        createConnectionSource();
        createTables();
    }

    public static DatabaseManager getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static DatabaseManager getTestInstance() throws DatabaseException {
        DB_URL = "jdbc:h2:file:./dbMovieTest";
        return getInstance();
    }

    private void createConnectionSource() throws DatabaseException {
        try {
            conn = new JdbcConnectionSource(DB_URL, username, password);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    private void createTables() throws DatabaseException {
        try{
            movieDao = DaoManager.createDao(conn, MovieEntity.class);
            watchlistDao = DaoManager.createDao(conn, WatchlistMovieEntity.class);
            TableUtils.createTableIfNotExists(conn, MovieEntity.class);
            TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }
}
