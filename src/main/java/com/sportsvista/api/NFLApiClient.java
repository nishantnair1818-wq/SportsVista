package com.sportsvista.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NFLApiClient {
    private static final String SCOREBOARD_URL = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard";
    private static final String SUMMARY_BASE_URL = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/summary?event=";
    private final HttpClient client = HttpClient.newHttpClient();

    public JsonObject getScoreboard() throws Exception {
        return fetch(SCOREBOARD_URL);
    }

    public JsonObject getMatchSummary(String eventId) throws Exception {
        return fetch(SUMMARY_BASE_URL + eventId);
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
