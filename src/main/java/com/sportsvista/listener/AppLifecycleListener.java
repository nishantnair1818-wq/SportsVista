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
        // 1. Initialize Sport Registry
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

        // 2. Start Schedulers
        scheduler.scheduleAtFixedRate(liveSync::syncAll, 0, 120, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(scorecardSync::syncAllDetails, 5, 120, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(metaSync::syncMetadata, 0, 24, TimeUnit.HOURS);
        
        System.out.println("SportsVista Schedulers Started.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        System.out.println("SportsVista Schedulers Stopped.");
    }
}
