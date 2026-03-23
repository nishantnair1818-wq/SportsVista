package com.sportsvista.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sportsvista.model.Match;
import com.sportsvista.model.SportRegistry;
import com.sportsvista.util.SafeParser;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CricketMatchParser {

    private static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    static {
        ISO_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public List<Match> parseMatches(JsonObject response) {
        List<Match> matches = new ArrayList<>();
        JsonArray events = SafeParser.arr(response, "events");

        for (JsonElement el : events) {
            try {
                JsonObject event = el.getAsJsonObject();
                Match match = new Match();
                match.setExternalId(SafeParser.str(event, "id"));
                match.setSportId(SportRegistry.getId("cricket"));
                match.setStatusSummaryText(SafeParser.str(event, "name"));

                // League name (injected by our CricketApiClient)
                String leagueName = SafeParser.str(event, "leagueName");
                if (!leagueName.isEmpty()) {
                    match.setMatchDayLabel(leagueName);
                }

                // Date
                try {
                    String dateStr = SafeParser.str(event, "date");
                    if (!dateStr.isEmpty()) {
                        Date date = ISO_FORMAT.parse(dateStr);
                        match.setMatchDate(new Timestamp(date.getTime()));
                    }
                } catch (Exception e) {
                    match.setMatchDate(new Timestamp(System.currentTimeMillis()));
                }

                // Competition details
                JsonArray competitions = SafeParser.arr(event, "competitions");
                if (competitions.size() == 0) continue;
                JsonObject comp = competitions.get(0).getAsJsonObject();

                // Match type (T20, ODI, Test)
                JsonObject matchClass = SafeParser.obj(comp, "class");
                if (matchClass != null) {
                    match.setMatchType(SafeParser.str(matchClass, "generalClassCard"));
                }

                // Venue
                JsonObject venue = SafeParser.obj(comp, "venue");
                if (venue != null) {
                    match.setVenue(SafeParser.str(venue, "fullName"));
                    JsonObject address = SafeParser.obj(venue, "address");
                    if (address != null) {
                        match.setVenueCity(SafeParser.str(address, "city"));
                        match.setVenueCountry(SafeParser.str(address, "country"));
                    }
                }

                // Competitors (teams)
                JsonArray competitors = SafeParser.arr(comp, "competitors");
                if (competitors.size() >= 2) {
                    JsonObject c1 = competitors.get(0).getAsJsonObject();
                    JsonObject c2 = competitors.get(1).getAsJsonObject();

                    JsonObject team1 = SafeParser.obj(c1, "team");
                    JsonObject team2 = SafeParser.obj(c2, "team");

                    if (team1 != null) {
                        match.setTeam1ExternalId(SafeParser.str(team1, "id"));
                        match.setTeam1Name(SafeParser.str(team1, "displayName"));
                        match.setTeam1Logo(SafeParser.str(team1, "logo"));
                    }
                    if (team2 != null) {
                        match.setTeam2ExternalId(SafeParser.str(team2, "id"));
                        match.setTeam2Name(SafeParser.str(team2, "displayName"));
                        match.setTeam2Logo(SafeParser.str(team2, "logo"));
                    }

                    // Scores
                    String score1 = SafeParser.str(c1, "score");
                    String score2 = SafeParser.str(c2, "score");

                    // Build display score from linescores if available
                    JsonArray ls1 = SafeParser.arr(c1, "linescores");
                    JsonArray ls2 = SafeParser.arr(c2, "linescores");

                    if (ls1.size() > 0) {
                        match.setScoreDisplayTeam1(buildCricketScore(ls1));
                    } else if (!score1.isEmpty()) {
                        match.setScoreDisplayTeam1(score1);
                    }

                    if (ls2.size() > 0) {
                        match.setScoreDisplayTeam2(buildCricketScore(ls2));
                    } else if (!score2.isEmpty()) {
                        match.setScoreDisplayTeam2(score2);
                    }
                }

                // Status
                JsonObject status = SafeParser.obj(comp, "status");
                if (status == null) status = SafeParser.obj(event, "status");
                if (status != null) {
                    String summary = SafeParser.str(status, "summary");
                    if (!summary.isEmpty()) {
                        match.setStatusSummaryText(summary);
                    }

                    JsonObject type = SafeParser.obj(status, "type");
                    if (type != null) {
                        String state = SafeParser.str(type, "state");
                        switch (state) {
                            case "in":
                                match.setStatus("live");
                                break;
                            case "post":
                                match.setStatus("completed");
                                break;
                            case "pre":
                            default:
                                match.setStatus("upcoming");
                                break;
                        }
                    }
                }

                matches.add(match);
            } catch (Exception e) {
                System.err.println("CricketMatchParser: Error parsing event: " + e.getMessage());
            }
        }

        System.out.println("CricketMatchParser: Parsed " + matches.size() + " cricket matches from ESPN.");
        return matches;
    }

    private String buildCricketScore(JsonArray linescores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linescores.size(); i++) {
            JsonObject ls = linescores.get(i).getAsJsonObject();
            int runs = SafeParser.Int(ls, "value");
            sb.append(runs);
            if (i < linescores.size() - 1) sb.append(" & ");
        }
        return sb.toString();
    }
}
