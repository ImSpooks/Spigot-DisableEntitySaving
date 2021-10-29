package me.imspooks.des;

import lombok.Getter;
import me.imspooks.des.api.DesApi;
import me.imspooks.des.api.impl.DesApiImpl;
import me.imspooks.des.api.impl.DesHousekeeperImpl;
import me.imspooks.des.clsm.api.ClsmEntityCheck;
import me.imspooks.des.clsm.api.ClsmEntityCheckImpl;
import me.imspooks.des.tempentities.TemporaryEntityApi;
import me.imspooks.des.tempentities.impl.TemporaryEntityApiImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public class Des {

    @Getter private static Des instance;

    private JavaPlugin plugin;

    private DesApi api;
    private TemporaryEntityApi temporaryEntityApi;

    /**
     * Initializes the API
     *
     * @param plugin Core responding plugin
     */
    public static void initialize(JavaPlugin plugin) {
        instance = new Des(plugin);
    }

    private Des(JavaPlugin plugin) {
        this.plugin = plugin;

        // Set up entity checker for the class modifier
        ClsmEntityCheck.setInstance(new ClsmEntityCheckImpl());

        // Set up api
        DesApiImpl desApi = (DesApiImpl) (this.api = new DesApiImpl());
        DesHousekeeperImpl housekeeper = new DesHousekeeperImpl(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, housekeeper, 10 * 20, 10 * 20);
        desApi.setHousekeeper(housekeeper);

        // Set up temporary entities
        this.temporaryEntityApi = new TemporaryEntityApiImpl(this.plugin);
    }

    public DesApi getApi() {
        return api;
    }

    public TemporaryEntityApi getTemporaryEntityApi() {
        return temporaryEntityApi;
    }
}