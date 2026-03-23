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

@WebServlet("/api/matches")
public class MatchApiServlet extends HttpServlet {

    private final MatchDAO matchDAO = new MatchDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sportStr = req.getParameter("sportId");
        int sportId = (sportStr != null) ? Integer.parseInt(sportStr) : 0;

        try {
            List<Match> matches = matchDAO.getLiveMatches(sportId);
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(matches));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
