package com.sportsvista.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sportsvista.model.MatchEvent;
import com.sportsvista.model.MatchLineup;
import com.sportsvista.util.SafeParser;
import java.util.ArrayList;
import java.util.List;

public class EspnMatchDetailParser {

    public List<MatchEvent> parseEvents(JsonObject response, int matchId) {
        List<MatchEvent> events = new ArrayList<>();

        // Strategy 1: Top-level "keyEvents" (Soccer, Baseball)
        JsonArray plays = SafeParser.arr(response, "keyEvents");

        // Strategy 2: Top-level "plays" (some Baseball formats)
        if (plays.size() == 0) {
            plays = SafeParser.arr(response, "plays");
        }

        if (plays.size() > 0) {
            for (JsonElement pEl : plays) {
                JsonObject p = pEl.getAsJsonObject();
                MatchEvent event = parsePlayObject(p, matchId);
                if (event != null) events.add(event);
                if (events.size() > 50) break;
            }
        }

        // Strategy 3: NFL-style "scoringPlays" array (flat list of scoring play refs)
        if (events.isEmpty()) {
            JsonArray scoringPlays = SafeParser.arr(response, "scoringPlays");
            for (JsonElement spEl : scoringPlays) {
                JsonObject sp = spEl.getAsJsonObject();
                MatchEvent event = parsePlayObject(sp, matchId);
                if (event != null) events.add(event);
                if (events.size() > 50) break;
            }
        }

        // Strategy 4: NFL-style drives -> previous[] -> plays[] (extract scoring plays only)
        if (events.isEmpty()) {
            JsonObject drives = SafeParser.obj(response, "drives");
            if (drives != null) {
                JsonArray previousDrives = SafeParser.arr(drives, "previous");
                for (JsonElement dEl : previousDrives) {
                    JsonObject drive = dEl.getAsJsonObject();
                    boolean isScore = drive.has("isScore") && drive.get("isScore").getAsBoolean();
                    String description = SafeParser.str(drive, "description");
                    String displayResult = SafeParser.str(drive, "displayResult");

                    // Get team info from drive
                    JsonObject driveTeam = SafeParser.obj(drive, "team");
                    String teamId = driveTeam != null ? SafeParser.str(driveTeam, "id") : "";
                    String teamAbbr = driveTeam != null ? SafeParser.str(driveTeam, "abbreviation") : "";

                    if (isScore) {
                        // Find the scoring play within this drive
                        JsonArray drivePlays = SafeParser.arr(drive, "plays");
                        for (JsonElement dpEl : drivePlays) {
                            JsonObject dp = dpEl.getAsJsonObject();
                            boolean scoring = dp.has("scoringPlay") && dp.get("scoringPlay").getAsBoolean();
                            if (scoring) {
                                MatchEvent event = new MatchEvent();
                                event.setMatchId(matchId);

                                // Clock from the play
                                JsonObject clock = SafeParser.obj(dp, "clock");
                                String clockVal = clock != null ? SafeParser.str(clock, "displayValue") : "";
                                // Period
                                JsonObject period = SafeParser.obj(dp, "period");
                                String periodNum = period != null ? String.valueOf(period.get("number").getAsInt()) : "";
                                event.setEventMinute("Q" + periodNum + " " + clockVal);

                                // Type
                                JsonObject type = SafeParser.obj(dp, "type");
                                event.setEventType(type != null ? SafeParser.str(type, "text") : displayResult);
                                event.setEventDetail(SafeParser.str(dp, "text"));
                                event.setTeamExternalId(teamId);
                                event.setPlayerName(teamAbbr);

                                events.add(event);
                                if (events.size() > 50) break;
                            }
                        }
                    } else if (!description.isEmpty()) {
                        // Also add drive summaries for non-scoring drives if we need content
                        // Skip for now to keep summary concise
                    }
                    if (events.size() > 50) break;
                }
            }
        }

        System.out.println("EspnParser: parsed " + events.size() + " events for match " + matchId);
        return events;
    }

