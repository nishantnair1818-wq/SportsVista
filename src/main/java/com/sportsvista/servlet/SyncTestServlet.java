package com.sportsvista.servlet;

import com.sportsvista.dao.*;
import com.sportsvista.model.*;
import com.sportsvista.scheduler.ScorecardSyncScheduler;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/debug/sync-test")
public class SyncTestServlet extends HttpServlet {
    private final MatchDAO matchDAO = new MatchDAO();
    private final MatchEventDAO eventDAO = new MatchEventDAO();
    private final MatchLineupDAO lineupDAO = new MatchLineupDAO();
    private final InningsDAO inningsDAO = new InningsDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.println("--- SportsVista Sync Debug ---");

        try {
            // 1. Check database counts
            out.println("Database Status:");
            out.println("- Matches (Total): " + matchDAO.getDashboardMatches(0).size());
            out.println("- Match Events: " + eventDAO.getByMatchId(0).size()); // This query might be weird, but let's see
            out.println("- Match Lineups: " + lineupDAO.getByMatchId(0).size());
            out.println("- Innings (Cricket): " + inningsDAO.getByMatchId(0).size());

            // 2. Check for a specific live match
            List<Match> liveMatches = matchDAO.getMatchesByStatus(0, "live");
            out.println("\nLive Matches Found: " + liveMatches.size());
            for (Match m : liveMatches) {
                out.println("  Match ID: " + m.getId() + " | External ID: " + m.getExternalId() + " | " + m.getTeam1Name() + " vs " + m.getTeam2Name());
                List<MatchEvent> events = eventDAO.getByMatchId(m.getId());
                out.println("    Events synced: " + events.size());
                List<MatchLineup> lineups = lineupDAO.getByMatchId(m.getId());
                out.println("    Lineups synced: " + lineups.size());
            }

            // 3. Try to trigger a manual sync for one match
            out.println("\nTriggering manual sync for ALL matches...");
            ScorecardSyncScheduler scheduler = new ScorecardSyncScheduler();
            scheduler.syncAllDetails();
            out.println("Sync triggered.");

        } catch (Exception e) {
            out.println("\nERROR during debug:");
            e.printStackTrace(out);
        }
    }
}
