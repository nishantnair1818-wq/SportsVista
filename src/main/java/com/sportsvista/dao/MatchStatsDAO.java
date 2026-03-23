package com.sportsvista.dao;

import com.sportsvista.model.MatchStats;
import com.sportsvista.util.DBConnection;
import java.sql.*;

public class MatchStatsDAO {

    public void upsertStats(MatchStats stats) throws SQLException {
        String sql;
        if (DBConnection.isPostgres()) {
            sql = "INSERT INTO match_stats (match_id, stats_json) VALUES (?, ?) " +
                  "ON CONFLICT (match_id) DO UPDATE SET stats_json = EXCLUDED.stats_json, last_updated = CURRENT_TIMESTAMP";
        } else {
            sql = "INSERT INTO match_stats (match_id, stats_json) VALUES (?, ?) " +
                  "ON DUPLICATE KEY UPDATE stats_json = VALUES(stats_json), last_updated = CURRENT_TIMESTAMP";
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stats.getMatchId());
            stmt.setString(2, stats.getStatsJson());
            stmt.executeUpdate();
        }
    }

    public MatchStats getByMatchId(int matchId) throws SQLException {
        String sql = "SELECT * FROM match_stats WHERE match_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MatchStats s = new MatchStats();
                    s.setId(rs.getInt("id"));
                    s.setMatchId(rs.getInt("match_id"));
                    s.setStatsJson(rs.getString("stats_json"));
                    s.setLastUpdated(rs.getTimestamp("last_updated"));
                    return s;
                }
            }
        }
        return null;
    }
}
