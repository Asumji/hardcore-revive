package me.asumji.hardcorerevive.Files;
import me.asumji.hardcorerevive.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class EventListener implements Listener {

    private final Main main;
    public DataManager data;

    public EventListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (main.getConfig().get("revive.method") == "ritual") {
            if (e.getEntity().getName().equalsIgnoreCase("totem of undying")) {
                e.getEntity().remove();
                if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getGameMode() == GameMode.SPECTATOR) {

                            List<World> worlds = this.main.getServer().getWorlds();

                            String world = worlds.get(0).getName();

                            Location location = this.main.getServer().getWorld(world).getSpawnLocation();
                            p.teleport(location);

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
}
