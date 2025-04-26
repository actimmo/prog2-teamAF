package com.teamAF.app.Exceptions;

/**
 * Custom exception class for handling database-related errors.
 * This exception is thrown when there are issues with the database,
 * such as connection errors or SQL exceptions.
 * */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
}
