package me.asumji.hardcorerevive.Files;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageEvent;
import me.asumji.hardcorerevive.Main;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener
{
    private final Main main;

    public EventListener(final Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent e) {
        ItemStack item = this.main.getConfig().getItemStack("revive.ritual.price");
        if (e.getEntity().getType().equals(EntityType.DROPPED_ITEM)) {
            Item droppedItem = (Item) e.getEntity();
            if (this.main.getConfig().get("revive.method") == "ritual" && droppedItem.getItemStack().getType().toString().equalsIgnoreCase(item.getType().toString())) {
                e.getEntity().remove();
                if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    for (final Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getGameMode() == GameMode.SPECTATOR) {
                            if (((String) this.main.getConfig().get("revive.spawn")).equalsIgnoreCase("spawn")) {
                                final String world = p.getLocation().getWorld().getName();
                                final Location location = this.main.getServer().getWorld(world).getSpawnLocation();
                                p.teleport(location);
                            } else if (((String) this.main.getConfig().get("revive.spawn")).equalsIgnoreCase("death")) {
                                final ConfigurationSection section = this.main.getConfig().getConfigurationSection(p.getUniqueId().toString());
                                if (section.getString("cause").equalsIgnoreCase("nonReviviable")) {
                                    final String world2 = p.getLocation().getWorld().getName();
                                    final Location location2 = this.main.getServer().getWorld(world2).getSpawnLocation();
                                    p.teleport(location2);
                                } else {
                                    p.teleport(new Location(p.getLocation().getWorld(), section.getDouble("location.x"), section.getDouble("location.y"), section.getDouble("location.z")));
                                }
                            }
                            p.setGameMode(GameMode.SURVIVAL);
                            p.sendMessage(ChatColor.GREEN + "You've been revived!");
                            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + p.getName() + " has been revived through a ritual!");
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        final Player p = e.getEntity();
        if (!this.main.getConfig().contains(p.getUniqueId().toString())) {
            this.main.getConfig().createSection(p.getUniqueId().toString());
        }
        final ConfigurationSection section = this.main.getConfig().getConfigurationSection(p.getUniqueId().toString());
        section.set("location.x", (Object)p.getLocation().getBlockX());
        section.set("location.y", (Object)p.getLocation().getBlockY());
        section.set("location.z", (Object)p.getLocation().getBlockZ());
        final EntityDamageEvent.DamageCause pCause = p.getLastDamageCause().getCause();
        final EntityDamageEvent.DamageCause lavaCause = EntityDamageEvent.DamageCause.LAVA;
        final EntityDamageEvent.DamageCause voidCause = EntityDamageEvent.DamageCause.VOID;
        final EntityDamageEvent.DamageCause suffocCause = EntityDamageEvent.DamageCause.SUFFOCATION;
        if (pCause.equals((Object)lavaCause) || pCause.equals((Object)voidCause) || pCause.equals((Object)suffocCause)) {
            section.set("cause", (Object)"nonReviviable");
        }
        else {
            section.set("cause", (Object)"reviviable");
        }
        this.main.saveConfig();
    }
}