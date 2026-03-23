package com.sportsvista.servlet;

import com.sportsvista.dao.MatchDAO;
import com.sportsvista.dao.SportDAO;
import com.sportsvista.model.Match;
import com.sportsvista.model.Sport;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final MatchDAO matchDAO = new MatchDAO();
    private final SportDAO sportDAO = new SportDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String sportIdParam = req.getParameter("sportId");
            List<Sport> sports;
            if (sportIdParam != null && !sportIdParam.isEmpty()) {
                int sportId = Integer.parseInt(sportIdParam);
                Sport s = sportDAO.getSportById(sportId);
                sports = new ArrayList<>();
                if (s != null) sports.add(s);
            } else {
                sports = sportDAO.getAllSports();
            }

            Map<String, List<Match>> matchesBySport = new HashMap<>();
            
            int totalMatches = 0;
            for (Sport s : sports) {
                List<Match> matches = matchDAO.getDashboardMatches(s.getId());
                matchesBySport.put(s.getSportKey(), matches);
                totalMatches += matches.size();
            }
            System.out.println("DashboardServlet: sportId=" + sportIdParam + ", Filtered Sports Count: " + sports.size() + ", Total Matches: " + totalMatches);

            req.setAttribute("sports", sports);
            req.setAttribute("sports_all", sportDAO.getAllSports());
            req.setAttribute("matchesBySport", matchesBySport);
            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
