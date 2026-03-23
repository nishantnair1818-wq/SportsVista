package com.sportsvista.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CricketApiClient {
    private static final String BASE_URL = "http://site.api.espn.com/apis/site/v2/sports/cricket/";
    private final HttpClient client = HttpClient.newHttpClient();

    // Multiple league IDs to get broad cricket coverage
    private static final String[] LEAGUE_IDS = {
        "8048",   // IPL
        "8039",   // ICC World Cup
        "8040",   // ICC T20 World Cup Qualifier
        "8676",   // ODI Tri-Series
        "8043",   // Sheffield Shield
        "8038"    // ICC Cricket World Cup Qualifier
    };

    public JsonObject getCurrentMatches() throws Exception {
        // Merge events from multiple cricket leagues into one response
        JsonArray allEvents = new JsonArray();
        
        for (String leagueId : LEAGUE_IDS) {
            try {
                JsonObject resp = fetch(BASE_URL + leagueId + "/scoreboard");
                JsonArray events = resp.has("events") ? resp.getAsJsonArray("events") : new JsonArray();
                for (JsonElement e : events) {
                    // Tag league name onto each event for display
                    JsonObject event = e.getAsJsonObject();
                    if (resp.has("leagues") && resp.getAsJsonArray("leagues").size() > 0) {
                        String leagueName = resp.getAsJsonArray("leagues").get(0).getAsJsonObject().get("name").getAsString();
                        event.addProperty("leagueName", leagueName);
                    }
                    allEvents.add(event);
                }
            } catch (Exception e) {
                System.err.println("CricketApiClient: Error fetching league " + leagueId + ": " + e.getMessage());
            }
        }

        // Build a combined response
        JsonObject combined = new JsonObject();
        combined.add("events", allEvents);
        return combined;
    }

    public JsonObject getMatchSummary(String matchId) throws Exception {
        // Try common leagues for summary
        for (String leagueId : LEAGUE_IDS) {
            try {
                return fetch(BASE_URL + leagueId + "/summary?event=" + matchId);
            } catch (Exception ignored) {}
        }
        throw new Exception("Could not fetch summary for match " + matchId);
    }

    private JsonObject fetch(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("HTTP " + response.statusCode() + " from " + url);
        }
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
}
