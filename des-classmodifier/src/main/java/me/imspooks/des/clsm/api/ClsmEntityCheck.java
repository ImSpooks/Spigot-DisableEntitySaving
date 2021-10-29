package me.imspooks.des.clsm.api;

import lombok.Getter;

import java.util.UUID;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public class ClsmEntityCheck {

    /**
     * The instance of the Entity Checker for the class modifier.
     */
    @Getter private static ClsmEntityCheck instance = new ClsmEntityCheck();

    /**
     * Set the instace of the Entity Checker for the class modifier.
     *
     * @param instance New instance, may only be called once
     */
    public static void setInstance(ClsmEntityCheck instance) {
        ClsmEntityCheck.instance = instance;
    }

    /**
     * Check if an entity is temporary and should not be saved.
     * This method is overridden in the api.
     *
     * @param uuid The entity's uuid
     * @return {@code true} if the entity is temporary, {@code false} otherwise
     */
    public boolean isTemporaryEntity(UUID uuid) {
        return false;
    }


}