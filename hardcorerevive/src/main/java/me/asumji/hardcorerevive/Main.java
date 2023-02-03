package me.asumji.hardcorerevive;

import me.asumji.hardcorerevive.commands.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import me.asumji.hardcorerevive.Files.EventListener;
import org.bukkit.command.CommandExecutor;
import me.asumji.hardcorerevive.Files.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    public DataManager data;

    public void onEnable() {
        final Main main = this;
        this.getServer().getPluginCommand("revive").setExecutor((CommandExecutor)new reviveCommand(this));
        this.getServer().getPluginCommand("reviveprice").setExecutor((CommandExecutor)new revivePriceCommand(this));
        this.getServer().getPluginCommand("revivetoggle").setExecutor((CommandExecutor)new reviveToggleCommand(this));
        this.getServer().getPluginCommand("ritualPrice").setExecutor((CommandExecutor)new ritualPriceCommand(this));
        this.getServer().getPluginManager().registerEvents((Listener)new EventListener(main), (Plugin)this);
        (this.data = new DataManager(this)).loadConfiguration();
    }

    public void onDisable() {
    }
}
