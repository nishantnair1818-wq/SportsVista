package com.sportsvista.dao;

import com.sportsvista.model.MatchLineup;
import com.sportsvista.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchLineupDAO {

    public void clearMatchLineups(int matchId) throws SQLException {
        String sql = "DELETE FROM match_lineups WHERE match_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            stmt.executeUpdate();
        }
    }

    public void insert(MatchLineup l) throws SQLException {
        String sql = "INSERT INTO match_lineups (match_id, team_id, player_name, shirt_number, position, is_starter, is_captain) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, l.getMatchId());
            stmt.setInt(2, l.getTeamId());
            stmt.setString(3, l.getPlayerName());
            stmt.setInt(4, l.getShirtNumber());
            stmt.setString(5, l.getPosition());
            stmt.setBoolean(6, l.isStarter());
            stmt.setBoolean(7, l.isCaptain());
            stmt.executeUpdate();
        }
    }

    public List<MatchLineup> getByMatchId(int matchId) throws SQLException {
        String sql = "SELECT * FROM match_lineups WHERE match_id = ? ORDER BY team_id, is_starter DESC, shirt_number ASC";
        List<MatchLineup> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MatchLineup l = new MatchLineup();
                    l.setId(rs.getInt("id"));
                    l.setMatchId(rs.getInt("match_id"));
                    l.setTeamId(rs.getInt("team_id"));
                    l.setPlayerName(rs.getString("player_name"));
                    l.setShirtNumber(rs.getInt("shirt_number"));
                    l.setPosition(rs.getString("position"));
                    l.setStarter(rs.getBoolean("is_starter"));
                    l.setCaptain(rs.getBoolean("is_captain"));
                    list.add(l);
                }
            }
        }
        return list;
    }
}
