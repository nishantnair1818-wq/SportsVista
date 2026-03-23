package com.sportsvista.model;

public class Competition {
    private int id;
    private String externalId;
    private String competitionName;
    private String shortName;
    private int sportId;
    private String competitionType;
    private String country;
    private String season;
    private String logoUrl;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public String getCompetitionName() { return competitionName; }
    public void setCompetitionName(String competitionName) { this.competitionName = competitionName; }
    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public int getSportId() { return sportId; }
    public void setSportId(int sportId) { this.sportId = sportId; }
    public String getCompetitionType() { return competitionType; }
    public void setCompetitionType(String competitionType) { this.competitionType = competitionType; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
