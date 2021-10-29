package me.imspooks.des.api.impl;

import lombok.Getter;
import lombok.Setter;
import me.imspooks.des.api.DesApi;
import me.imspooks.des.api.DesHousekeeper;

import java.util.*;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
@Getter
@Setter
public class DesApiImpl implements DesApi {

    // Synchronized set, because house keeper is async
    private final Set<UUID> entities = Collections.synchronizedSet(new HashSet<>());
    private DesHousekeeper housekeeper;

    @Override
    public Collection<UUID> getEntities() {
        return Collections.unmodifiableSet(this.entities);
    }

    public Collection<UUID> getModifiableEntities() {
        return this.entities;
    }

    @Override
    public boolean addEntity(UUID uuid) {
        return this.entities.add(uuid);
    }

    @Override
    public boolean removeEntity(UUID uuid) {
        return this.entities.remove(uuid);
    }
}