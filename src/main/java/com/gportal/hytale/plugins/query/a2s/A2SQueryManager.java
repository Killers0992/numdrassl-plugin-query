package com.gportal.hytale.plugins.query.a2s;

import com.gportal.hytale.plugins.query.MainClass;
import com.gportal.hytale.plugins.query.Models.PluginConfig;
import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class A2SQueryManager {
    private final Logger logger;
    private final ScheduledExecutorService scheduler;
    private final A2SStatusWriter statusWriter;

    public A2SQueryManager(MainClass plugin, Logger logger) {
        this.logger = logger;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.statusWriter = new A2SStatusWriter(logger);
    }

    public void start() {
        statusWriter.start(PluginConfig.Singleton.QueryHost, PluginConfig.Singleton.QueryPort);

        // Update status every 30 seconds
        scheduler.scheduleAtFixedRate(statusWriter::updateStatus, 30, 30, TimeUnit.SECONDS);

        logger.info("A2S Query Manager started");
    }

    public void stop() {
        statusWriter.stop();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("A2S Query Manager stopped");
    }

    public void updateStatus() {
        statusWriter.updateStatus();
    }
}