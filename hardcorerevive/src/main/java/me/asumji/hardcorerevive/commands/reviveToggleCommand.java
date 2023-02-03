package me.asumji.hardcorerevive.commands;
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
            String method = (String) this.main.getConfig().get("revive.method");
            String spawn = (String) this.main.getConfig().get("revive.spawn");
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("method")) {
                    if (method == "command") {
                        this.main.getConfig().set("revive.method", "ritual");
                        this.main.saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "You can now only revive people with the ritual.");
                    } else {
                        this.main.getConfig().set("revive.method", "command");
                        this.main.saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "You can now only revive people with the command.");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("spawn")) {
                    if (spawn == "spawn") {
                        this.main.getConfig().set("revive.spawn", "death");
                        this.main.saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Revived people will now spawn at their death spot.");
                    } else {
                        this.main.getConfig().set("revive.spawn", "spawn");
                        this.main.saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Revived people will now spawn at world spawn.");
                    }
                    return true;
                }
                return true;
            }
            sender.sendMessage(ChatColor.RED + "/revivetoggle method/spawn");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You have to be an operator to execute this command.");
            return true;
        }
    }
}
