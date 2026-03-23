package com.sportsvista.model;

public class MatchInfo {
    private int id;
    private int matchId;
    private String umpires;
    private String matchReferee;
    private String tvUmpire;
    private Integer attendance;
    private String weatherConditions;
    private String pitchConditions;
    private String tossWinner;
    private String tossDecision;
    private String seriesContext;
    private String extraInfoJson;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public String getUmpires() { return umpires; }
    public void setUmpires(String umpires) { this.umpires = umpires; }
    public String getMatchReferee() { return matchReferee; }
    public void setMatchReferee(String matchReferee) { this.matchReferee = matchReferee; }
    public String getTvUmpire() { return tvUmpire; }
    public void setTvUmpire(String tvUmpire) { this.tvUmpire = tvUmpire; }
    public Integer getAttendance() { return attendance; }
    public void setAttendance(Integer attendance) { this.attendance = attendance; }
    public String getWeatherConditions() { return weatherConditions; }
    public void setWeatherConditions(String weatherConditions) { this.weatherConditions = weatherConditions; }
    public String getPitchConditions() { return pitchConditions; }
    public void setPitchConditions(String pitchConditions) { this.pitchConditions = pitchConditions; }
    public String getTossWinner() { return tossWinner; }
    public void setTossWinner(String tossWinner) { this.tossWinner = tossWinner; }
    public String getTossDecision() { return tossDecision; }
    public void setTossDecision(String tossDecision) { this.tossDecision = tossDecision; }
    public String getSeriesContext() { return seriesContext; }
    public void setSeriesContext(String seriesContext) { this.seriesContext = seriesContext; }
    public String getExtraInfoJson() { return extraInfoJson; }
    public void setExtraInfoJson(String extraInfoJson) { this.extraInfoJson = extraInfoJson; }
}
