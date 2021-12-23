package me.asumji.hardcorerevive.Files;
import me.asumji.hardcorerevive.Main;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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

                            if (((String) main.getConfig().get("revive.spawn")).equalsIgnoreCase("spawn")) {
                                String world = p.getLocation().getWorld().getName();

                                Location location = this.main.getServer().getWorld(world).getSpawnLocation();
                                p.teleport(location);
                            } else if (((String) main.getConfig().get("revive.spawn")).equalsIgnoreCase("death")) {
                                ConfigurationSection section = main.getConfig().getConfigurationSection(p.getUniqueId().toString());
                                if (section.getString("cause").equalsIgnoreCase("nonReviviable")) {
                                    String world = p.getLocation().getWorld().getName();

                                    Location location = this.main.getServer().getWorld(world).getSpawnLocation();
                                    p.teleport(location);
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
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!main.getConfig().contains(p.getUniqueId().toString())) {
            main.getConfig().createSection(p.getUniqueId().toString());
        }
        ConfigurationSection section = main.getConfig().getConfigurationSection(p.getUniqueId().toString());
        section.set("location.x", Integer.valueOf(p.getLocation().getBlockX()));
        section.set("location.y", Integer.valueOf(p.getLocation().getBlockY()));
        section.set("location.z", Integer.valueOf(p.getLocation().getBlockZ()));
        EntityDamageEvent.DamageCause pCause = p.getLastDamageCause().getCause();
        EntityDamageEvent.DamageCause lavaCause = EntityDamageEvent.DamageCause.LAVA;
        EntityDamageEvent.DamageCause voidCause = EntityDamageEvent.DamageCause.VOID;
        EntityDamageEvent.DamageCause suffocCause = EntityDamageEvent.DamageCause.SUFFOCATION;
        if (pCause.equals(lavaCause) || pCause.equals(voidCause) || pCause.equals(suffocCause)) {
            section.set("cause", "nonReviviable");
        } else {
            section.set("cause", "reviviable");
        }
        main.saveConfig();
    }
}
