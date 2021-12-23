package me.asumji.hardcorerevive.commands;

import me.asumji.hardcorerevive.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class reviveCommand implements CommandExecutor {
    private final  Main main;

    public reviveCommand(Main main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        int amount = this.main.getConfig().getItemStack("revive.price").getAmount();
        ItemStack item = this.main.getConfig().getItemStack("revive.price");

        if (sender instanceof Player) {
            if (((String) main.getConfig().get("revive.method")).equalsIgnoreCase("command")) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    if (player.getGameMode() == GameMode.SPECTATOR) {
                        player.sendMessage(ChatColor.RED + "You can't revive people when you're dead.");
                        return true;
                    } else {
                        Player target = Bukkit.getPlayerExact(args[0]);
                        if (target instanceof Player) {
                            if (target != player) {
                                if (target.getGameMode() == GameMode.SPECTATOR) {
                                    if (player.getInventory().containsAtLeast(item, amount)) {
                                        target.setGameMode(GameMode.SURVIVAL);

                                        if (((String) main.getConfig().get("revive.spawn")).equalsIgnoreCase("spawn")) {
                                            String world = player.getLocation().getWorld().getName();

                                            Location location = this.main.getServer().getWorld(world).getSpawnLocation();
                                            target.teleport(location);
                                        } else if (((String) main.getConfig().get("revive.spawn")).equalsIgnoreCase("death")) {
                                            ConfigurationSection section = main.getConfig().getConfigurationSection(target.getUniqueId().toString());
                                            if (section.getString("cause").equalsIgnoreCase("nonReviviable")) {
                                                main.getLogger().info("died in lava/void");
                                                String world = player.getLocation().getWorld().getName();

                                                Location location = this.main.getServer().getWorld(world).getSpawnLocation();
                                                target.teleport(location);
                                            } else {
                                                target.teleport(new Location(player.getLocation().getWorld(), section.getDouble("location.x"), section.getDouble("location.y"), section.getDouble("location.z")));
                                            }
                                        }

                                        player.getInventory().removeItem(new ItemStack(item.getType(),amount));
                                        player.sendMessage(ChatColor.GREEN + "You've revived " + args[0]);
                                        target.sendMessage(ChatColor.GREEN + "You've been revived by " + player.getDisplayName());
                                        return true;
                                    } else {
                                        String item1 = String.valueOf(item.getType());
                                        item1 = item1.replace("_", " ");
                                        item1 = item1.toLowerCase();
                                        player.sendMessage(ChatColor.RED + "You need at least " + amount + " " + item1 + " to revive someone!\nMake sure the item isn't in any extra slot (Off-hand or similar) but in the inventory.");
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "This player isn't dead.");
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You can't revive yourself.");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Couldn't find the player " + args[0]);
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "/revive player");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Only the ritual is currently available.");
                return true;
            }
        } else {
            main.getLogger().info("You have to be a Player to revive someone.");
            return true;
        }
    }
}
