package me.asumji.hardcorerevive.commands;

import me.asumji.hardcorerevive.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class reviveCommand implements CommandExecutor {
    private final  Main main;

    public reviveCommand(Main main) {
        this.main = main;
    }

    public static int removeItems(Inventory inventory, Material type, int amount) {

        if(type == null || inventory == null)
            return -1;
        if (amount <= 0)
            return -1;

        if (amount == Integer.MAX_VALUE) {
            inventory.remove(type);
            return 0;
        }

        HashMap<Integer, ItemStack> retVal = inventory.removeItem(new ItemStack(type,amount));

        int notRemoved = 0;
        for(ItemStack item: retVal.values()) {
            notRemoved+=item.getAmount();
        }
        return notRemoved;
    }

    public static boolean containsAtLeast(Player p, Material m, Integer amount) {
        AtomicReference<Integer> has = new AtomicReference<>(0);

        Arrays.stream(p.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() != null && itemStack.getType().equals(m))
                has.updateAndGet(v -> v + itemStack.getAmount());
        });

        if (has.get() >= amount)
            return true;

        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        int amount = this.main.getConfig().getItemStack("revive.price").getAmount();
        main.getLogger().info(String.valueOf(amount));
        Material item = this.main.getConfig().getItemStack("revive.price").getType();

        if (sender instanceof Player) {
            if (main.getConfig().get("revive.method") == "command") {
                Player player = (Player) sender;
                if (args.length > 0) {
                    if (player.getGameMode() == GameMode.SPECTATOR) {
                        player.sendMessage(ChatColor.RED + "You can't revive people when you're dead.");
                        return true;
                    } else {
                        Player target = Bukkit.getPlayerExact(args[0]);
                        Inventory inv = player.getInventory();
                        if (target instanceof Player) {
                            if (target != player) {
                                if (target.getGameMode() == GameMode.SPECTATOR) {
                                    if (containsAtLeast(player, item, amount)) {
                                        target.setGameMode(GameMode.SURVIVAL);
                                        List<World> worlds = this.main.getServer().getWorlds();

                                        String world = worlds.get(0).getName();

                                        Location location = this.main.getServer().getWorld(world).getSpawnLocation();
                                        target.teleport(location);

                                        removeItems(inv, item, amount);
                                        player.sendMessage(ChatColor.GREEN + "You've revived " + args[0]);
                                        target.sendMessage(ChatColor.GREEN + "You've been revived by " + player.getDisplayName());
                                        return true;
                                    } else {
                                        String item1 = String.valueOf(item);
                                        item1 = item1.replace("_", " ");
                                        item1 = item1.toLowerCase();
                                        player.sendMessage(ChatColor.RED + "You need at least " + amount + " " + item1 + " to revive someone!");
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "This player isn't dead.");
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You can't revive yourself.");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Couldn't find the player " + args[0]);
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "/revive player");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Only the ritual is currently available.");
                return true;
            }
        } else {
            main.getLogger().info("You have to be a Player to revive someone.");
            return true;
        }
    }
}
