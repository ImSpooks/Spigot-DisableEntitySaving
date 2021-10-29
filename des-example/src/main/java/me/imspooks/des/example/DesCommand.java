package me.imspooks.des.example;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandExecutor;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public abstract class DesCommand implements CommandExecutor {

    protected final String prefix;
}