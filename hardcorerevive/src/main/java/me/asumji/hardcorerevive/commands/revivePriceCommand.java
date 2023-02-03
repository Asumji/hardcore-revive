package me.asumji.hardcorerevive.commands;

import me.asumji.hardcorerevive.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class revivePriceCommand implements CommandExecutor {
    private final Main main;
    public revivePriceCommand(Main main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                Inventory inv = player.getInventory();
                ItemStack slot = ((PlayerInventory) inv).getItemInMainHand();
                this.main.getConfig().set("revive.price", slot);
                this.main.saveConfig();

                String item1 = String.valueOf(slot.getType());
                item1 = item1.replace("_", " ");
                item1 = item1.toLowerCase();

                int amount = slot.getAmount();

                player.sendMessage(ChatColor.GREEN + "Successfully set " + amount + " " + item1 + " as the price");
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You have to be an operator to execute this command.");
                return true;
            }
        } else {
            this.main.getLogger().info("You have to be a player to hold items.");
            return true;
        }
    }
}
