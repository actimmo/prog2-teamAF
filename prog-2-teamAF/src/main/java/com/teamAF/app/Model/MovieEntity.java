package com.teamAF.app.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DatabaseTable(tableName = "movies")
public class MovieEntity {

    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(canBeNull = false)
    public String apiId;

    @DatabaseField
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField
    public String genres;

    @DatabaseField
    public int releaseYear;

    @DatabaseField
    public String imgUrl;

    @DatabaseField
    public int lengthInMinutes;

    @DatabaseField
    public double rating;

    public MovieEntity() {
    }

    // Getter & Setter ...

    public static String genresToString(List<String> genres) {
        StringBuilder sb = new StringBuilder();
        for (String genre : genres) {
            sb.append(genre);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        List<MovieEntity> movieEntities = new ArrayList<>();
        for (Movie movie : movies) {
            MovieEntity me = new MovieEntity();
            me.apiId = movie.getId();
            me.title = movie.getTitle();
            me.description = movie.getDescription();
            me.genres = genresToString(movie.getGenreList());
            me.releaseYear = movie.getReleaseYear();
            me.imgUrl = movie.getImgUrl();
            me.lengthInMinutes = movie.getLengthInMinutes();
            me.rating = movie.getRating();
            movieEntities.add(me);
        }

        return movieEntities;
    }

    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        List<Movie> movies = new ArrayList<>();
        for (MovieEntity me : movieEntities) {
            Movie movie = new Movie();
            movie.setId(me.apiId);
            movie.setTitle(me.title);
            movie.setDescription(me.description);
            movie.setGenres(Arrays.stream(me.genres.split(",")).toList());
            movie.setReleaseYear(me.releaseYear);
            movie.setImgUrl(me.imgUrl);
            movie.setLengthInMinutes(me.lengthInMinutes);
            movie.setRating(me.rating);
            movies.add(movie);
        }
        return movies;
    }
}