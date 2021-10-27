package me.imspooks.des.api;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.logging.Level;

/**
 * Housekeeper of the Des entity collection
 */
class DesHousekeeper implements Runnable {

    public static final boolean DEBUG = Boolean.getBoolean("desDebug");

    private final DesImpl main;

    public DesHousekeeper(DesImpl main) {
        this.main = main;
    }

    @Override
    public void run() {
        this.runEntityCleanup();
    }

    /**
     * Runs the entity cleanup, checks if entity is valid or not
     */
    private void runEntityCleanup() {
        long time = System.nanoTime();
        log("Entites cache size before: " + main.getEntities().size());

        // Clean up entities
        main.getEntities().removeIf(entities -> {
            for (World world : Bukkit.getWorlds()) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (entity.getUniqueId().equals(entities)) {
                        return false;
                    }
                }
            }
            return true;
        });

        log("Entites cache size after: " + main.getEntities().size());
        log("Entities metadata cache housekeeping took " + ms(time) + "ms. ");

        main.setLastEntitiesHousekeeping(ms(time));
    }

    private void log(String str) {
        if (DEBUG) {
            main.getPlugin().getLogger().log(Level.INFO, str);
        }
    }

    private String ms(Long time) {
        final long ns = System.nanoTime() - time;
        final float ms = ns / 1_000_000.0f;
        return String.format("%.3f", ms);
    }
}