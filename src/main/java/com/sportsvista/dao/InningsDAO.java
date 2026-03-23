package com.sportsvista.dao;

import com.sportsvista.model.MatchInnings;
import com.sportsvista.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InningsDAO {

    public void upsertInnings(MatchInnings innings) throws SQLException {
        String sql = "INSERT INTO match_innings (match_id, innings_number, batting_team_id, total_runs, total_wickets, total_overs, extras, innings_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "total_runs=VALUES(total_runs), total_wickets=VALUES(total_wickets), total_overs=VALUES(total_overs), " +
                     "extras=VALUES(extras), innings_status=VALUES(innings_status)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, innings.getMatchId());
            stmt.setInt(2, innings.getInningsNumber());
            stmt.setInt(3, innings.getBattingTeamId());
            stmt.setInt(4, innings.getTotalRuns());
            stmt.setInt(5, innings.getTotalWickets());
            stmt.setString(6, innings.getTotalOvers());
            stmt.setInt(7, innings.getExtras());
            stmt.setString(8, innings.getInningsStatus());

            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    innings.setId(generatedKeys.getInt(1));
                } else {
                    innings.setId(getId(innings.getMatchId(), innings.getInningsNumber()));
                }
            }
        }
    }

    public int getId(int matchId, int inningsNumber) throws SQLException {
        String sql = "SELECT id FROM match_innings WHERE match_id = ? AND innings_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            stmt.setInt(2, inningsNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        return -1;
    }

    public List<MatchInnings> getByMatchId(int matchId) throws SQLException {
        String sql = "SELECT i.*, t.team_name as batting_team_name " +
                     "FROM match_innings i " +
                     "LEFT JOIN teams t ON i.batting_team_id = t.id " +
                     "WHERE i.match_id = ? ORDER BY i.innings_number ASC";
        List<MatchInnings> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MatchInnings i = new MatchInnings();
                    i.setId(rs.getInt("id"));
                    i.setMatchId(rs.getInt("match_id"));
                    i.setInningsNumber(rs.getInt("innings_number"));
                    i.setBattingTeamId(rs.getInt("batting_team_id"));
                    i.setTotalRuns(rs.getInt("total_runs"));
                    i.setTotalWickets(rs.getInt("total_wickets"));
                    i.setTotalOvers(rs.getString("total_overs"));
                    i.setExtras(rs.getInt("extras"));
                    i.setInningsStatus(rs.getString("innings_status"));
                    i.setBattingTeamName(rs.getString("batting_team_name"));
                    list.add(i);
                }
            }
        }
        return list;
    }
}
