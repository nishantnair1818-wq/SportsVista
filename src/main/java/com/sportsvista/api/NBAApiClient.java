package com.sportsvista.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import com.sportsvista.util.ConfigLoader;

public class NBAApiClient {
    private static final String API_KEY = ConfigLoader.get("nba.balldontlie.key"); 
    private static final String BASE_URL = "https://api.balldontlie.io/v1/";
    private final HttpClient client = HttpClient.newHttpClient();

    public JsonObject getTodayGames() throws Exception {
        String date = LocalDate.now().toString();
        return fetch(BASE_URL + "games?dates[]=" + date);
    }

    private JsonObject fetch(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", API_KEY)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
}
