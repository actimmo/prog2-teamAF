package com.teamAF.app.Model;

import com.teamAF.app.FhmdbApplication;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Movie {
    private String id;
    private String title;
    private String description;
    private List<String> genres;
    private int releaseYear;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;
    private double rating;


    public Movie(String title, String description, List<String> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public Movie() {}
    public Movie(String id, String title, String description, List<String> genres, int releaseYear, String imgUrl, int lengthInMinutes, List<String> directors, List<String> writers, List<String> mainCast, double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Getter for the genres list
     * @return a string representation of the genres list, separated by commas
     * e.g. "Action, Adventure, Comedy"
     * */
    public String getGenres() {
        StringBuilder sb = new StringBuilder();
        String[] genresArray = genres.toArray(new String[0]);
        for (int j = 0; j < genresArray.length; j++) {
            sb.append(genresArray[j]);
            if (j < genresArray.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public List<String> getGenreList() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

    // Initialize allMovies from JSON
    public static List<Movie> initializeMoviesDummyMoviesFromJson(){
        List<Movie> movies = new ArrayList<>();
        try{
            Path path = Paths.get(FhmdbApplication.class.getClassLoader().getResource("movies.json").toURI());
            String jsonText = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonText);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String desc = jsonObject.getString("description");
                JSONArray genresArray = jsonObject.getJSONArray("genres");
                List<String> genres = new ArrayList<>();
                for (int j = 0; j < genresArray.length(); j++) {
                    genres.add(genresArray.getString(j));
                }

                Movie movie = new Movie( title, desc, genres);
                movies.add(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }


    @Override
    public boolean equals(Object compared) {
        if (this == compared) return true;
        if (compared == null || getClass() != compared.getClass()) return false;
        Movie movie = (Movie) compared;
        return releaseYear == movie.releaseYear &&
                lengthInMinutes == movie.lengthInMinutes &&
                Double.compare(movie.rating, rating) == 0 &&
                Objects.equals(id, movie.id) &&
                Objects.equals(title, movie.title) &&
                Objects.equals(description, movie.description) &&
                Objects.equals(genres, movie.genres) &&
                Objects.equals(imgUrl, movie.imgUrl) &&
                Objects.equals(directors, movie.directors) &&
                Objects.equals(writers, movie.writers) &&
                Objects.equals(mainCast, movie.mainCast);
    }
}