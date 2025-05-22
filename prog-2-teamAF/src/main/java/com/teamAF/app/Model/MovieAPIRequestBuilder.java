package com.teamAF.app.Model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MovieAPIRequestBuilder {
    private static final Logger LOGGER = Logger.getLogger(MovieAPIRequestBuilder.class.getName());
    private final String baseUrl;
    private final Map<String, String> queryParams;
    private boolean debugMode = false;

    public MovieAPIRequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        this.queryParams = new HashMap<>();
    }


    /**
     * Aktiviert den Debug-Modus, der die erstellte URL in der Konsole ausgibt.
     *
     * @return Die aktuelle Builder-Instanz für Methoden-Verkettung
     */
    public MovieAPIRequestBuilder debug() {
        this.debugMode = true;
        return this;
    }

    public MovieAPIRequestBuilder query(String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            queryParams.put("query", searchQuery);
        }
        return this;
    }

    public MovieAPIRequestBuilder genre(String genre) {
        if (genre != null && !genre.isEmpty()) {
            queryParams.put("genre", genre);
        }
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(String year) {
        if (year != null && !year.isEmpty()) {
            queryParams.put("releaseYear", year);
        }
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(String rating) {
        if (rating != null && !rating.isEmpty()) {
            queryParams.put("ratingFrom", rating);
        }
        return this;
    }

    public String build() {
        String url;
        if (queryParams.isEmpty()) {
            url = baseUrl;
        } else {
            String queryString = queryParams.entrySet().stream()
                    .map(entry -> {
                        try {
                            return URLEncoder.encode(entry.getKey(), "UTF-8") + "=" +
                                    URLEncoder.encode(entry.getValue(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            return "";
                        }
                    })
                    .collect(Collectors.joining("&"));

            url = baseUrl + "?" + queryString;
        }

        if (debugMode) {
            //System.out.println("DEBUG - Generated URL: " + url);
            LOGGER.info("Generated URL: " + url);
        }

        return url;
    }
}