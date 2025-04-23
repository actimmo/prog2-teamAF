package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.teamAF.app.Model.MovieEntity;
import com.teamAF.app.Model.WatchlistMovieEntity;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {

    private Dao<WatchlistMovieEntity,Long> dao;
    public WatchlistRepository(Dao<WatchlistMovieEntity,Long> dao){
        this.dao = dao;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return dao.queryForAll();
    }

    public int addToWatchlist(MovieEntity movieEntity) throws SQLException {

        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("apiId", movieEntity.apiId);

        List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());

        if (!result.isEmpty()) {
            System.out.println("The apiId exists in the DAO.");
        } else {
            WatchlistMovieEntity wlme = new WatchlistMovieEntity();
            wlme.apiId = movieEntity.apiId;
           return dao.create(wlme);
        }
        return -1;

    }

    public int removeFromWatchlist(String apiId) throws SQLException {
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
        queryBuilder.where().eq("apiId", apiId);

        List<WatchlistMovieEntity> result = dao.query(queryBuilder.prepare());

        if (!result.isEmpty()) {
            return dao.delete(result);
        } else {
            System.out.println("The apiId does not exists in the DAO.");
        }
        return -1;
    }
}
