package com.sportsvista.dao;

import com.sportsvista.model.Team;
import com.sportsvista.util.DBConnection;
import java.sql.*;

public class TeamDAO {

    public void upsertTeam(Team team) throws SQLException {
        String sql = "INSERT INTO teams (external_id, team_name, short_name, sport_id, country, logo_url, team_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "team_name=VALUES(team_name), short_name=VALUES(short_name), logo_url=VALUES(logo_url)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, team.getExternalId());
            stmt.setString(2, team.getTeamName());
            stmt.setString(3, team.getShortName());
            stmt.setInt(4, team.getSportId());
            stmt.setString(5, team.getCountry());
            stmt.setString(6, team.getLogoUrl());
            stmt.setString(7, team.getTeamType());

            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    team.setId(generatedKeys.getInt(1));
                } else {
                    team.setId(getIdByExternalId(team.getExternalId()));
                }
            }
        }
    }

    public int getIdByExternalId(String externalId) throws SQLException {
        String sql = "SELECT id FROM teams WHERE external_id = ?";
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
