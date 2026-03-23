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
import java.util.stream.Collectors;

@WebServlet("/api/ticker")
public class TickerApiServlet extends HttpServlet {
    private final MatchDAO matchDAO = new MatchDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Match> liveMatches = matchDAO.getDashboardMatches(0); // 0 for all sports
            
            List<String> tickerTexts = liveMatches.stream()
                .map(m -> m.getTeam1Name() + " " + m.getScoreDisplayTeam1() + " vs " + 
                          m.getTeam2Name() + " " + m.getScoreDisplayTeam2() + " (" + m.getStatusSummaryText() + ")")
                .collect(Collectors.toList());
                
            if (tickerTexts.isEmpty()) {
                tickerTexts.add("Stay tuned for live sports action!");
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(tickerTexts));
        } catch (Exception e) {
            response.sendError(500, e.getMessage());
        }
    }
}
