package com.sportsvista.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FootballApiClient {
    private static final String BASE_URL = "http://site.api.espn.com/apis/site/v2/sports/soccer/all/scoreboard";
    private final HttpClient client = HttpClient.newHttpClient();

    public JsonObject getLiveMatches() throws Exception {
        return fetch(BASE_URL);
    }

    public JsonObject getMatchSummary(String matchId) throws Exception {
        return fetch("http://site.api.espn.com/apis/site/v2/sports/soccer/all/summary?event=" + matchId);
    }

    private JsonObject fetch(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
}
