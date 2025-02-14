package com.teamAF.app.models;

import com.teamAF.app.FhmdbApplication;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Movie {
    private String title;
    private String description;
    // TODO add more properties here

    public Movie(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

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
                Movie movie = new Movie(title, desc);
                movies.add(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}
