package com.sportsvista.model;

import java.math.BigDecimal;

public class BowlingScorecard {
    private int id;
    private int inningsId;
    private String playerName;
    private String overs;
    private int maidens;
    private int runsGiven;
    private int wickets;
    private BigDecimal economy;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getInningsId() { return inningsId; }
    public void setInningsId(int inningsId) { this.inningsId = inningsId; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public String getOvers() { return overs; }
    public void setOvers(String overs) { this.overs = overs; }
    public int getMaidens() { return maidens; }
    public void setMaidens(int maidens) { this.maidens = maidens; }
    public int getRunsGiven() { return runsGiven; }
    public void setRunsGiven(int runsGiven) { this.runsGiven = runsGiven; }
    public int getWickets() { return wickets; }
    public void setWickets(int wickets) { this.wickets = wickets; }
    public BigDecimal getEconomy() { return economy; }
    public void setEconomy(BigDecimal economy) { this.economy = economy; }
}
