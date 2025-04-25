package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.teamAF.app.Model.Movie;
import com.teamAF.app.Model.MovieEntity;
import com.teamAF.app.Model.WatchlistMovieEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    Dao<MovieEntity,Long> dao;
    public MovieRepository(Dao<MovieEntity,Long> dao) {
        this.dao = dao;
    }

    public List<MovieEntity> getAllMovies() throws SQLException {
        List<MovieEntity> list = new ArrayList<MovieEntity>();
        list.addAll( dao.queryForAll());
        return list;
    }

    public int removeAll() throws SQLException {
        return dao.delete(dao.queryForAll());
    }
    public MovieEntity getMovie(MovieEntity movie) throws SQLException {
        return getMovie(movie.apiId);
    }

    public MovieEntity getMovie(Movie movie) throws SQLException {
        return getMovie(movie.getId());
    }
    public MovieEntity getMovie(String apiId) throws SQLException {
        QueryBuilder<MovieEntity, Long> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("apiId", apiId);

        List<MovieEntity> result = dao.query(queryBuilder.prepare());

        if (!result.isEmpty()) {
            return result.get(0);
        }

        throw new SQLException("The apiId does not exists in the DAO.");
    }

    public int addAllMovies(List<Movie> movies) throws SQLException {
        List<MovieEntity> errors = new ArrayList<>();
        int count = 0;
        for (MovieEntity movEnt : MovieEntity.fromMovies(movies)){
            try{
                dao.create(movEnt);
                count++;
            }catch (SQLException e){
                errors.add(movEnt);
            }
        }

        if (!errors.isEmpty()) {
           throw new SQLException(errors.size()+" could not been added to the database.");
        }else
            return count;
    }
}
