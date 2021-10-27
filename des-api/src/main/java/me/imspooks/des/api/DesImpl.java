package me.imspooks.des.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Des "API" Class
 */
@Getter
public class DesImpl extends Des {

    public static Des getInstance() {
        return Des.getInstance();
    }

    // Make sure setup only runs once
    private static final AtomicBoolean SETUP = new AtomicBoolean(false);

    /**
     * Initializes the API
     *
     * @param plugin Core responding plugin
     */
    public static void initialize(JavaPlugin plugin) {
        Des.initialize(new DesImpl(plugin));
    }

    /**
     * Core responding plugin that initialized this class
     *
     * @return Plugin that initialized this instance
     */
    @Getter(value = AccessLevel.PACKAGE) private JavaPlugin plugin;

    /**
     * This is where all the entities (UUIDs) are stored in
     *
     * @return The hash set where the entities are stored in
     */
    private Set<UUID> entities = Collections.synchronizedSet(new HashSet<>());

    /**
     * Keeping track how long the last housekeeping took
     *
     * @return The amount of time the last housekeeping took in milliseconds
     */
    @Setter(value = AccessLevel.PACKAGE) private String lastEntitiesHousekeeping;

    /**
     * Constructor
     *
     * @param plugin Plugin that initialized this instance
     */
    private DesImpl(JavaPlugin plugin) {
        this.plugin = plugin;

        // Setup runnable
        if (SETUP.get()) {
            return;
        }

        if (!SETUP.getAndSet(true)) {
            // cleanup left over world entities
            // cache housekeeping task SYNC
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new DesHousekeeper(this), 900 * 20, 900 * 20);
        }
    }

    /**
     * Add an entity to the collection
     *
     * @param entity Target entity
     * @return {@see Set#add(E)}
     */
    public boolean addEntity(LivingEntity entity) {
        return entities.add(entity.getUniqueId());
    }

    public boolean addEntity(UUID uuid) {
        return entities.add(uuid);
    }

    /**
     * Removes an entity from the collection
     *
     * @param entity Target entity
     * @return {@see Set#remove(E)}
     */
    public boolean removeEntity(LivingEntity entity) {
        return entities.remove(entity.getUniqueId());
    }

    public boolean removeEntity(UUID uuid) {
        return entities.remove(uuid);
    }

    /**
     * Checks if the entity is stored in the collection
     *
     * @param entity Target entity
     * @return {@code true} if the entity is stored in the collection, {@code false} otherwise. {@see Set#contains(E)}
     */
    public boolean containsEntity(LivingEntity entity) {
        return entities.contains(entity.getUniqueId());
    }

    public boolean containsEntity(UUID uuid) {
        return entities.contains(uuid);
    }
}