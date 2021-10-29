package me.imspooks.des.api;

/**
 * Housekeeper of the Des entity collection
 */
public interface DesHousekeeper {

    /**
     * Runs the entity cleanup, checks if entity is valid or not
     */
    void runEntityCleanup();

    /**
     * @return Last time house keeping ran in millis
     */
    long getLastCleanup();

    /**
     * @return Housekeeper delay, default value of 600 seconds (10 minutes)
     */
    long getDelay();

    /**
     * @param delay Delay in seconds between each cleanup
     *              Note: Runnable ticks every 10 seconds.
     */
    void setDelay(long delay);
}