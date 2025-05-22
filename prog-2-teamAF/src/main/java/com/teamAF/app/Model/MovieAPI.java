package com.teamAF.app.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eventmanager.EventManager;
import com.teamAF.app.Exceptions.MovieApiException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class that provides methods to interact with the movie API service.
 * The class contains methods to retrieve a list of movies and a single movie by its ID.
 */
public class MovieAPI {
    /**
     * The URL of the movie API service.
     */
    private static final String URL = "https://prog2.fh-campuswien.ac.at/movies";
    public static final int SUCCESS = 200;
    public static final int REDIRECTED = 300;
    private EventManager eventManager;
    private HttpClient httpClient;

    public MovieAPI(EventManager eventManager) {
        this.eventManager = eventManager;
        this.httpClient = HttpClient.newHttpClient();
    }

    public MovieAPI(EventManager eventManager, HttpClient httpClient) {
        this.eventManager = eventManager;
        this.httpClient = httpClient;
    }

    public boolean isApiAvailable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .GET()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            return response.statusCode() >= SUCCESS && response.statusCode() < REDIRECTED;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Retrieves a list of movies from the specified URL.
     * Sends an HTTP GET request to the URL and parses the response as a list of {@link Movie} objects.
     * If the request is successful (status code 200), the response body is parsed into a list of {@link Movie} objects.
     * If the request fails, an appropriate error message is logged, and null is returned.
     *
     * @return A list of {@link Movie} objects, or null if an error occurs during the request or parsing.
     */
    public List<Movie> getMovies() throws MovieApiException {
        HttpResponse<String> response;
        HttpClient client = this.httpClient;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .GET()
                    .build();
            // Send request and get response
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == SUCCESS) {
                this.eventManager.logInfoMessage("Successfully retrieved movies.");
                return loadJSON(response);
            } else {
                logFailedResponseCode(response);
                return null;
            }
        } catch (ConnectException e){
            this.eventManager.logErrorMessage("Connection failed");
            throw new MovieApiException(e.getMessage());
        } catch (IOException e) {
            this.eventManager.logErrorMessage("IO Exception");
            throw new MovieApiException(e.getMessage());
        } catch (InterruptedException e) {
            this.eventManager.logErrorMessage("Interrupted Exception");
            throw new MovieApiException(e.getMessage());
        } catch (IllegalArgumentException e){
            this.eventManager.logErrorMessage("Invalid URL");
            throw new MovieApiException(e.getMessage());
        }
    }

    /**
     * Retrieves a list of movies from the API based on the provided query parameters.
     * Constructs a URL using the `MovieAPIRequestBuilder` with the given parameters,
     * sends an HTTP GET request, and parses the response into a list of `Movie` objects.
     * If the request fails, appropriate error messages are logged, and an exception is thrown.
     *
     * @param queryParams A map containing query parameters such as "query", "genre", "releaseYear", and "ratingFrom".
     * @return A list of `Movie` objects that match the query parameters, or null if the request fails.
     * @throws MovieApiException If an error occurs during the request or response processing.
     */
    public List<Movie> getMoviesWithParams(Map<String, String> queryParams) throws MovieApiException {
        HttpResponse<String> response;
        try {
            // Build the URL with the provided query parameters
            String url = new MovieAPIRequestBuilder(URL)
                    .debug() // Debug-Modus aktivieren
                    .query(queryParams.get("query"))
                    .genre(queryParams.get("genre"))
                    .releaseYear(queryParams.get("releaseYear"))
                    .ratingFrom(queryParams.get("ratingFrom"))
                    .build();

            // Create an HTTP GET request with the constructed URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and capture the response
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code indicates success
            if (response.statusCode() == SUCCESS) {
                this.eventManager.logInfoMessage("Successfully retrieved movies with parameters.");
                // Parse the response body into a list of Movie objects
                return loadJSON(response);
            } else {
                // Log an error message for non-successful status codes
                logFailedResponseCode(response);
                return null;
            }
        } catch (ConnectException e) {
            // Log and throw an exception if the connection fails
            this.eventManager.logErrorMessage("Connection failed");
            throw new MovieApiException(e.getMessage());
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            // Log and throw an exception for other errors
            this.eventManager.logErrorMessage(e.getClass().getSimpleName());
            throw new MovieApiException(e.getMessage());
        }
    }

    /**
     * Retrieves a movie by its unique ID from the API.
     * Constructs a URL using the `MovieAPIRequestBuilder` with the given ID,
     * sends an HTTP GET request, and parses the response into a `Movie` object.
     * If the request fails, an appropriate error message is logged, and null is returned.
     *
     * @param id The unique identifier of the movie to retrieve.
     * @return A `Movie` object corresponding to the given ID, or null if the request fails.
     */
    public Movie getMovieByID(String id) {
        HttpResponse<String> response;
        try {
            // Build the URL with the provided movie ID
            String url = new MovieAPIRequestBuilder(URL + "/" + id)
                    .debug() // Debug-Modus aktivieren
                    .build();

            // Create an HTTP GET request with the constructed URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and capture the response
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code indicates success
            if (response.statusCode() == SUCCESS) {
                this.eventManager.logInfoMessage("Successfully retrieved movie with ID: " + id);
                // Parse the response body into a Movie object
                return new ObjectMapper().readValue(response.body(), Movie.class);
            } else {
                // Log an error message for non-successful status codes
                logFailedResponseCode(response);
                return null;
            }
        } catch (Exception e) {
            // Log and return null if an exception occurs
            this.eventManager.logErrorMessage(e.getClass().getSimpleName());
            return null;
        }
    }

    /**
     * Parses the JSON response body into a list of {@link Movie} objects.
     * Uses the Jackson ObjectMapper to convert the JSON string into a list of {@link Movie} objects.
     * If an error occurs during parsing, an error message is logged, and null is returned.
     *
     * @param response The HTTP response containing the JSON body.
     * @return A list of {@link Movie} objects, or null if an error occurs during parsing.
     */
    private List<Movie> loadJSON(HttpResponse<String> response) {
        try {
            return new ObjectMapper().readValue(response.body(), new TypeReference<List<Movie>>() {});
        } catch (JsonProcessingException e) {
            this.eventManager.logErrorMessage("Error parsing JSON - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Logs the HTTP response code to the console with a corresponding message.
     * Depending on the status code, a specific message is printed to the console.
     *
     * @param response The HTTP response containing the status code.
     */
    private void logFailedResponseCode(HttpResponse<String> response) {
        switch (response.statusCode()){
            case 404:
                this.eventManager.logErrorMessage("Resource not found");
                break;
            case 500:
                this.eventManager.logErrorMessage("Internal server error");
                break;
            default:
                this.eventManager.logErrorMessage("Unknown error - " + response.statusCode());
        }
    }
}