package com.sportsvista.servlet;

import com.google.gson.Gson;
import com.sportsvista.dao.BattingScorecardDAO;
import com.sportsvista.dao.BowlingScorecardDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/scorecard")
public class ScorecardApiServlet extends HttpServlet {

    private final BattingScorecardDAO battingDAO = new BattingScorecardDAO();
    private final BowlingScorecardDAO bowlingDAO = new BowlingScorecardDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String inningsIdStr = req.getParameter("inningsId");
        if (inningsIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int inningsId = Integer.parseInt(inningsIdStr);
            Map<String, Object> data = new HashMap<>();
            data.put("batting", battingDAO.getByInningsId(inningsId));
            data.put("bowling", bowlingDAO.getByInningsId(inningsId));
            
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(data));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
