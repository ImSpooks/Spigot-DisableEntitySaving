package me.imspooks.des.api;

import lombok.Getter;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

/**
 * Created by Nick on 28 Oct 2021.
 * Copyright © ImSpooks
 */
public abstract class Des {
    /**
     * Get the instance of this class.
     * Only works after {@see DisableEntitySaving#initialize(JavaPlugin)} is called
     *
     * @return Instance of this class
     */
    @Getter private static Des instance;

    /**
     * Initializes the API
     *
     * @param des Core responding des instance
     */
    public static void initialize(Des des) {
        if (instance != null) {
            throw new AssertionError("Instance is already initialized.");
        }
        instance = des;
    }

    /**
     * Add an entity to the collection
     *
     * @param entity Target entity
     * @return {@see Set#add(E)}
     */
    public abstract boolean addEntity(LivingEntity entity);

    public abstract boolean addEntity(UUID uuid);

    /**
     * Removes an entity from the collection
     *
     * @param entity Target entity
     * @return {@see Set#remove(E)}
     */
    public abstract boolean removeEntity(LivingEntity entity);

    public abstract boolean removeEntity(UUID uuid);

    /**
     * Checks if the entity is stored in the collection
     *
     * @param entity Target entity
     * @return {@code true} if the entity is stored in the collection, {@code false} otherwise. {@see Set#contains(E)}
     */
    public abstract boolean containsEntity(LivingEntity entity);

    public abstract boolean containsEntity(UUID uuid);
}