package com.teamAF.app.Model;

import com.github.eventmanager.EventManager;
import com.teamAF.app.FhmdbApplication;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MovieService {
    private final List<Movie> allMovies;
    private EventManager eventManager;

    // Initialize allMovies from JSON
    public MovieService(EventManager eventManager) {
        this.eventManager = eventManager;
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
            eventManager.logErrorMessage("Error initializing movies from JSON");
        }
        this.allMovies = movies;
    }

    // Defensive copy for testing purposes
    public MovieService(List<Movie> movies) {
        this.allMovies = new ArrayList<>(movies);
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }

    public List<Movie> filterMovies(List<String> selectedGenres, String searchQuery) {
        Set<String> seenTitles = new HashSet<>();
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : allMovies) {
            boolean matchesGenre = selectedGenres == null || selectedGenres.isEmpty() || selectedGenres.get(0).isEmpty();

            for (int i = 0; i < movie.getGenreList().size() && (!matchesGenre); i++) {
                matchesGenre = selectedGenres.contains(movie.getGenreList().get(i));
            }

            boolean matchesQuery = searchQuery == null || searchQuery.isEmpty() || movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase())
                    || (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(searchQuery.toLowerCase()));

            if (matchesGenre && matchesQuery && seenTitles.add(movie.getTitle().toLowerCase())) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

    public List<Movie> sortMoviesAscending(List<Movie> movies) {
        List<Movie> sorted = new ArrayList<>(movies);
        sorted.sort(Comparator.comparing(m -> m.getTitle().toLowerCase()));
        return sorted;
    }

    public List<Movie> sortMoviesDescending(List<Movie> movies) {
        List<Movie> sorted = new ArrayList<>(movies);
        sorted.sort(Comparator.comparing((Movie m) -> m.getTitle().toLowerCase()).reversed());
        return sorted;
    }
}
