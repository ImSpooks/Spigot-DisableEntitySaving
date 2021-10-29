package me.imspooks.des.tempentities;

import me.imspooks.des.Des;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public class TemporaryEntityListener implements Listener {

    private Set<TemporaryEntity<?>> despawnedEntities = new HashSet<>();

    /**
     * Keeps track of temporary entities when unloading chunk
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        // Loop through temporary entities
        tempEntitiesLoop: for (TemporaryEntity<?> temporaryEntity : Des.getInstance().getTemporaryEntityApi().getEntities()) {
            // Loop through chunk entities
            for (Entity entity : event.getChunk().getEntities()) {
                // Check if entity is eqial
                if (entity.equals(temporaryEntity.getEntity())) {
                    // Update despawn location and add to list
                    temporaryEntity.setDespawnLocation(entity.getLocation());

                    despawnedEntities.add(temporaryEntity);
                    continue tempEntitiesLoop;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.isNewChunk())
            return;

        despawnedEntities.removeIf(entity -> {
            if (entity.getDespawnLocation().getChunk().equals(event.getChunk())) {
                entity.spawn(entity.getDespawnLocation());
                return true;
            }
            return false;
        });
    }
}