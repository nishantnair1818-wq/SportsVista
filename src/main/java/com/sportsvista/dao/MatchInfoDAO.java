package com.sportsvista.dao;

import com.sportsvista.model.MatchInfo;
import com.sportsvista.util.DBConnection;
import java.sql.*;

public class MatchInfoDAO {

    public void upsertInfo(MatchInfo info) throws SQLException {
        String sql;
        if (DBConnection.isPostgres()) {
            sql = "INSERT INTO match_info (match_id, umpires, match_referee, tv_umpire, attendance, weather_conditions, " +
                  "pitch_conditions, toss_winner, toss_decision, series_context, extra_info_json) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                  "ON CONFLICT (match_id) DO UPDATE SET " +
                  "umpires=EXCLUDED.umpires, match_referee=EXCLUDED.match_referee, tv_umpire=EXCLUDED.tv_umpire, " +
                  "attendance=EXCLUDED.attendance, weather_conditions=EXCLUDED.weather_conditions, pitch_conditions=EXCLUDED.pitch_conditions, " +
                  "toss_winner=EXCLUDED.toss_winner, toss_decision=EXCLUDED.toss_decision, series_context=EXCLUDED.series_context, " +
                  "extra_info_json=EXCLUDED.extra_info_json";
        } else {
            sql = "INSERT INTO match_info (match_id, umpires, match_referee, tv_umpire, attendance, weather_conditions, " +
                  "pitch_conditions, toss_winner, toss_decision, series_context, extra_info_json) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                  "ON DUPLICATE KEY UPDATE " +
                  "umpires=VALUES(umpires), match_referee=VALUES(match_referee), tv_umpire=VALUES(tv_umpire), " +
                  "attendance=VALUES(attendance), weather_conditions=VALUES(weather_conditions), pitch_conditions=VALUES(pitch_conditions), " +
                  "toss_winner=VALUES(toss_winner), toss_decision=VALUES(toss_decision), series_context=VALUES(series_context), " +
                  "extra_info_json=VALUES(extra_info_json)";
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, info.getMatchId());
            stmt.setString(2, info.getUmpires());
            stmt.setString(3, info.getMatchReferee());
            stmt.setString(4, info.getTvUmpire());
            stmt.setObject(5, info.getAttendance());
            stmt.setString(6, info.getWeatherConditions());
            stmt.setString(7, info.getPitchConditions());
            stmt.setString(8, info.getTossWinner());
            stmt.setString(9, info.getTossDecision());
            stmt.setString(10, info.getSeriesContext());
            stmt.setString(11, info.getExtraInfoJson());

            stmt.executeUpdate();
        }
    }

    public MatchInfo getByMatchId(int matchId) throws SQLException {
        String sql = "SELECT * FROM match_info WHERE match_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MatchInfo i = new MatchInfo();
                    i.setId(rs.getInt("id"));
                    i.setMatchId(rs.getInt("match_id"));
                    i.setUmpires(rs.getString("umpires"));
                    i.setMatchReferee(rs.getString("match_referee"));
                    i.setTvUmpire(rs.getString("tv_umpire"));
                    i.setAttendance((Integer) rs.getObject("attendance"));
                    i.setWeatherConditions(rs.getString("weather_conditions"));
                    i.setPitchConditions(rs.getString("pitch_conditions"));
                    i.setTossWinner(rs.getString("toss_winner"));
                    i.setTossDecision(rs.getString("toss_decision"));
                    i.setSeriesContext(rs.getString("series_context"));
                    i.setExtraInfoJson(rs.getString("extra_info_json"));
                    return i;
                }
            }
        }
        return null;
    }
}
