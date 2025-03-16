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
    private int rating;

    public Movie(String title, String description, List<String> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public Movie() {
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
    };

    public static List<Movie> initializeMovies(){
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
                Movie movie = new Movie(title, desc, genres);
                movies.add(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}
