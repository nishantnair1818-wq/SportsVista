package com.sportsvista.listener;

import com.sportsvista.scheduler.LiveSyncScheduler;
import com.sportsvista.scheduler.MetaSyncScheduler;
import com.sportsvista.scheduler.ScorecardSyncScheduler;
import com.sportsvista.dao.SportDAO;
import com.sportsvista.model.Sport;
import com.sportsvista.model.SportRegistry;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppLifecycleListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 1. Auto-initialize Database if empty
        checkAndInitDatabase();

        // 2. Initialize Sport Registry
        try {
            List<Sport> sports = new SportDAO().getAllSports();
            System.out.println("Initializing SportRegistry with " + sports.size() + " sports.");
            for (Sport s : sports) {
                SportRegistry.register(s.getSportKey(), s.getId());
                System.out.println("Registered: " + s.getSportKey() + " -> " + s.getId());
            }
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to initialize SportRegistry!");
            e.printStackTrace();
        }

        scheduler = Executors.newScheduledThreadPool(3);

        LiveSyncScheduler liveSync = new LiveSyncScheduler();
        ScorecardSyncScheduler scorecardSync = new ScorecardSyncScheduler();
        MetaSyncScheduler metaSync = new MetaSyncScheduler();

        // 3. Start Schedulers
        scheduler.scheduleAtFixedRate(liveSync::syncAll, 0, 120, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(scorecardSync::syncAllDetails, 5, 120, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(metaSync::syncMetadata, 0, 24, TimeUnit.HOURS);
        
        System.out.println("SportsVista Schedulers Started.");
    }

    private void checkAndInitDatabase() {
        try (java.sql.Connection conn = com.sportsvista.util.DBConnection.getConnection()) {
            // Check if sports table exists
            boolean empty = false;
            try (java.sql.ResultSet rs = conn.getMetaData().getTables(null, null, "sports", null)) {
                if (!rs.next()) {
                    // Try lowercase check for Postgres compatibility
                    try (java.sql.ResultSet rs2 = conn.getMetaData().getTables(null, null, "sports", null)) {
                        if (!rs2.next()) empty = true;
                    }
                }
            }

            if (empty) {
                System.out.println("Database seems empty. Running auto-initialization...");
                String sqlFile = com.sportsvista.util.DBConnection.isPostgres() ? "database_pg.sql" : "database.sql";
                java.io.File file = new java.io.File(sqlFile);
                if (!file.exists()) {
                    // Try absolute path if in container
                    file = new java.io.File("/app/" + sqlFile);
                }

                if (file.exists()) {
                    String sql = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                    // Split SQL by semicolon, ensuring we skip empty parts
                    String[] statements = sql.split(";");
                    try (java.sql.Statement stmt = conn.createStatement()) {
                        conn.setAutoCommit(false);
                        for (String s : statements) {
                            String trimmed = s.trim();
                            if (!trimmed.isEmpty()) {
                                stmt.addBatch(trimmed);
                            }
                        }
                        stmt.executeBatch();
                        conn.commit();
                        System.out.println("Database auto-initialized successfully using " + sqlFile);
                    } catch (Exception ex) {
                        conn.rollback();
                        throw ex;
                    }
                } else {
                    System.err.println("Auto-init error: Could not find SQL file " + sqlFile);
                }
            }
        } catch (Exception e) {
            System.err.println("Auto-init warning (Normal if DB is already ready): " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        System.out.println("SportsVista Schedulers Stopped.");
    }
}
