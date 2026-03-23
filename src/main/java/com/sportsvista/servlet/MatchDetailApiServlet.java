package com.sportsvista.servlet;

import com.google.gson.Gson;
import com.sportsvista.dao.MatchEventDAO;
import com.sportsvista.dao.MatchLineupDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/match-details")
public class MatchDetailApiServlet extends HttpServlet {
    private final MatchEventDAO eventDAO = new MatchEventDAO();
    private final MatchLineupDAO lineupDAO = new MatchLineupDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matchIdStr = req.getParameter("matchId");
        if (matchIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int matchId = Integer.parseInt(matchIdStr);
            Map<String, Object> data = new HashMap<>();
            data.put("events", eventDAO.getByMatchId(matchId));
            data.put("lineups", lineupDAO.getByMatchId(matchId));
            
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(data));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
