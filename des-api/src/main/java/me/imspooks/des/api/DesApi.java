package me.imspooks.des.api;

import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public interface DesApi {

    /**
     * @return Unmodifiable collection of entities
     */
    Collection<UUID> getEntities();

    /**
     * @return Entity housekeeper
     */
    DesHousekeeper getHousekeeper();

    /**
     * Add an entity to the collection
     *
     * @param entity Target entity
     * @return {@see Set#add(E)}
     */
    default boolean addEntity(Entity entity) {
        return this.addEntity(entity.getUniqueId());
    }

    boolean addEntity(UUID uuid);

    /**
     * Removes an entity from the collection
     *
     * @param entity Target entity
     * @return {@see Set#remove(E)}
     */
    default boolean removeEntity(Entity entity) {
        return this.removeEntity(entity.getUniqueId());
    }

    boolean removeEntity(UUID uuid);

    /**
     * Checks if the entity is stored in the collection
     *
     * @param entity Target entity
     * @return {@code true} if the entity is stored in the collection, {@code false} otherwise. {@see Set#contains(E)}
     */
    default boolean containsEntity(Entity entity) {
        return getEntities().contains(entity.getUniqueId());
    }

    default boolean containsEntity(UUID uuid) {
        return getEntities().contains(uuid);
    }
}