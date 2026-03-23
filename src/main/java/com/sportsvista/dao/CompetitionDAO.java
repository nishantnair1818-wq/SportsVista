package com.sportsvista.dao;

import com.sportsvista.model.Competition;
import com.sportsvista.util.DBConnection;
import java.sql.*;

public class CompetitionDAO {

    public void upsertCompetition(Competition comp) throws SQLException {
        String sql;
        if (DBConnection.isPostgres()) {
            sql = "INSERT INTO competitions (external_id, competition_name, short_name, sport_id, competition_type, country, season, logo_url) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                  "ON CONFLICT (external_id) DO UPDATE SET " +
                  "competition_name=EXCLUDED.competition_name, short_name=EXCLUDED.short_name, logo_url=EXCLUDED.logo_url, season=EXCLUDED.season";
        } else {
            sql = "INSERT INTO competitions (external_id, competition_name, short_name, sport_id, competition_type, country, season, logo_url) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                  "ON DUPLICATE KEY UPDATE " +
                  "competition_name=VALUES(competition_name), short_name=VALUES(short_name), logo_url=VALUES(logo_url), season=VALUES(season)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, comp.getExternalId());
            stmt.setString(2, comp.getCompetitionName());
            stmt.setString(3, comp.getShortName());
            stmt.setInt(4, comp.getSportId());
            stmt.setString(5, comp.getCompetitionType());
            stmt.setString(6, comp.getCountry());
            stmt.setString(7, comp.getSeason());
            stmt.setString(8, comp.getLogoUrl());

            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comp.setId(generatedKeys.getInt(1));
                } else {
                    comp.setId(getIdByExternalId(comp.getExternalId()));
                }
            }
        }
    }

    public int getIdByExternalId(String externalId) throws SQLException {
        String sql = "SELECT id FROM competitions WHERE external_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, externalId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        return -1;
    }
}
