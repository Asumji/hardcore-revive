package me.asumji.hardcorerevive;

import me.asumji.hardcorerevive.Files.DataManager;
import me.asumji.hardcorerevive.Files.EventListener;
import me.asumji.hardcorerevive.commands.reviveCommand;
import me.asumji.hardcorerevive.commands.revivePriceCommand;
import me.asumji.hardcorerevive.commands.reviveToggleCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public DataManager data;

    @Override
    public void onEnable() {
        // Plugin startup logic

        Main main = this;

        getServer().getPluginCommand("revive").setExecutor(new reviveCommand(this));
        getServer().getPluginCommand("reviveprice").setExecutor(new revivePriceCommand(this));
        getServer().getPluginCommand("revivetoggle").setExecutor(new reviveToggleCommand(this));
        getServer().getPluginManager().registerEvents(new EventListener(main), this);
        this.data = new DataManager(this);
        this.data.loadConfiguration();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
