package com.sportsvista.model;

public class Team {
    private int id;
    private String externalId;
    private String teamName;
    private String shortName;
    private int sportId;
    private String country;
    private String logoUrl;
    private String teamType;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public int getSportId() { return sportId; }
    public void setSportId(int sportId) { this.sportId = sportId; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getTeamType() { return teamType; }
    public void setTeamType(String teamType) { this.teamType = teamType; }
}
