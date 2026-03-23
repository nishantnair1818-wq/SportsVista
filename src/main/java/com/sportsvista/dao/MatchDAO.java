package com.sportsvista.dao;

import com.sportsvista.model.Match;
import com.sportsvista.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    public void upsertMatch(Match match) throws SQLException {
        String sql;
        if (DBConnection.isPostgres()) {
            sql = "INSERT INTO matches (external_id, sport_id, competition_id, team1_id, team2_id, match_type, match_sub_type, status, " +
                  "score_display_team1, score_display_team2, status_summary_text, match_date, venue, venue_city, venue_country, " +
                  "toss_text, result_text, match_day_label) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                  "ON CONFLICT (external_id) DO UPDATE SET " +
                  "status=EXCLUDED.status, score_display_team1=EXCLUDED.score_display_team1, score_display_team2=EXCLUDED.score_display_team2, " +
                  "status_summary_text=EXCLUDED.status_summary_text, toss_text=EXCLUDED.toss_text, result_text=EXCLUDED.result_text, " +
                  "match_day_label=EXCLUDED.match_day_label, last_updated=CURRENT_TIMESTAMP";
        } else {
            sql = "INSERT INTO matches (external_id, sport_id, competition_id, team1_id, team2_id, match_type, match_sub_type, status, " +
                  "score_display_team1, score_display_team2, status_summary_text, match_date, venue, venue_city, venue_country, " +
                  "toss_text, result_text, match_day_label) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                  "ON DUPLICATE KEY UPDATE " +
                  "status=VALUES(status), score_display_team1=VALUES(score_display_team1), score_display_team2=VALUES(score_display_team2), " +
                  "status_summary_text=VALUES(status_summary_text), toss_text=VALUES(toss_text), result_text=VALUES(result_text), " +
                  "match_day_label=VALUES(match_day_label), last_updated=CURRENT_TIMESTAMP";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, match.getExternalId());
            stmt.setInt(2, match.getSportId());
            stmt.setObject(3, match.getCompetitionId() > 0 ? match.getCompetitionId() : null);
            stmt.setObject(4, match.getTeam1Id() > 0 ? match.getTeam1Id() : null);
            stmt.setObject(5, match.getTeam2Id() > 0 ? match.getTeam2Id() : null);
            stmt.setString(6, match.getMatchType());
            stmt.setString(7, match.getMatchSubType());
            stmt.setString(8, match.getStatus());
            stmt.setString(9, match.getScoreDisplayTeam1());
            stmt.setString(10, match.getScoreDisplayTeam2());
            stmt.setString(11, match.getStatusSummaryText());
            stmt.setTimestamp(12, match.getMatchDate());
            stmt.setString(13, match.getVenue());
            stmt.setString(14, match.getVenueCity());
            stmt.setString(15, match.getVenueCountry());
            stmt.setString(16, match.getTossText());
            stmt.setString(17, match.getResultText());
            stmt.setString(18, match.getMatchDayLabel());

            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    match.setId(generatedKeys.getInt(1));
                } else {
                    match.setId(getIdByExternalId(match.getExternalId()));
                }
            }
        }
    }

    public int getIdByExternalId(String externalId) throws SQLException {
        String sql = "SELECT id FROM matches WHERE external_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, externalId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        return -1;
    }

    public List<Match> getDashboardMatches(int sportId) throws Exception {
        String sql = "SELECT m.*, t1.team_name as team1_name, t1.logo_url as team1_logo, " +
                     "t2.team_name as team2_name, t2.logo_url as team2_logo, " +
                     "c.competition_name as competition_name " +
                     "FROM matches m " +
                     "LEFT JOIN teams t1 ON m.team1_id = t1.id " +
                     "LEFT JOIN teams t2 ON m.team2_id = t2.id " +
                     "LEFT JOIN competitions c ON m.competition_id = c.id " +
                     "WHERE ? = 0 OR m.sport_id = ? " +
                     "ORDER BY CASE " +
                     "  WHEN m.status = 'live' THEN 1 " +
                     "  WHEN m.status = 'upcoming' THEN 2 " +
                     "  ELSE 3 END, m.match_date ASC LIMIT 20";
        return queryMatches(sql, sportId, sportId);
    }

    private List<Match> queryMatches(String sql, Object... params) throws Exception {
        List<Match> matches = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(mapMatch(rs));
                }
            }
        }
        return matches;
    }

    public List<Match> getLiveMatches(int sportId) throws Exception {
        String sql = "SELECT m.*, t1.team_name as team1_name, t1.logo_url as team1_logo, " +
                     "t2.team_name as team2_name, t2.logo_url as team2_logo, c.competition_name " +
                     "FROM matches m " +
                     "LEFT JOIN teams t1 ON m.team1_id = t1.id " +
                     "LEFT JOIN teams t2 ON m.team2_id = t2.id " +
                     "LEFT JOIN competitions c ON m.competition_id = c.id " +
                     "WHERE m.status IN ('live', 'halftime', 'innings_break', 'rain_delay') ";
        
        if (sportId > 0) {
            sql += "AND m.sport_id = ? ";
        }
        
        sql += "ORDER BY m.match_date DESC";

        List<Match> matches = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (sportId > 0) stmt.setInt(1, sportId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(mapMatch(rs));
                }
            }
        }
        return matches;
    }

    public List<Match> getMatchesByStatus(int sportId, String status) throws Exception {
        String sql = "SELECT m.*, t1.team_name as team1_name, t1.logo_url as team1_logo, " +
                     "t2.team_name as team2_name, t2.logo_url as team2_logo, c.competition_name " +
                     "FROM matches m " +
                     "LEFT JOIN teams t1 ON m.team1_id = t1.id " +
                     "LEFT JOIN teams t2 ON m.team2_id = t2.id " +
                     "LEFT JOIN competitions c ON m.competition_id = c.id " +
                     "WHERE m.status = ? ";
        
        if (sportId > 0) {
            sql += "AND m.sport_id = ? ";
            return queryMatches(sql, status, sportId);
        } else {
            return queryMatches(sql, status);
        }
    }

    public Match getById(int id) throws SQLException {
        String sql = "SELECT m.*, t1.team_name as team1_name, t1.logo_url as team1_logo, " +
                     "t2.team_name as team2_name, t2.logo_url as team2_logo, c.competition_name " +
                     "FROM matches m " +
                     "LEFT JOIN teams t1 ON m.team1_id = t1.id " +
                     "LEFT JOIN teams t2 ON m.team2_id = t2.id " +
                     "LEFT JOIN competitions c ON m.competition_id = c.id " +
                     "WHERE m.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapMatch(rs);
            }
        }
        return null;
    }

    private Match mapMatch(ResultSet rs) throws SQLException {
        Match m = new Match();
        m.setId(rs.getInt("id"));
        m.setExternalId(rs.getString("external_id"));
        m.setSportId(rs.getInt("sport_id"));
        m.setCompetitionId(rs.getInt("competition_id"));
        m.setTeam1Id(rs.getInt("team1_id"));
        m.setTeam2Id(rs.getInt("team2_id"));
        m.setMatchType(rs.getString("match_type"));
        m.setMatchSubType(rs.getString("match_sub_type"));
        m.setStatus(rs.getString("status"));
        m.setScoreDisplayTeam1(rs.getString("score_display_team1"));
        m.setScoreDisplayTeam2(rs.getString("score_display_team2"));
        m.setStatusSummaryText(rs.getString("status_summary_text"));
        m.setMatchDate(rs.getTimestamp("match_date"));
        m.setVenue(rs.getString("venue"));
        m.setVenueCity(rs.getString("venue_city"));
        m.setVenueCountry(rs.getString("venue_country"));
        m.setTossText(rs.getString("toss_text"));
        m.setResultText(rs.getString("result_text"));
        m.setMatchDayLabel(rs.getString("match_day_label"));
        m.setLastUpdated(rs.getTimestamp("last_updated"));

        m.setTeam1Name(rs.getString("team1_name"));
        m.setTeam2Name(rs.getString("team2_name"));
        m.setTeam1Logo(rs.getString("team1_logo"));
        m.setTeam2Logo(rs.getString("team2_logo"));
        m.setCompetitionName(rs.getString("competition_name"));
        return m;
    }
}
