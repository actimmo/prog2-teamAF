package com.teamAF.app.Exceptions;

/**
 * Custom exception class for handling movie API-related errors.
 * This exception is thrown when there are issues with the movie API,
 * such as network errors or invalid responses.
 */
public class MovieApiException extends Exception {
    public MovieApiException(String message) {
        super(message);
    }
}
