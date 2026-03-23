package com.sportsvista.model;

public class MatchLineup {
    private int id;
    private int matchId;
    private int teamId;
    private String playerName;
    private int shirtNumber;
    private String position;
    private boolean isStarter;
    private boolean isCaptain;
    private String teamExternalId;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public int getShirtNumber() { return shirtNumber; }
    public void setShirtNumber(int shirtNumber) { this.shirtNumber = shirtNumber; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public boolean isStarter() { return isStarter; }
    public void setStarter(boolean starter) { isStarter = starter; }
    public boolean isCaptain() { return isCaptain; }
    public void setCaptain(boolean captain) { isCaptain = captain; }
    public String getTeamExternalId() { return teamExternalId; }
    public void setTeamExternalId(String teamExternalId) { this.teamExternalId = teamExternalId; }
}
