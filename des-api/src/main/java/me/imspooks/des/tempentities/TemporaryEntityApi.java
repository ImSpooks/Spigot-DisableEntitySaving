package me.imspooks.des.tempentities;


import java.util.Collection;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public interface TemporaryEntityApi {

    /**
     * @return The listener for this api
     */
    TemporaryEntityListener getListener();

    /**
     * @see TemporaryEntityStorage#getEntities()
     */
    Collection<TemporaryEntity<?>> getEntities();
}