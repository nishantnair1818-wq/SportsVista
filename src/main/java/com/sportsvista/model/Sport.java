package com.sportsvista.model;

public class Sport {
    private int id;
    private String sportKey;
    private String sportName;
    private int displayOrder;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSportKey() { return sportKey; }
    public void setSportKey(String sportKey) { this.sportKey = sportKey; }
    public String getSportName() { return sportName; }
    public void setSportName(String sportName) { this.sportName = sportName; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
