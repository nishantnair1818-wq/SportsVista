package com.sportsvista.model;

public class MatchEvent {
    private int id;
    private int matchId;
    private String eventType;
    private String eventMinute;
    private String eventSecond;
    private String eventDetail;
    private int teamId;
    private String playerName;
    private int eventOrder;
    private String teamExternalId;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getEventMinute() { return eventMinute; }
    public void setEventMinute(String eventMinute) { this.eventMinute = eventMinute; }
    public String getEventSecond() { return eventSecond; }
    public void setEventSecond(String eventSecond) { this.eventSecond = eventSecond; }
    public String getEventDetail() { return eventDetail; }
    public void setEventDetail(String eventDetail) { this.eventDetail = eventDetail; }
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public int getEventOrder() { return eventOrder; }
    public void setEventOrder(int eventOrder) { this.eventOrder = eventOrder; }
    public String getTeamExternalId() { return teamExternalId; }
    public void setTeamExternalId(String teamExternalId) { this.teamExternalId = teamExternalId; }
}
