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

public class FootballMatchParser {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

    public List<Match> parseMatches(JsonObject response) {
        List<Match> matches = new ArrayList<>();
        JsonArray events = SafeParser.arr(response, "events");

        for (JsonElement eEl : events) {
            JsonObject e = eEl.getAsJsonObject();
            Match match = new Match();
            match.setExternalId(SafeParser.str(e, "id"));
            match.setSportId(SportRegistry.getId("football"));
            
            JsonArray competitions = SafeParser.arr(e, "competitions");
            if (competitions.size() == 0) continue;
            JsonObject competition = competitions.get(0).getAsJsonObject();
            JsonArray competitors = SafeParser.arr(competition, "competitors");
            
            JsonObject home = competitors.get(0).getAsJsonObject();
            JsonObject away = competitors.get(1).getAsJsonObject();

            JsonObject hTeam = SafeParser.obj(home, "team");
            JsonObject aTeam = SafeParser.obj(away, "team");
            
            match.setTeam1Name(SafeParser.str(hTeam, "displayName"));
            match.setTeam2Name(SafeParser.str(aTeam, "displayName"));
            match.setTeam1ExternalId(SafeParser.str(hTeam, "id"));
            match.setTeam2ExternalId(SafeParser.str(aTeam, "id"));

            String logo1 = SafeParser.str(hTeam, "logo");
            JsonArray hLogos = SafeParser.arr(hTeam, "logos");
            if (hLogos.size() > 0) logo1 = SafeParser.str(hLogos.get(0).getAsJsonObject(), "href");
            if (logo1 == null || logo1.isEmpty()) logo1 = "https://a.espncdn.com/i/teamlogos/soccer/500/" + match.getTeam1ExternalId() + ".png";
            match.setTeam1Logo(logo1);

            String logo2 = SafeParser.str(aTeam, "logo");
            JsonArray aLogos = SafeParser.arr(aTeam, "logos");
            if (aLogos.size() > 0) logo2 = SafeParser.str(aLogos.get(0).getAsJsonObject(), "href");
            if (logo2 == null || logo2.isEmpty()) logo2 = "https://a.espncdn.com/i/teamlogos/soccer/500/" + match.getTeam2ExternalId() + ".png";
            match.setTeam2Logo(logo2);

            match.setScoreDisplayTeam1(SafeParser.str(home, "score"));
            match.setScoreDisplayTeam2(SafeParser.str(away, "score"));

            JsonObject status = SafeParser.obj(e, "status");
            match.setStatusSummaryText(SafeParser.str(SafeParser.obj(status, "type"), "detail"));
            
            // Try to find competition name
            match.setCompetitionName("Football Match"); // Default
            
            try {
                String dateStr = SafeParser.str(e, "date");
                Date date = DATE_FORMAT.parse(dateStr);
                match.setMatchDate(new Timestamp(date.getTime()));
            } catch (Exception ex) {
                match.setMatchDate(new Timestamp(System.currentTimeMillis()));
            }

            String state = SafeParser.str(SafeParser.obj(status, "type"), "state");
            if ("in".equals(state)) {
                match.setStatus("live");
            } else if ("post".equals(state)) {
                match.setStatus("completed");
            } else {
                match.setStatus("upcoming");
            }

            matches.add(match);
        }
        return matches;
    }
}
