package com.sportsvista.dao;

import com.sportsvista.model.MatchEvent;
import com.sportsvista.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchEventDAO {

    public void clearMatchEvents(int matchId) throws SQLException {
        String sql = "DELETE FROM match_events WHERE match_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            stmt.executeUpdate();
        }
    }

    public void insert(MatchEvent e) throws SQLException {
        String sql = "INSERT INTO match_events (match_id, event_type, event_minute, event_second, event_detail, team_id, player_name, event_order) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getMatchId());
            stmt.setString(2, e.getEventType());
            stmt.setString(3, e.getEventMinute());
            stmt.setString(4, e.getEventSecond());
            stmt.setString(5, e.getEventDetail());
            stmt.setObject(6, e.getTeamId() > 0 ? e.getTeamId() : null);
            stmt.setString(7, e.getPlayerName());
            stmt.setInt(8, e.getEventOrder());
            stmt.executeUpdate();
        }
    }

    public List<MatchEvent> getByMatchId(int matchId) throws SQLException {
        String sql;
        if (com.sportsvista.util.DBConnection.isPostgres()) {
            // Postgres needs more care with casting to avoid errors on strings like "90+2"
            sql = "SELECT * FROM match_events WHERE match_id = ? ORDER BY event_order DESC, " +
                  "CASE WHEN event_minute ~ '^[0-9]+$' THEN CAST(event_minute AS INTEGER) ELSE 0 END DESC";
        } else {
            sql = "SELECT * FROM match_events WHERE match_id = ? ORDER BY event_order DESC, CAST(event_minute AS UNSIGNED) DESC";
        }
        List<MatchEvent> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MatchEvent e = new MatchEvent();
                    e.setId(rs.getInt("id"));
                    e.setMatchId(rs.getInt("match_id"));
                    e.setEventType(rs.getString("event_type"));
                    e.setEventMinute(rs.getString("event_minute"));
                    e.setEventSecond(rs.getString("event_second"));
                    e.setEventDetail(rs.getString("event_detail"));
                    e.setTeamId(rs.getInt("team_id"));
                    e.setPlayerName(rs.getString("player_name"));
                    e.setEventOrder(rs.getInt("event_order"));
                    list.add(e);
                }
            }
        }
        return list;
    }
}
