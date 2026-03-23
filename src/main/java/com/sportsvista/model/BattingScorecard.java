package com.sportsvista.model;

import java.math.BigDecimal;

public class BattingScorecard {
    private int id;
    private int inningsId;
    private String playerName;
    private int runs;
    private int ballsFaced;
    private int fours;
    private int sixes;
    private BigDecimal strikeRate;
    private String dismissalText;
    private boolean isBatting;
    private int battingOrder;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getInningsId() { return inningsId; }
    public void setInningsId(int inningsId) { this.inningsId = inningsId; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public int getRuns() { return runs; }
    public void setRuns(int runs) { this.runs = runs; }
    public int getBallsFaced() { return ballsFaced; }
    public void setBallsFaced(int ballsFaced) { this.ballsFaced = ballsFaced; }
    public int getFours() { return fours; }
    public void setFours(int fours) { this.fours = fours; }
    public int getSixes() { return sixes; }
    public void setSixes(int sixes) { this.sixes = sixes; }
    public BigDecimal getStrikeRate() { return strikeRate; }
    public void setStrikeRate(BigDecimal strikeRate) { this.strikeRate = strikeRate; }
    public String getDismissalText() { return dismissalText; }
    public void setDismissalText(String dismissalText) { this.dismissalText = dismissalText; }
    public boolean isBatting() { return isBatting; }
    public void setBatting(boolean batting) { isBatting = batting; }
    public int getBattingOrder() { return battingOrder; }
    public void setBattingOrder(int battingOrder) { this.battingOrder = battingOrder; }
}
