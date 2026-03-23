package com.sportsvista.model;

import java.sql.Timestamp;

public class MatchStats {
    private int id;
    private int matchId;
    private String statsJson;
    private Timestamp lastUpdated;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public String getStatsJson() { return statsJson; }
    public void setStatsJson(String statsJson) { this.statsJson = statsJson; }
    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
}
