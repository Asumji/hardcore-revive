package me.asumji.hardcorerevive.commands;

import me.asumji.hardcorerevive.Files.DataManager;
import me.asumji.hardcorerevive.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class reviveToggleCommand implements CommandExecutor {
    private final Main main;
    public reviveToggleCommand(Main main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            String method = (String) main.getConfig().get("revive.method");
            if (method == "command") {
                main.getConfig().set("revive.method", "ritual");
                sender.sendMessage(ChatColor.GREEN + "You can now only revive people with the ritual.");
            } else {
                main.getConfig().set("revive.method", "command");
                sender.sendMessage(ChatColor.GREEN + "You can now only revive people with the command.");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You have to be an operator to execute this command.");
            return true;
        }
    }
}
