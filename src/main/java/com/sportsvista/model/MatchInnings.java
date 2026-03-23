package com.sportsvista.model;

public class MatchInnings {
    private int id;
    private int matchId;
    private int inningsNumber;
    private int battingTeamId;
    private int totalRuns;
    private int totalWickets;
    private String totalOvers;
    private int extras;
    private String inningsStatus;

    // Helper for display
    private String battingTeamName;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public int getInningsNumber() { return inningsNumber; }
    public void setInningsNumber(int inningsNumber) { this.inningsNumber = inningsNumber; }
    public int getBattingTeamId() { return battingTeamId; }
    public void setBattingTeamId(int battingTeamId) { this.battingTeamId = battingTeamId; }
    public int getTotalRuns() { return totalRuns; }
    public void setTotalRuns(int totalRuns) { this.totalRuns = totalRuns; }
    public int getTotalWickets() { return totalWickets; }
    public void setTotalWickets(int totalWickets) { this.totalWickets = totalWickets; }
    public String getTotalOvers() { return totalOvers; }
    public void setTotalOvers(String totalOvers) { this.totalOvers = totalOvers; }
    public int getExtras() { return extras; }
    public void setExtras(int extras) { this.extras = extras; }
    public String getInningsStatus() { return inningsStatus; }
    public void setInningsStatus(String inningsStatus) { this.inningsStatus = inningsStatus; }
    public String getBattingTeamName() { return battingTeamName; }
    public void setBattingTeamName(String battingTeamName) { this.battingTeamName = battingTeamName; }
}