    private MatchEvent parsePlayObject(JsonObject p, int matchId) {
        MatchEvent event = new MatchEvent();
        event.setMatchId(matchId);

        // Extract minute/clock
        String clock = SafeParser.str(SafeParser.obj(p, "clock"), "displayValue");
        if (clock.isEmpty()) clock = SafeParser.str(p, "clock");
        if (clock.contains("'")) clock = clock.replace("'", "");
        event.setEventMinute(clock);

        event.setEventType(SafeParser.str(SafeParser.obj(p, "type"), "text"));
        event.setEventDetail(SafeParser.str(p, "text"));
        event.setTeamExternalId(SafeParser.str(SafeParser.obj(p, "team"), "id"));

        // Player info
        JsonArray participants = SafeParser.arr(p, "participants");
        if (participants.size() > 0) {
            event.setPlayerName(SafeParser.str(participants.get(0).getAsJsonObject(), "athleteName"));
        } else {
            JsonObject athlete = SafeParser.obj(p, "athlete");
            if (athlete != null) event.setPlayerName(SafeParser.str(athlete, "displayName"));
        }

        return event;
    }

    public List<MatchLineup> parseLineups(JsonObject response, int matchId) {
        List<MatchLineup> lineups = new ArrayList<>();

        // Strategy 1: "rosters" array (Soccer, Baseball)
        JsonArray rosters = SafeParser.arr(response, "rosters");
        if (rosters.size() > 0) {
            for (JsonElement rEl : rosters) {
                JsonObject r = rEl.getAsJsonObject();
                String teamExtId = SafeParser.str(SafeParser.obj(r, "team"), "id");
                JsonArray participants = SafeParser.arr(r, "roster");
                for (JsonElement pEl : participants) {
                    JsonObject p = pEl.getAsJsonObject();
                    JsonObject athlete = SafeParser.obj(p, "athlete");

                    MatchLineup lineup = new MatchLineup();
                    lineup.setMatchId(matchId);
                    lineup.setTeamExternalId(teamExtId);
                    lineup.setPlayerName(SafeParser.str(athlete, "displayName"));
                    lineup.setPosition(SafeParser.str(SafeParser.obj(athlete, "position"), "abbreviation"));
                    lineup.setStarter(SafeParser.bool(p, "starter"));

                    lineups.add(lineup);
                    if (lineups.size() > 100) break;
                }
                if (lineups.size() > 100) break;
            }
        }

        // Strategy 2: NFL-style "boxscore.players[].statistics[].athletes[]"
        if (lineups.isEmpty()) {
            JsonObject boxscore = SafeParser.obj(response, "boxscore");
            if (boxscore != null) {
                JsonArray players = SafeParser.arr(boxscore, "players");
                for (JsonElement plEl : players) {
                    JsonObject teamBlock = plEl.getAsJsonObject();
                    JsonObject team = SafeParser.obj(teamBlock, "team");
                    String teamExtId = team != null ? SafeParser.str(team, "id") : "";

                    // Collect unique athlete names from all stat categories
                    JsonArray statistics = SafeParser.arr(teamBlock, "statistics");
                    java.util.Set<String> addedAthletes = new java.util.HashSet<>();
                    for (JsonElement statEl : statistics) {
                        JsonObject stat = statEl.getAsJsonObject();
                        JsonArray athletes = SafeParser.arr(stat, "athletes");
                        for (JsonElement aEl : athletes) {
                            JsonObject athleteEntry = aEl.getAsJsonObject();
                            JsonObject athlete = SafeParser.obj(athleteEntry, "athlete");
                            if (athlete == null) continue;
                            String name = SafeParser.str(athlete, "displayName");
                            if (name.isEmpty() || addedAthletes.contains(name)) continue;
                            addedAthletes.add(name);

                            MatchLineup lineup = new MatchLineup();
                            lineup.setMatchId(matchId);
                            lineup.setTeamExternalId(teamExtId);
                            lineup.setPlayerName(name);
                            lineup.setPosition(SafeParser.str(athlete, "jersey"));
                            lineup.setStarter(true);
                            lineups.add(lineup);
                            if (lineups.size() > 100) break;
                        }
                        if (lineups.size() > 100) break;
                    }
                    if (lineups.size() > 100) break;
                }
            }
        }

        System.out.println("EspnParser: parsed " + lineups.size() + " lineup players for match " + matchId);
        return lineups;
    }
}
