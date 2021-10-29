package me.imspooks.des.tempentities.impl;

import lombok.Getter;
import me.imspooks.des.tempentities.TemporaryEntity;
import me.imspooks.des.tempentities.TemporaryEntityApi;
import me.imspooks.des.tempentities.TemporaryEntityListener;
import me.imspooks.des.tempentities.TemporaryEntityStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
@Getter
public class TemporaryEntityApiImpl implements TemporaryEntityApi {

    private final TemporaryEntityStorage storage;
    private final TemporaryEntityListener listener;

    public TemporaryEntityApiImpl(JavaPlugin plugin) {
        this.storage = new TemporaryEntityStorage();
        plugin.getServer().getPluginManager().registerEvents(this.listener = new TemporaryEntityListener(), plugin);
    }

    @Override
    public Collection<TemporaryEntity<?>> getEntities() {
        return storage.getEntities();
    }
}