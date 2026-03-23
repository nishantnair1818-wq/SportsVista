package com.sportsvista.scheduler;

import com.google.gson.JsonObject;
import com.sportsvista.api.*;
import com.sportsvista.dao.*;
import com.sportsvista.model.*;
import com.sportsvista.parser.EspnMatchDetailParser;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ScorecardSyncScheduler {

    private final CricketApiClient cricketClient = new CricketApiClient();
    private final FootballApiClient footballClient = new FootballApiClient();
    private final NFLApiClient nflClient = new NFLApiClient();
    private final BaseballApiClient baseballClient = new BaseballApiClient();
    
    // Cricket now uses ESPN format like other sports
    private final EspnMatchDetailParser espnParser = new EspnMatchDetailParser();
    
    private final MatchDAO matchDAO = new MatchDAO();
    private final TeamDAO teamDAO = new TeamDAO();
    private final MatchEventDAO eventDAO = new MatchEventDAO();
    private final MatchLineupDAO lineupDAO = new MatchLineupDAO();
    private final Set<Integer> syncedCompletedMatches = new HashSet<>();

    public void syncAllDetails() {
        System.out.println(">>> Periodic Sync Started <<<");
        syncEspnDetails();
        System.out.println(">>> Periodic Sync Completed <<<");
    }



    public void syncEspnDetails() {
        String[] sports = {"cricket", "football", "american_football", "baseball"};
        for (String sportName : sports) {
            try {
                Integer sportId = SportRegistry.getId(sportName);
                if (sportId == null) continue;

                List<Match> matches = matchDAO.getMatchesByStatus(sportId, "live");
                List<Match> completed = matchDAO.getMatchesByStatus(sportId, "completed");
                
                for (Match c : completed) {
                    if (!syncedCompletedMatches.contains(c.getId())) {
                        matches.add(c);
                    }
                }
                
                for (Match m : matches) {
                    syncSingleEspnDetail(m, sportName);
                    if ("completed".equals(m.getStatus())) {
                        syncedCompletedMatches.add(m.getId());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error syncing details for " + sportName + ": " + e.getMessage());
            }
        }
    }

    private void syncSingleEspnDetail(Match m, String sportName) {
        try {
            // Reduced logging
            JsonObject summary = null;
            if ("cricket".equals(sportName)) summary = cricketClient.getMatchSummary(m.getExternalId());
            else if ("football".equals(sportName)) summary = footballClient.getMatchSummary(m.getExternalId());
            else if ("american_football".equals(sportName)) summary = nflClient.getMatchSummary(m.getExternalId());
            else if ("baseball".equals(sportName)) summary = baseballClient.getMatchSummary(m.getExternalId());

            if (summary != null) {
                // Events
                List<MatchEvent> events = espnParser.parseEvents(summary, m.getId());
                eventDAO.clearMatchEvents(m.getId());
                int order = 1;
                for (MatchEvent e : events) {
                    e.setEventOrder(order++);
                    if (e.getTeamExternalId() != null) {
                        int tid = teamDAO.getIdByExternalId(e.getTeamExternalId());
                        if (tid > 0) e.setTeamId(tid);
                    }
                    eventDAO.insert(e);
                }

                // Lineups
                List<MatchLineup> lineups = espnParser.parseLineups(summary, m.getId());
                lineupDAO.clearMatchLineups(m.getId());
                for (MatchLineup l : lineups) {
                    if (l.getTeamExternalId() != null) {
                        int tid = teamDAO.getIdByExternalId(l.getTeamExternalId());
                        if (tid > 0) l.setTeamId(tid);
                    }
                    lineupDAO.insert(l);
                }
            }
        } catch (Exception e) {
            System.err.println("Error syncing ESPN detail for match " + m.getExternalId() + ": " + e.getMessage());
        }
    }
}
