package me.imspooks.des.api.impl;

import lombok.Getter;
import lombok.Setter;
import me.imspooks.des.Des;
import me.imspooks.des.api.DesHousekeeper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.concurrent.TimeUnit;

/**
 * Housekeeper of the Des entity collection
 */
@Getter
@Setter
public class DesHousekeeperImpl implements DesHousekeeper, Runnable {

    public static final boolean DEBUG = Boolean.getBoolean("desDebug");

    private final Des plugin;

    private long lastCleanup = 0;
    private long delay = 600;

    public DesHousekeeperImpl(Des plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        if (now - lastCleanup > TimeUnit.SECONDS.toMillis(this.delay)) {
            this.runEntityCleanup();
            this.lastCleanup = now;
        }
    }

    public void runEntityCleanup() {
        long time = System.nanoTime();

        DesApiImpl desApi = (DesApiImpl) plugin.getApi();

        log("Entites cache size before: " + desApi.getModifiableEntities().size());

        // Clean up entities
        desApi.getModifiableEntities().removeIf(entities -> {
            for (World world : Bukkit.getWorlds()) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (entity.getUniqueId().equals(entities)) {
                        return false;
                    }
                }
            }
            return true;
        });

        log("Entites cache size after: " + desApi.getModifiableEntities().size());
        log("Entities metadata cache housekeeping took " + ms(time) + "ms. ");


    }

    private void log(String str) {
        if (DEBUG) {
            System.out.println(str);
        }
    }

    private String ms(Long time) {
        final long ns = System.nanoTime() - time;
        final float ms = ns / 1_000_000.0f;
        return String.format("%.3f", ms);
    }
}