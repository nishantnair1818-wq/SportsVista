package com.sportsvista.dao;

import com.sportsvista.model.BowlingScorecard;
import com.sportsvista.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BowlingScorecardDAO {

    public void clearInningsData(int inningsId) throws SQLException {
        String sql = "DELETE FROM bowling_scorecards WHERE innings_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inningsId);
            stmt.executeUpdate();
        }
    }

    public void insert(BowlingScorecard b) throws SQLException {
        String sql = "INSERT INTO bowling_scorecards (innings_id, player_name, overs, maidens, runs_given, wickets, economy) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getInningsId());
            stmt.setString(2, b.getPlayerName());
            stmt.setString(3, b.getOvers());
            stmt.setInt(4, b.getMaidens());
            stmt.setInt(5, b.getRunsGiven());
            stmt.setInt(6, b.getWickets());
            stmt.setBigDecimal(7, b.getEconomy());
            stmt.executeUpdate();
        }
    }

    public List<BowlingScorecard> getByInningsId(int inningsId) throws SQLException {
        String sql = "SELECT * FROM bowling_scorecards WHERE innings_id = ?";
        List<BowlingScorecard> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inningsId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BowlingScorecard b = new BowlingScorecard();
                    b.setId(rs.getInt("id"));
                    b.setInningsId(rs.getInt("innings_id"));
                    b.setPlayerName(rs.getString("player_name"));
                    b.setOvers(rs.getString("overs"));
                    b.setMaidens(rs.getInt("maidens"));
                    b.setRunsGiven(rs.getInt("runs_given"));
                    b.setWickets(rs.getInt("wickets"));
                    b.setEconomy(rs.getBigDecimal("economy"));
                    list.add(b);
                }
            }
        }
        return list;
    }
}
