package com.teamAF.app.Model;

import com.github.eventmanager.EventManager;
import com.github.eventmanager.filehandlers.LogHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MovieAPITest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    private EventManager eventManager;
    private MovieAPI movieAPI;

    @BeforeEach
    void setUp() {
        LogHandler logHandler = new LogHandler("");
        logHandler.getConfig().getEvent().setPrintToConsole(true);
        logHandler.getConfig().getEvent().setInformationalMode(true);
        logHandler.getConfig().getInternalEvents().setEnabled(false);
        this.eventManager = new EventManager(logHandler);

        // Initialize mocks to simulate HTTP requests
        MockitoAnnotations.openMocks(this);
        this.movieAPI = new MovieAPI(eventManager, mockHttpClient);
    }

    @Test
    void getMovies_success() throws IOException, InterruptedException {
        MovieAPI movieAPI = new MovieAPI(eventManager);

        List<Movie> movies = movieAPI.getMovies();
        assertEquals(31, movies.size());
    }

    @Test
    void getMovies_IOException() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException());

        List<Movie> movies = this.movieAPI.getMovies();
        assertNull(movies, "Should return null when API responds with 404");
    }

    @Test
    void getMovies_NotFound() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(404);

        List<Movie> movies = this.movieAPI.getMovies();
        assertNull(movies, "Should return null when API responds with 404");
    }


    @Test
    void getMovieByID_success() {
        MovieAPI movieAPI = new MovieAPI(eventManager);

        List<Movie> movies = movieAPI.getMovies();
        Movie movie = movies.get(0);

        Movie responseMovie = movieAPI.getMovieByID(movie.getId());
        assertEquals(movie.getId(), responseMovie.getId());
    }

    @Test
    void getMovieByID_ServerError() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);

        Movie movie = this.movieAPI.getMovieByID("1");
        assertNull(movie, "Should return null when API responds with 500");
    }

    @Test
    void getMovies_with_query_param_success() throws IOException, InterruptedException {
        MovieAPI movieAPI = new MovieAPI(eventManager);
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("query","life is");


        List<Movie> movies = movieAPI.getMoviesWithParams(queryParams);
        assertEquals(1, movies.size());
    }

    @Test
    void getMovies_with_genre_param_success() throws IOException, InterruptedException {
        MovieAPI movieAPI = new MovieAPI(eventManager);
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("genre","WAR");


        List<Movie> movies = movieAPI.getMoviesWithParams(queryParams);
        assertEquals(2, movies.size());
    }

    @Test
    void getMovies_with_releaseYear_param_success() throws IOException, InterruptedException {
        MovieAPI movieAPI = new MovieAPI(eventManager);
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("releaseYear","1998");


        List<Movie> movies = movieAPI.getMoviesWithParams(queryParams);
        assertEquals(1, movies.size());
    }

    @Test
    void getMovies_with_rating_param_success() throws IOException, InterruptedException {
        MovieAPI movieAPI = new MovieAPI(eventManager);
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("ratingFrom","9.3");


        List<Movie> movies = movieAPI.getMoviesWithParams(queryParams);
        assertEquals(1, movies.size());
    }

    @Test
    void getMovies_with_all_params_success() throws IOException, InterruptedException {
        MovieAPI movieAPI = new MovieAPI(eventManager);
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("query","saving pri");
        queryParams.put("releaseYear","1998");
        queryParams.put("genre","WAR");
        queryParams.put("ratingFrom","8.6");


        List<Movie> movies = movieAPI.getMoviesWithParams(queryParams);
        assertEquals(1, movies.size());
    }

    @Test
    void getMovies_with_empty_params_success() throws IOException, InterruptedException {
        MovieAPI movieAPI = new MovieAPI(eventManager);
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("query","");
        queryParams.put("releaseYear","");
        queryParams.put("genre","");
        queryParams.put("rating","");


        List<Movie> movies = movieAPI.getMoviesWithParams(queryParams);
        assertEquals(31, movies.size());
    }
}
