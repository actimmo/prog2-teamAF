package com.teamAF.app.Model;

import com.github.eventmanager.EventManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.teamAF.app.FhmdbApplication;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MovieService {
    private final List<Movie> allMovies;
    private EventManager eventManager;
    private MovieAPI movieAPI;

    // Initialize allMovies from API
    public MovieService(EventManager eventManager) {
        this.eventManager = eventManager;
        this.movieAPI = new MovieAPI(eventManager);
        this.allMovies = movieAPI.getMovies();
    }

    // Defensive copy for testing purposes
    public MovieService(List<Movie> movies) {
        this.allMovies = new ArrayList<>(movies);
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }

    /**
     * Filters movies based on multiple criteria including genres, search query, years, and ratings.
     * Duplicate movies (based on title) are removed from the results.
     *
     * @param selectedGenres List of genre strings to filter by. Movies must contain at least one of these genres.
     *                      If empty or null, genre filtering is skipped.
     * @param searchQuery String to search for in movie titles and descriptions (case-insensitive).
     *                   If empty or null, text search is skipped.
     * @param years List of release years to filter by. Movies must match one of these years.
     *             If empty or null, year filtering is skipped.
     * @param ratings List of rating values to filter by. Movies must match one of these ratings.
     *               If empty or null, rating filtering is skipped.
     * @return List of Movie objects that match all the provided filter criteria.
     *         Duplicates are removed based on case-insensitive title comparison.
     */
    public List<Movie> filterMovies(List<String> selectedGenres, String searchQuery, List<String> years, List<String> ratings) {
        // Set to track seen titles and avoid duplicates
        Set<String> seenTitles = new HashSet<>();
        // List to store filtered results
        List<Movie> filteredMovies = new ArrayList<>();

        // Check which filters are active
        boolean hasGenres = selectedGenres != null && !selectedGenres.isEmpty() && !selectedGenres.get(0).isEmpty();
        boolean hasQuery = searchQuery != null && !searchQuery.isEmpty();
        boolean hasYears = years != null && !years.isEmpty();
        boolean hasRatings = ratings != null && !ratings.isEmpty();
        Set<Double> ratingValues = hasRatings ? ratings.stream().map(Double::parseDouble).collect(Collectors.toSet()) : Collections.emptySet();

        // Check each movie against active filters
        for (Movie movie : allMovies) {
            boolean matchesGenre = !hasGenres || movie.getGenreList().stream().anyMatch(selectedGenres::contains);
            boolean matchesQuery = !hasQuery ||
                    (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(searchQuery.toLowerCase()));
            boolean matchesYear = !hasYears || years.contains(String.valueOf(movie.getReleaseYear()));
            boolean matchesRating = !hasRatings || ratingValues.contains(movie.getRating());

            // Add movie if it matches all criteria and hasn't been seen before
            if (matchesGenre && matchesQuery && matchesYear && matchesRating && seenTitles.add(movie.getTitle().toLowerCase())) {
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

    public List<Movie> fromJson2List(String json){
        Gson gson = new Gson();
        Type movieListType = new TypeToken<List<Movie>>(){}.getType();

        try {
            List<Movie> movies = gson.fromJson(json, movieListType);
            return movies;

        } catch (JsonSyntaxException e) {
            Movie movie = fromJson2Movie(json);
            return new ArrayList<>(){{if (movie != null) add(movie);}};
        }
    }

    protected Movie fromJson2Movie(String json){
        Gson gson = new Gson();
        try {
            Movie movie = gson.fromJson(json, Movie.class);
           return movie;
        } catch (JsonSyntaxException ex) {
            System.out.println("Failed to parse JSON");
        }
        return null;
    }

    public String fromList2Json(List<Movie> movies){
        Gson gson = new Gson();
        return gson.toJson(movies);
    }
}
