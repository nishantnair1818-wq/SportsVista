package com.sportsvista.dao;

import com.sportsvista.model.Sport;
import com.sportsvista.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportDAO {

    public Sport getSportById(int id) throws SQLException {
        String sql = "SELECT * FROM sports WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Sport s = new Sport();
                    s.setId(rs.getInt("id"));
                    s.setSportKey(rs.getString("sport_key"));
                    s.setSportName(rs.getString("sport_name"));
                    s.setDisplayOrder(rs.getInt("display_order"));
                    return s;
                }
            }
        }
        return null;
    }

    public List<Sport> getAllSports() throws SQLException {
        String sql = "SELECT * FROM sports ORDER BY display_order ASC";
        List<Sport> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Sport s = new Sport();
                s.setId(rs.getInt("id"));
                s.setSportKey(rs.getString("sport_key"));
                s.setSportName(rs.getString("sport_name"));
                s.setDisplayOrder(rs.getInt("display_order"));
                list.add(s);
            }
        }
        return list;
    }
}
