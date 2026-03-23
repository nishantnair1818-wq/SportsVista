package com.sportsvista.servlet;

import com.sportsvista.dao.*;
import com.sportsvista.model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/match/*")
public class MatchDetailServlet extends HttpServlet {

    private final MatchDAO matchDAO = new MatchDAO();
    private final InningsDAO inningsDAO = new InningsDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int matchId = Integer.parseInt(pathInfo.substring(1));
            Match match = matchDAO.getById(matchId);
            if (match == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            req.setAttribute("match", match);
            
            // Fetch detail based on sport
            if (match.getSportId() == 1) { // Cricket (Assumed ID from seed)
                List<MatchInnings> innings = inningsDAO.getByMatchId(matchId);
                req.setAttribute("innings", innings);
            }
            // Non-cricket detail (events, lineups) used to be fetched here, 
            // but now handled via AJAX in match-detail.jsp + MatchDetailApiServlet.

            req.getRequestDispatcher("/match-detail.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
