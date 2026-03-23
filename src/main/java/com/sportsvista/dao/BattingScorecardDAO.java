package com.sportsvista.dao;

import com.sportsvista.model.BattingScorecard;
import com.sportsvista.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BattingScorecardDAO {

    public void clearInningsData(int inningsId) throws SQLException {
        String sql = "DELETE FROM batting_scorecards WHERE innings_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inningsId);
            stmt.executeUpdate();
        }
    }

    public void insert(BattingScorecard b) throws SQLException {
        String sql = "INSERT INTO batting_scorecards (innings_id, player_name, runs, balls_faced, fours, sixes, strike_rate, dismissal_text, is_batting, batting_order) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getInningsId());
            stmt.setString(2, b.getPlayerName());
            stmt.setInt(3, b.getRuns());
            stmt.setInt(4, b.getBallsFaced());
            stmt.setInt(5, b.getFours());
            stmt.setInt(6, b.getSixes());
            stmt.setBigDecimal(7, b.getStrikeRate());
            stmt.setString(8, b.getDismissalText());
            stmt.setBoolean(9, b.isBatting());
            stmt.setInt(10, b.getBattingOrder());
            stmt.executeUpdate();
        }
    }

    public List<BattingScorecard> getByInningsId(int inningsId) throws SQLException {
        String sql = "SELECT * FROM batting_scorecards WHERE innings_id = ? ORDER BY batting_order ASC";
        List<BattingScorecard> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inningsId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BattingScorecard b = new BattingScorecard();
                    b.setId(rs.getInt("id"));
                    b.setInningsId(rs.getInt("innings_id"));
                    b.setPlayerName(rs.getString("player_name"));
                    b.setRuns(rs.getInt("runs"));
                    b.setBallsFaced(rs.getInt("balls_faced"));
                    b.setFours(rs.getInt("fours"));
                    b.setSixes(rs.getInt("sixes"));
                    b.setStrikeRate(rs.getBigDecimal("strike_rate"));
                    b.setDismissalText(rs.getString("dismissal_text"));
                    b.setBatting(rs.getBoolean("is_batting"));
                    b.setBattingOrder(rs.getInt("batting_order"));
                    list.add(b);
                }
            }
        }
        return list;
    }
}
