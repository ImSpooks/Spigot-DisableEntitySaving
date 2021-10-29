package me.imspooks.des.tempentities;

import me.imspooks.des.Des;
import me.imspooks.des.tempentities.impl.TemporaryEntityApiImpl;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.ref.WeakReference;
import java.util.function.Function;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public class TemporaryEntity<T extends Entity> {

    private final Function<Location, T> entitySupplier;
    private WeakReference<T> entityReference;
    private Location despawnLocation;


    public TemporaryEntity(Function<Location, T> entitySupplier) {
        this.entitySupplier = entitySupplier;

        ((TemporaryEntityApiImpl) Des.getInstance().getTemporaryEntityApi()).getStorage().addEntity(this);
    }

    /**
     * Get the entity instance of this temporary entity.
     * This method will automatically spawn the entity if it does not exist;
     *
     * @return The entity instance
     */
    public T getEntity() {
        // The entity has not been spawned yet
        if (this.entityReference == null) {
            return null;
        }

        return this.entityReference.get();
    }

    /**
     * @return {@code true} if the entity is valid, {@code false} otherwise
     */
    public boolean isValid() {
        return this.getEntity() != null;
    }

    /**
     * Spawns the entity at given location.
     * Only spawns if the chunk is loaded.
     *
     * @param location Location to spawn
     * @return This instance
     */
    public TemporaryEntity<T> spawn(Location location) {
        if (location.getChunk().isLoaded()) {
            // Spawn the entity
            T entity = this.entitySupplier.apply(this.despawnLocation = location);
            this.entityReference = new WeakReference<>(entity);

            // Disable saving on this entity
            Des.getInstance().getApi().addEntity(entity);
        } else {
            this.despawnLocation = location;
            this.entityReference = new WeakReference<>(null);
        }
        return this;
    }

    /**
     * Despawns the entity
     */
    public void release() {
        // Removes the entity if it exists
        Entity entity = this.getEntity();
        if (entity != null) {
            entity.remove();
        }

        // Removes the entity if it exists
        ((TemporaryEntityApiImpl) Des.getInstance().getTemporaryEntityApi()).getStorage().removeEntity(this);
    }

    /**
     * Get the last seen location
     *
     * @return Location where it was despawned
     */
    public Location getDespawnLocation() {
        return despawnLocation;
    }
    /**
     * Set the last seen location
     *
     * @param despawnLocation New last location
     */
    public void setDespawnLocation(Location despawnLocation) {
        this.despawnLocation = despawnLocation;
    }
}