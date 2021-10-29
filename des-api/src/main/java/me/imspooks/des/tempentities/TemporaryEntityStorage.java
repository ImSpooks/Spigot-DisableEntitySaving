package me.imspooks.des.tempentities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public class TemporaryEntityStorage {

    private Set<TemporaryEntity<?>> entities = new HashSet<>();

    public boolean addEntity(TemporaryEntity<?> entity) {
        return this.entities.add(entity);
    }

    public boolean removeEntity(TemporaryEntity<?> entity) {
        return entities.remove(entity);
    }

    public Collection<TemporaryEntity<?>> getEntities() {
        return Collections.unmodifiableSet(entities);
    }

    public Collection<TemporaryEntity<?>> getModifiableEntities() {
        return entities;
    }
}