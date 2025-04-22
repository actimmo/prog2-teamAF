package com.teamAF.app.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "WatchlistMovie")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(canBeNull = false)
    public String apiId;

    public WatchlistMovieEntity() {
    }
}
