package com.teamAF.app.Data;

import com.j256.ormlite.dao.Dao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testklasse zur Überprüfung der Singleton-Implementierung
 * der Repository-Klassen.
 */
public class RepositorySingletonTest {

    private Dao<MovieEntity, Long> movieDaoMock;
    private Dao<WatchlistMovieEntity, Long> watchlistDaoMock;

    @BeforeEach
    void setUp() {
        // Mock-Objekte für die DAOs erstellen
        movieDaoMock = mock(Dao.class);
        watchlistDaoMock = mock(Dao.class);

        // Singleton-Instanzen zurücksetzen für isolierte Tests
        // Diese Methode müsste in den Repository-Klassen implementiert werden
        resetSingletons();
    }

    /**
     * Testet, ob MovieRepository als Singleton implementiert ist
     */
    @Test
    public void testMovieRepositorySingleton() {
        // Zwei Instanzen anfordern
        MovieRepository instance1 = MovieRepository.getInstance(movieDaoMock);
        MovieRepository instance2 = MovieRepository.getInstance(movieDaoMock);

        // Prüfen, ob es sich um dieselbe Instanz handelt
        assertSame(instance1, instance2, "MovieRepository sollte als Singleton implementiert sein");
    }

    /**
     * Testet, ob WatchlistRepository als Singleton implementiert ist
     */
    @Test
    public void testWatchlistRepositorySingleton() {
        // Zwei Instanzen anfordern
        WatchlistRepository instance1 = WatchlistRepository.getInstance(watchlistDaoMock);
        WatchlistRepository instance2 = WatchlistRepository.getInstance(watchlistDaoMock);

        // Prüfen, ob es sich um dieselbe Instanz handelt
        assertSame(instance1, instance2, "WatchlistRepository sollte als Singleton implementiert sein");
    }

    /**
     * Testet, ob auch bei unterschiedlichen DAO-Objekten dieselbe Singleton-Instanz zurückgegeben wird
     */
    @Test
    public void testMovieRepositorySingletonWithDifferentDaos() {
        // Erstelle erste Instanz
        MovieRepository instance1 = MovieRepository.getInstance(movieDaoMock);

        // Erstelle einen anderen Mock
        Dao<MovieEntity, Long> anotherMock = mock(Dao.class);

        // Fordere Instanz mit anderem Mock an
        MovieRepository instance2 = MovieRepository.getInstance(anotherMock);

        // Es sollte trotzdem dieselbe Instanz sein
        assertSame(instance1, instance2, "MovieRepository sollte dieselbe Instanz zurückgeben, unabhängig vom DAO");
    }

    /**
     * Testet, ob auch bei unterschiedlichen DAO-Objekten dieselbe Singleton-Instanz zurückgegeben wird
     */
    @Test
    public void testWatchlistRepositorySingletonWithDifferentDaos() {
        // Erstelle erste Instanz
        WatchlistRepository instance1 = WatchlistRepository.getInstance(watchlistDaoMock);

        // Erstelle einen anderen Mock
        Dao<WatchlistMovieEntity, Long> anotherMock = mock(Dao.class);

        // Fordere Instanz mit anderem Mock an
        WatchlistRepository instance2 = WatchlistRepository.getInstance(anotherMock);

        // Es sollte trotzdem dieselbe Instanz sein
        assertSame(instance1, instance2, "WatchlistRepository sollte dieselbe Instanz zurückgeben, unabhängig vom DAO");
    }

    /**
     * Hilfsmethode zum Zurücksetzen der Singleton-Instanzen für isolierte Tests
     * Diese Methode müsste in den Repository-Klassen implementiert werden
     */
    private void resetSingletons() {
        try {
            java.lang.reflect.Field instanceField = MovieRepository.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);

            instanceField = WatchlistRepository.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}