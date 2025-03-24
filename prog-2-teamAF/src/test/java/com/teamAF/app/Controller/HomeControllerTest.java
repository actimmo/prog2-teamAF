package com.teamAF.app.Controller;
import com.teamAF.app.Model.Movie;
import org.junit.jupiter.api.*;
import java.util.*;

public class HomeControllerTest {

    private HomeController controller;
    private List<Movie> movies;

    @BeforeEach
    void setUp() {
        controller = new HomeController();
        movies = new ArrayList<>();

        // String id, String title, String description, List<String> genres, int releaseYear, String imgUrl,
        //                 int lengthInMinutes, List<String> directors, List<String> writers, List<String> mainCast,
        //                 double rating)

        movies = Arrays.asList(
                new Movie("86642997-ee66-4102-ade1-54941a1d3a6e",
                        "Inception",
                        "A thief, who steals corporate secrets through use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a CEO.",
                        Arrays.asList("ACTION", "SCIENCE_FICTION"),
                        2010,
                        "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_FMjpg_UX1000_.jpg",
                        148,
                        List.of("Christopher Nolan"),
                        List.of("Christopher Nolan"),
                        Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"),
                        8.8),

                new Movie("e2d9913d-3869-454c-9fbf-a63aaf57bedf",
                        "The Matrix",
                        "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                        Arrays.asList("ACTION", "SCIENCE_FICTION"),
                        1999,
                        "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                        136,
                        List.of("Lana Wachowski", "Lilly Wachowski"),
                        List.of("Lana Wachowski", "Lilly Wachowski"),
                        Arrays.asList("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
                        8.7),

                new Movie("d95d6912-b281-4e08-86b8-f9101f5f2c15",
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList("BIOGRAPHY", "COMEDY", "CRIME", "DRAMA"),
                        2013,
                        "https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_FMjpg_UX1000_.jpg",
                        180,
                        List.of("Martin Scorsese "),
                        List.of("Martin Scorsese"),
                        Arrays.asList("Leonardo DiCaprio", "Jonah Hill", "Margot Robbie"),
                        8.2)
        );

    }

    @Test
    void getMostPopularActor() {
        String mostPopularActor = controller.getMostPopularActor(movies);
    }

}
