package me.imspooks.des.example;

import me.imspooks.des.Des;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Created by Nick on 29 Oct 2021.
 * Copyright © ImSpooks
 */
public class CmdDesExample extends DesCommand{

    public CmdDesExample() {
        super("DES: ");
    }

    private LivingEntity entity;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command");
            return true;
        }

        Player player = (Player) sender;

        if (!sender.hasPermission("des.example")) {
            sender.sendMessage(ChatColor.RED + this.prefix + "You are not allowed to peform this command");
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("status")) {
                if (entity == null ){
                    sender.sendMessage(ChatColor.RED + this.prefix + "No entity setup");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + this.prefix + "Entity: " + ChatColor.GRAY + entity.toString() + " (valid=" + entity.isValid() + ")");
                return true;
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (entity == null) {
                    sender.sendMessage(ChatColor.RED + this.prefix + "No entity setup");
                } else {
                    entity.remove();
                    sender.sendMessage(ChatColor.GREEN + this.prefix + "The example entity has been despawned.");
                }
            } else {
                try {
                    EntityType type = EntityType.valueOf(args[0].toUpperCase());

                    if (LivingEntity.class.isAssignableFrom(type.getEntityClass())) {
                        if (entity != null) {
                            player.performCommand("desexample remove");
                        }

                        entity = (LivingEntity) player.getWorld().spawn(player.getLocation(), type.getEntityClass());
                        entity.setCustomName("DES Entity");
                        entity.setCustomNameVisible(true);
                        Des.getInstance().getApi().addEntity(entity);

                        sender.sendMessage(ChatColor.GREEN + this.prefix + "Entity " + ChatColor.BOLD + type.name() + ChatColor.GREEN + " has been spawned.");
                    } else {
                        sender.sendMessage(ChatColor.RED + this.prefix + "Entity type must be a living entity");
                    }
                } catch (Exception ignored) {
                    sender.sendMessage(ChatColor.RED + this.prefix + "Invalid entity type, check " + ChatColor.GRAY + "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html" + ChatColor.RED + " for valid entities");
                    return true;
                }
            }
        }

        return true;
    }
}