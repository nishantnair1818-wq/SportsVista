package com.sportsvista.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sportsvista.model.MatchEvent;
import com.sportsvista.model.MatchLineup;
import com.sportsvista.util.SafeParser;
import java.util.ArrayList;
import java.util.List;

public class FootballDetailParser {

    public List<MatchEvent> parseEvents(JsonObject response) {
        List<MatchEvent> events = new ArrayList<>();
        JsonArray resArr = SafeParser.arr(response, "response");
        for (JsonElement el : resArr) {
            JsonObject item = el.getAsJsonObject();
            MatchEvent e = new MatchEvent();
            e.setEventType(SafeParser.str(SafeParser.obj(item, "type"), "name"));
            e.setEventMinute(String.valueOf(SafeParser.Int(SafeParser.obj(item, "time"), "elapsed")));
            e.setEventDetail(SafeParser.str(item, "detail"));
            e.setPlayerName(SafeParser.str(SafeParser.obj(item, "player"), "name"));
            // team_id needs DAO resolution
            events.add(e);
        }
        return events;
    }

    public List<MatchLineup> parseLineups(JsonObject response) {
        List<MatchLineup> lineups = new ArrayList<>();
        JsonArray resArr = SafeParser.arr(response, "response");
        for (JsonElement el : resArr) {
            JsonObject item = el.getAsJsonObject();
            // This is per team
            JsonArray startXI = SafeParser.arr(item, "startXI");
            for (JsonElement pEl : startXI) {
                lineups.add(parsePlayer(pEl.getAsJsonObject(), true));
            }
            JsonArray substitutes = SafeParser.arr(item, "substitutes");
            for (JsonElement pEl : substitutes) {
                lineups.add(parsePlayer(pEl.getAsJsonObject(), false));
            }
        }
        return lineups;
    }

    private MatchLineup parsePlayer(JsonObject pObj, boolean isStarter) {
        JsonObject p = SafeParser.obj(pObj, "player");
        MatchLineup l = new MatchLineup();
        l.setPlayerName(SafeParser.str(p, "name"));
        l.setShirtNumber(SafeParser.Int(p, "number"));
        l.setPosition(SafeParser.str(p, "pos"));
        l.setStarter(isStarter);
        return l;
    }
}
