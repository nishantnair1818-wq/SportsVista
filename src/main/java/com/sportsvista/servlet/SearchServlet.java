package com.sportsvista.servlet;

import com.google.gson.Gson;
import com.sportsvista.dao.MatchDAO;
import com.sportsvista.model.Match;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/search")
public class SearchServlet extends HttpServlet {
    private final MatchDAO matchDAO = new MatchDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        if (query == null || query.trim().isEmpty()) {
            resp.getWriter().write("[]");
            return;
        }

        try {
            // Simplified search: just return all matches and filter in memory for now
            // In a real app, this would be a DB LIKE query
            List<Match> allMatches = matchDAO.getLiveMatches(0);
            List<Match> filtered = allMatches.stream()
                .filter(m -> m.getTeam1Name().toLowerCase().contains(query.toLowerCase()) || 
                             m.getTeam2Name().toLowerCase().contains(query.toLowerCase()) ||
                             m.getCompetitionName().toLowerCase().contains(query.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());

            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(filtered));
        } catch (Exception e) {
            resp.sendError(500, e.getMessage());
        }
    }
}
