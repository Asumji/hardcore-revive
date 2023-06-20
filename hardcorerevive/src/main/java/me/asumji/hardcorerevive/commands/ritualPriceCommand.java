package me.asumji.hardcorerevive.commands;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.asumji.hardcorerevive.Main;
import org.bukkit.command.CommandExecutor;

public class ritualPriceCommand implements CommandExecutor
{
    private final Main main;

    public ritualPriceCommand(final Main main) {
        this.main = main;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            this.main.getLogger().info("You have to be a player to hold items.");
            return true;
        }
        final Player player = (Player)sender;
        final PlayerInventory inv = player.getInventory();
        final ItemStack slot = inv.getItemInMainHand();
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You have to be an operator to execute this command.");
            return true;
        }
        if (!slot.getType().equals(Material.AIR)) {
            String item = slot.getItemMeta().getDisplayName();
            if (item.equals("") || this.main.getConfig().getBoolean("revive.ritual.ritualname")) item = String.valueOf(slot.getType()).replace("_", " ").toLowerCase();
            ItemStack slot2 = new ItemStack(slot.getType(), 1);
            slot2.setItemMeta(slot.getItemMeta());
            this.main.getConfig().set("revive.ritual.price", slot2);
            this.main.saveConfig();
            player.sendMessage(ChatColor.GREEN + "Successfully set " + item + " as the price");
            return true;
        }
        player.sendMessage(ChatColor.RED + "You can't set air as the ritual price.");
        return true;
    }
}