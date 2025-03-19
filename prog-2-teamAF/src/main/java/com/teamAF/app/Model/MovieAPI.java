package com.teamAF.app.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.eventmanager.EventManager;

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

    /**
     * Retrieves a list of movies from the specified URL.
     * Sends an HTTP GET request to the URL and parses the response as a list of {@link Movie} objects.
     * If the request is successful (status code 200), the response body is parsed into a list of {@link Movie} objects.
     * If the request fails, an appropriate error message is logged, and null is returned.
     *
     * @return A list of {@link Movie} objects, or null if an error occurs during the request or parsing.
     */
    public List<Movie> getMovies() {
        HttpResponse<String> response;
        HttpClient client = this.httpClient;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .GET()
                    .build();
            // Send request and get response
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                this.eventManager.logInfoMessage("Successfully retrieved movies.");
                return loadJSON(response);
            } else {
                logFailedResponseCode(response);
                return null;
            }
        } catch (ConnectException e){
            this.eventManager.logErrorMessage("Connection failed");
            return null;
        } catch (IOException e) {
            this.eventManager.logErrorMessage("IO Exception");
            return null;
        } catch (InterruptedException e) {
            this.eventManager.logErrorMessage("Interrupted Exception");
            return null;
        } catch (IllegalArgumentException e){
            this.eventManager.logErrorMessage("Invalid URL");
            return null;
        }
    }


    /**
     * Retrieves a list of movies from the specified URL.
     * Sends an HTTP GET request to the URL and parses the response as a list of {@link Movie} objects.
     * If the request is successful (status code 200), the response body is parsed into a list of {@link Movie} objects.
     * If the request fails, an appropriate error message is logged, and null is returned.
     *
     * @param queryParams a Map which could contain all the query params
     *
     * @return A list of {@link Movie} objects, or null if an error occurs during the request or parsing.
     */
    public List<Movie> getMoviesWithParams(Map<String, String> queryParams) {
        HttpResponse<String> response;
        HttpClient client = this.httpClient;

        try {
            String queryString = queryParams.entrySet().stream()
                    .map(entry -> {
                        try {
                            return URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            this.eventManager.logErrorMessage("UnsupportedEncodingException");
                            return "";
                        }
                    })
                    .collect(Collectors.joining("&"));

            String urlWithParams = URL + "?" + queryString;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithParams))
                    .GET()
                    .build();

            // Send request and get response
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                this.eventManager.logInfoMessage("Successfully retrieved movies with parameters.");
                return loadJSON(response);
            } else {
                logFailedResponseCode(response);
                return null;
            }
        }  catch (ConnectException e) {
            this.eventManager.logErrorMessage("Connection failed");
            return null;
        } catch (IOException e) {
            this.eventManager.logErrorMessage("IO Exception");
            return null;
        } catch (InterruptedException e) {
            this.eventManager.logErrorMessage("Interrupted Exception");
            return null;
        } catch (IllegalArgumentException e) {
            this.eventManager.logErrorMessage("Invalid URL");
            return null;
        }
    }

    /**
     * Retrieves a movie by its ID from the specified URL.
     * Sends an HTTP GET request to the URL with the movie ID and parses the response as a {@link Movie} object.
     * If the request is successful (status code 200), the response body is parsed into a {@link Movie} object.
     * If the request fails, an appropriate error message is logged, and null is returned.
     *
     * @param id The ID of the movie to retrieve.
     * @return A {@link Movie} object, or null if an error occurs during the request or parsing.
     */
    public Movie getMovieByID(String id) {
        HttpResponse<String> response;
        HttpClient client = this.httpClient;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/" + id))
                    .GET()
                    .build();
            // Send request and get response
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                this.eventManager.logInfoMessage("Successfully retrieved movie with ID: " + id);
                return new ObjectMapper().readValue(response.body(), Movie.class);
            } else {
                logFailedResponseCode(response);
                return null;
            }
        } catch (ConnectException e){
            this.eventManager.logErrorMessage("Connection failed");
            return null;
        } catch (IOException e) {
            this.eventManager.logErrorMessage("IO Exception");
            return null;
        } catch (InterruptedException e) {
            this.eventManager.logErrorMessage("Interrupted Exception");
            return null;
        } catch (IllegalArgumentException e){
            this.eventManager.logErrorMessage("Invalid URL");
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
