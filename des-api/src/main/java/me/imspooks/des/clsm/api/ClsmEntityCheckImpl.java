package me.imspooks.des.clsm.api;

import me.imspooks.des.Des;

import java.util.UUID;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public class ClsmEntityCheckImpl extends ClsmEntityCheck {

    @Override
    public boolean isTemporaryEntity(UUID uuid) {
        return Des.getInstance().getApi().containsEntity(uuid);
    }
}