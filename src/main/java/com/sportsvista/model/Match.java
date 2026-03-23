package com.sportsvista.model;

import java.sql.Timestamp;

public class Match {
    private int id;
    private String externalId;
    private int sportId;
    private int competitionId;
    private int team1Id;
    private int team2Id;
    private String matchType;
    private String matchSubType;
    private String status;
    private String scoreDisplayTeam1;
    private String scoreDisplayTeam2;
    private String statusSummaryText;
    private Timestamp matchDate;
    private String venue;
    private String venueCity;
    private String venueCountry;
    private String tossText;
    private String resultText;
    private String matchDayLabel;
    private Timestamp lastUpdated;

    // Helper fields for display (not in DB columns but useful for JSON)
    private String team1Name;
    private String team2Name;
    private String team1Logo;
    private String team2Logo;
    private String team1ExternalId;
    private String team2ExternalId;
    private String competitionName;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public int getSportId() { return sportId; }
    public void setSportId(int sportId) { this.sportId = sportId; }
    public int getCompetitionId() { return competitionId; }
    public void setCompetitionId(int competitionId) { this.competitionId = competitionId; }
    public int getTeam1Id() { return team1Id; }
    public void setTeam1Id(int team1Id) { this.team1Id = team1Id; }
    public int getTeam2Id() { return team2Id; }
    public void setTeam2Id(int team2Id) { this.team2Id = team2Id; }
    public String getMatchType() { return matchType; }
    public void setMatchType(String matchType) { this.matchType = matchType; }
    public String getMatchSubType() { return matchSubType; }
    public void setMatchSubType(String matchSubType) { this.matchSubType = matchSubType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getScoreDisplayTeam1() { return scoreDisplayTeam1; }
    public void setScoreDisplayTeam1(String scoreDisplayTeam1) { this.scoreDisplayTeam1 = scoreDisplayTeam1; }
    public String getScoreDisplayTeam2() { return scoreDisplayTeam2; }
    public void setScoreDisplayTeam2(String scoreDisplayTeam2) { this.scoreDisplayTeam2 = scoreDisplayTeam2; }
    public String getStatusSummaryText() { return statusSummaryText; }
    public void setStatusSummaryText(String statusSummaryText) { this.statusSummaryText = statusSummaryText; }
    public Timestamp getMatchDate() { return matchDate; }
    public void setMatchDate(Timestamp matchDate) { this.matchDate = matchDate; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getVenueCity() { return venueCity; }
    public void setVenueCity(String venueCity) { this.venueCity = venueCity; }
    public String getVenueCountry() { return venueCountry; }
    public void setVenueCountry(String venueCountry) { this.venueCountry = venueCountry; }
    public String getTossText() { return tossText; }
    public void setTossText(String tossText) { this.tossText = tossText; }
    public String getResultText() { return resultText; }
    public void setResultText(String resultText) { this.resultText = resultText; }
    public String getMatchDayLabel() { return matchDayLabel; }
    public void setMatchDayLabel(String matchDayLabel) { this.matchDayLabel = matchDayLabel; }
    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getTeam1Name() { return team1Name; }
    public void setTeam1Name(String team1Name) { this.team1Name = team1Name; }
    public String getTeam2Name() { return team2Name; }
    public void setTeam2Name(String team2Name) { this.team2Name = team2Name; }
    public String getTeam1Logo() { return team1Logo; }
    public void setTeam1Logo(String team1Logo) { this.team1Logo = team1Logo; }
    public String getTeam2Logo() { return team2Logo; }
    public void setTeam2Logo(String team2Logo) { this.team2Logo = team2Logo; }
    public String getTeam1ExternalId() { return team1ExternalId; }
    public void setTeam1ExternalId(String team1ExternalId) { this.team1ExternalId = team1ExternalId; }
    public String getTeam2ExternalId() { return team2ExternalId; }
    public void setTeam2ExternalId(String team2ExternalId) { this.team2ExternalId = team2ExternalId; }
    public String getCompetitionName() { return competitionName; }
    public void setCompetitionName(String competitionName) { this.competitionName = competitionName; }
}
