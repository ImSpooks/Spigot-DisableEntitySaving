package me.imspooks.des.example;

import me.imspooks.des.api.DesImpl;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Nick on 27 Oct 2021.
 * Copyright © ImSpooks
 */
public class DesExample extends JavaPlugin {

    private LivingEntity entity;

    @Override
    public void onEnable() {
        DesImpl.initialize(this);

        this.getCommand("desexample").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can execute this command");
                return true;
            }

            Player player = (Player) sender;

            if (!sender.hasPermission("des.example")) {
                sender.sendMessage(ChatColor.RED + "You are not allowed to peform this command");
            }

            EntityType type = EntityType.CREEPER;

            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("valid")) {
                    if (entity == null ){
                        sender.sendMessage(ChatColor.RED + "No entity setup");
                        return true;
                    }
                    sender.sendMessage(ChatColor.GREEN + "Entity: " + ChatColor.GRAY + entity.toString() + " (valid=" + entity.isValid() + ")");
                    return true;
                }
                try {
                    type = EntityType.valueOf(args[0].toUpperCase());
                } catch (Exception ignored) {
                    sender.sendMessage(ChatColor.RED + "Invalid entity type, check " + ChatColor.GRAY + "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html" + ChatColor.RED + " for valid entities");
                    return true;
                }
            }

            if (LivingEntity.class.isAssignableFrom(type.getEntityClass())) {
                entity = (LivingEntity) player.getWorld().spawn(player.getLocation(), type.getEntityClass());
                DesImpl.getInstance().addEntity(entity);

                sender.sendMessage(ChatColor.GREEN + "Entity " + ChatColor.BOLD + type.name() + ChatColor.GREEN + " has been spawned.");
            } else {
                sender.sendMessage(ChatColor.RED + "Entity type must be a living entity");
            }


            return true;
        });
    }
}