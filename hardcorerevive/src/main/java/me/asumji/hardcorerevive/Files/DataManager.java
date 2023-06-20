package me.asumji.hardcorerevive.Files;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import me.asumji.hardcorerevive.Main;

public class DataManager
{
    private Main plugin;
    private FileConfiguration dataConfig;
    private File configFile;

    public DataManager(final Main plugin) {
        this.dataConfig = null;
        this.configFile = null;
        (this.plugin = plugin).saveDefaultConfig();
    }

    public void loadConfiguration() {
        final String path = "revive.price";
        final String path2 = "revive.method";
        final String path3 = "revive.spawn";
        final String path4 = "revive.ritual.price";
        final String ritualname = "revive.ritual.ritualname";
        final String spawn = "death";
        final String method = "command";
        final ItemStack price = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        final ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        this.plugin.getConfig().addDefault(path3, (Object)spawn);
        this.plugin.getConfig().addDefault(path2, (Object)method);
        this.plugin.getConfig().addDefault(path, (Object)item);
        this.plugin.getConfig().addDefault(path4, (Object)price);
        this.plugin.getConfig().addDefault(ritualname, (Object)false);

        this.plugin.getConfig().options().copyDefaults(true);
        this.plugin.saveConfig();
    }
}