package Endxel;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class PayHeartsCommand implements CommandExecutor, TabCompleter {

    private final LSD plugin;

    public PayHeartsCommand(LSD plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        
        if (!player.hasPermission("lifesteal.payhearts")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage("§cUsage: /payhearts <player> <amount>");
            return true;
        }

        String targetName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetName);
        OfflinePlayer targetOffline = null;
        
        if (targetPlayer == null) {
            targetOffline = Bukkit.getOfflinePlayer(targetName);
            if (!targetOffline.hasPlayedBefore()) {
                player.sendMessage("§cPlayer not found: " + targetName);
                return true;
            }
        }

        try {
            int amount = Integer.parseInt(args[1]);
            if (amount <= 0) {
                player.sendMessage("§cAmount must be positive!");
                return true;
            }

            int currentHearts = plugin.getHearts(player);
            if (amount > currentHearts - plugin.getMinHearts()) {
                player.sendMessage("§cYou can't send that many hearts! You need at least " + plugin.getMinHearts() + " hearts.");
                return true;
            }

            plugin.removeHearts(player, amount);
            
            if (targetPlayer != null) {
                plugin.addHearts(targetPlayer, amount);
                targetPlayer.sendMessage("§a" + player.getName() + " sent you " + amount + " hearts! You now have " + plugin.getHearts(targetPlayer) + " hearts.");
                player.sendMessage("§aSent " + amount + " hearts to " + targetPlayer.getName() + "! You now have " + plugin.getHearts(player) + " hearts.");
            } else {
                plugin.addOfflineHearts(targetOffline, amount);
                player.sendMessage("§aSent " + amount + " hearts to " + targetOffline.getName() + " (offline)! You now have " + plugin.getHearts(player) + " hearts.");
            }
            
        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid amount: " + args[1]);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(input)) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 2) {
            completions.add("1");
            completions.add("5");
            completions.add("10");
        }
        
        return completions;
    }
}
