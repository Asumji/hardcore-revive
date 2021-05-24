package me.asumji.hardcorerevive.Files;

import me.asumji.hardcorerevive.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class DataManager {

    private Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(Main plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
    }

    public void loadConfiguration() {
        String path = "revive.price";
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        plugin.getConfig().addDefault(path, item);
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }
}
