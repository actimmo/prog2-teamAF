package com.teamAF.app.Model;

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


    public MovieService() {
        List<Movie> movies = Movie.initializeMoviesDummyMoviesFromJson();

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

        boolean hasGenres = selectedGenres != null && !selectedGenres.isEmpty() && !selectedGenres.get(0).isEmpty();
        boolean hasQuery = searchQuery != null && !searchQuery.isEmpty();

        for (Movie movie : allMovies) {
            boolean matchesGenre = !hasGenres || movie.getGenreList().stream().anyMatch(selectedGenres::contains);

            boolean matchesQuery = !hasQuery ||
                    (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(searchQuery.toLowerCase()));

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
