package com.sportsvista.scheduler;

import com.sportsvista.api.*;
import com.sportsvista.dao.MatchDAO;
import com.sportsvista.dao.TeamDAO;
import com.sportsvista.model.Match;
import com.sportsvista.model.Team;
import com.sportsvista.model.SportRegistry;
import com.sportsvista.parser.*;
import java.util.List;

public class LiveSyncScheduler {

    private final CricketApiClient cricketClient = new CricketApiClient();
    private final FootballApiClient footballClient = new FootballApiClient();
    private final BaseballApiClient baseballClient = new BaseballApiClient();
    private final NFLApiClient nflClient = new NFLApiClient();
    private final NBAApiClient nbaClient = new NBAApiClient();

    private final CricketMatchParser cricketParser = new CricketMatchParser();
    private final FootballMatchParser footballParser = new FootballMatchParser();
    private final BaseballMatchParser baseballParser = new BaseballMatchParser();
    private final NFLMatchParser nflParser = new NFLMatchParser();
    private final NBAMatchParser nbaParser = new NBAMatchParser();

    private final MatchDAO matchDAO = new MatchDAO();
    private final TeamDAO teamDAO = new TeamDAO();

    public void syncAll() {
        System.out.println("Starting Global Match Sync...");
        syncCricket();
        syncFootball();
        syncBaseball();
        syncNFL();
        syncNBA();
        System.out.println("Global Match Sync Completed Successfully.");
    }

    private void syncCricket() {
        System.out.println("Syncing Cricket...");
        try {
            List<Match> matches = cricketParser.parseMatches(cricketClient.getCurrentMatches());
            System.out.println("Found " + matches.size() + " matches for Cricket.");
            upsertMatches(matches);
        } catch (Exception e) {
            System.err.println("Error syncing Cricket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void syncFootball() {
        System.out.println("Syncing Football...");
        try {
            List<Match> matches = footballParser.parseMatches(footballClient.getLiveMatches());
            System.out.println("Found " + matches.size() + " matches for Football.");
            upsertMatches(matches);
        } catch (Exception e) {
            System.err.println("Error syncing Football: " + e.getMessage());
        }
    }

    private void syncBaseball() {
        System.out.println("Syncing Baseball...");
        try {
            List<Match> matches = baseballParser.parseMatches(baseballClient.getTodaySchedule());
            System.out.println("Found " + matches.size() + " matches for Baseball.");
            upsertMatches(matches);
        } catch (Exception e) {
            System.err.println("Error syncing Baseball: " + e.getMessage());
        }
    }

    private void syncNFL() {
        System.out.println("Syncing NFL...");
        try {
            List<Match> matches = nflParser.parseMatches(nflClient.getScoreboard());
            System.out.println("Found " + matches.size() + " matches for NFL.");
            upsertMatches(matches);
        } catch (Exception e) {
            System.err.println("Error syncing NFL: " + e.getMessage());
        }
    }

    private void syncNBA() {
        System.out.println("Syncing NBA...");
        try {
            List<Match> matches = nbaParser.parseMatches(nbaClient.getTodayGames());
            System.out.println("Found " + matches.size() + " matches for NBA.");
            upsertMatches(matches);
        } catch (Exception e) {
            System.err.println("Error syncing NBA: " + e.getMessage());
        }
    }

    private void upsertMatches(List<Match> matches) throws Exception {
        for (Match m : matches) {
            // Ensure teams exist using stable external IDs
            ensureTeam(m.getTeam1ExternalId(), m.getTeam1Name(), m.getTeam1Logo(), m.getSportId());
            ensureTeam(m.getTeam2ExternalId(), m.getTeam2Name(), m.getTeam2Logo(), m.getSportId());
            
            // Re-fetch internal IDs for match record
            m.setTeam1Id(teamDAO.getIdByExternalId(m.getTeam1ExternalId()));
            m.setTeam2Id(teamDAO.getIdByExternalId(m.getTeam2ExternalId()));
            
            matchDAO.upsertMatch(m);
        }
    }

    private void ensureTeam(String externalId, String name, String logo, int sportId) throws Exception {
        if (externalId == null || externalId.isEmpty()) return;
        Team t = new Team();
        t.setExternalId(externalId);
        t.setTeamName(name);
        t.setLogoUrl(logo);
        t.setSportId(sportId);
        teamDAO.upsertTeam(t);
    }
}
