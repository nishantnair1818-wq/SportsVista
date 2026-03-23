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

public class NBAMatchParser {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public List<Match> parseMatches(JsonObject response) {
        List<Match> matches = new ArrayList<>();
        JsonArray data = SafeParser.arr(response, "data");

        for (JsonElement dEl : data) {
            JsonObject g = dEl.getAsJsonObject();
            Match match = new Match();
            match.setExternalId(String.valueOf(SafeParser.Int(g, "id")));
            match.setSportId(SportRegistry.getId("basketball"));
            
            JsonObject home = SafeParser.obj(g, "home_team");
            JsonObject away = SafeParser.obj(g, "visitor_team");

            match.setTeam1Name(SafeParser.str(home, "full_name"));
            match.setTeam2Name(SafeParser.str(away, "full_name"));
            
            int hId = SafeParser.Int(home, "id");
            int aId = SafeParser.Int(away, "id");
            
            match.setTeam1ExternalId(String.valueOf(hId));
            match.setTeam2ExternalId(String.valueOf(aId));
            
            String hAbbr = SafeParser.str(home, "abbreviation").toLowerCase();
            String aAbbr = SafeParser.str(away, "abbreviation").toLowerCase();

            match.setTeam1Logo("https://a.espncdn.com/i/teamlogos/nba/500/" + hAbbr + ".png");
            match.setTeam2Logo("https://a.espncdn.com/i/teamlogos/nba/500/" + aAbbr + ".png");
            
            match.setScoreDisplayTeam1(String.valueOf(SafeParser.Int(g, "home_team_score")));
            match.setScoreDisplayTeam2(String.valueOf(SafeParser.Int(g, "visitor_team_score")));
            
            match.setStatusSummaryText(SafeParser.str(g, "status"));
            
            try {
                String dateStr = SafeParser.str(g, "date"); // "2024-06-01"
                Date date = DATE_FORMAT.parse(dateStr);
                match.setMatchDate(new Timestamp(date.getTime()));
            } catch (Exception ex) {
                match.setMatchDate(new Timestamp(System.currentTimeMillis()));
            }

            String status = SafeParser.str(g, "status").toLowerCase();
            if (status.contains("final")) {
                match.setStatus("completed");
            } else if (status.contains("q") || status.contains("half")) {
                match.setStatus("live");
            } else {
                match.setStatus("upcoming");
            }

            matches.add(match);
        }
        return matches;
    }
}
