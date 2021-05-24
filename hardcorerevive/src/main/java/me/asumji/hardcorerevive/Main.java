package me.asumji.hardcorerevive;

import me.asumji.hardcorerevive.Files.DataManager;
import me.asumji.hardcorerevive.commands.reviveCommand;
import me.asumji.hardcorerevive.commands.revivePriceCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public DataManager data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginCommand("revive").setExecutor(new reviveCommand(this));
        getServer().getPluginCommand("reviveprice").setExecutor(new revivePriceCommand(this));
        this.data = new DataManager(this);
        this.data.loadConfiguration();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
