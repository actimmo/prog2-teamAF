package com.teamAF.app.Data;
import com.j256.ormlite.dao.Dao;

import com.teamAF.app.Exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import java.sql.SQLException;

public class DatabaseManagerTest {

    private DatabaseManager dbm;
    @BeforeEach
    void setUp() {
        try {
            dbm =  DatabaseManager.getTestInstance();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getMovieDao(){
        assertInstanceOf(Dao.class, dbm.getMovieDao());
    }

    @Test
    void getWatchlistMovieDao(){
        assertInstanceOf(Dao.class, dbm.getWatchlistDao());
    }
}
