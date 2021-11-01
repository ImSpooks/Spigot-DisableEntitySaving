package me.imspooks.des.example;

import me.imspooks.des.Des;
import me.imspooks.des.tempentities.TemporaryEntity;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Nick on 27 Oct 2021.
 * Copyright © ImSpooks
 */
public class DesExample extends JavaPlugin {

    @Override
    public void onEnable() {
        Des.initialize(this);

        this.getCommand("teexample").setExecutor(new CmdTeExample());
        this.getCommand("desexample").setExecutor(new CmdDesExample());
    }
}