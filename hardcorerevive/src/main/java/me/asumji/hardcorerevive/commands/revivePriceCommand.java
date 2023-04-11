package me.asumji.hardcorerevive.commands;

import me.asumji.hardcorerevive.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class revivePriceCommand implements CommandExecutor {
    private final Main main;
    public revivePriceCommand(Main main) { this.main = main; }

    public String getItemName(ItemStack item) {
        if (!item.getType().equals((Object) Material.AIR)) {
            if (item.getItemMeta().getDisplayName() != "") {
                return item.getItemMeta().getDisplayName();
            } else {
                String item1 = item.getType().toString();
                item1 = item1.replace("_", " ");
                item1 = item1.toLowerCase();
                return item1;
            }
        } else {
            return "air";
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                Inventory inv = player.getInventory();
                ItemStack slot = ((PlayerInventory) inv).getItemInMainHand();
                this.main.getConfig().set("revive.price", slot);
                this.main.saveConfig();
                int amount = slot.getAmount();

                player.sendMessage(ChatColor.GREEN + "Successfully set " + amount + " " + getItemName(slot) + "§a as the price");
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
