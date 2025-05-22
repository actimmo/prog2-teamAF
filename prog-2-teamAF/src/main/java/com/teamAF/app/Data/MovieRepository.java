package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.teamAF.app.Exceptions.DatabaseException;
import com.teamAF.app.Model.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing movie entities in the database.
 * Implements the Singleton pattern to ensure only one instance exists throughout the application.
 * Handles CRUD operations for movie data using ORMLite for database access.
 */
public class MovieRepository {

    private static MovieRepository instance;
    private Dao<MovieEntity,Long> dao;

    /**
     * Private constructor to enforce the Singleton pattern.
     *
     * @param dao Data access object for MovieEntity persistence
     */
    private MovieRepository(Dao<MovieEntity,Long> dao) {
        this.dao = dao;
    }

    /**
     * Gets the singleton instance of MovieRepository.
     * Creates a new instance if one doesn't exist yet.
     *
     * @param dao Data access object for MovieEntity persistence
     * @return The singleton instance of MovieRepository
     */
    public static MovieRepository getInstance(Dao<MovieEntity,Long> dao) {
        if (instance == null) {
            instance = new MovieRepository(dao);
        }
        return instance;
    }

    public List<MovieEntity> getAllMovies() throws DatabaseException {
        List<MovieEntity> list = new ArrayList<MovieEntity>();
        try {
            list.addAll(dao.queryForAll());
        } catch (SQLException e) {
            throw new DatabaseException("Add all movies to the database failed.");
        }
        return list;
    }

    public int removeAll() throws DatabaseException {
        try{
            return dao.delete(dao.queryForAll());
        }catch (SQLException e){
            throw new DatabaseException("Could not remove all movies from the database.");
        }
    }

    public MovieEntity getMovie(MovieEntity movie) throws SQLException {
        return getMovie(movie.apiId);
    }

    public MovieEntity getMovie(Movie movie) throws SQLException {
        return getMovie(movie.getId());
    }

    public MovieEntity getMovie(String apiId) throws SQLException {
        // Build a query to find movie with matching API ID
        QueryBuilder<MovieEntity, Long> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("apiId", apiId);

        // Execute the query
        List<MovieEntity> result = dao.query(queryBuilder.prepare());

        // Return the first match if found
        if (!result.isEmpty()) {
            return result.get(0);
        }

        // No matching movie found
        throw new SQLException("The apiId does not exists in the DAO.");
    }

    public int addAllMovies(List<Movie> movies) throws DatabaseException {
        List<MovieEntity> errors = new ArrayList<>();
        int count = 0;

        // Process each MovieEntity
        for (MovieEntity movEnt : MovieEntity.fromMovies(movies)){
            try{
                // Add to database
                dao.create(movEnt);
                count++;
            }catch (SQLException e){
                // Track failed entities
                errors.add(movEnt);
            }
        }

        // Report any errors that occurred
        if (!errors.isEmpty()) {
            throw new DatabaseException(errors.size()+" could not been added to the database.");
        }else
            return count;
    }
}