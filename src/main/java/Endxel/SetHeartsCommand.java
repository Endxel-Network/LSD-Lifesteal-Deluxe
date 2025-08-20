package Endxel;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetHeartsCommand implements CommandExecutor, TabCompleter {

    private final LSD plugin;

    public SetHeartsCommand(LSD plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lifesteal.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cUsage: /sethearts <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + args[0]);
            return true;
        }

        try {
            int amount = Integer.parseInt(args[1]);
            if (amount < plugin.getMinHearts() || amount > plugin.getMaxHearts()) {
                sender.sendMessage("§cAmount must be between " + plugin.getMinHearts() + " and " + plugin.getMaxHearts());
                return true;
            }

            plugin.setHearts(target, amount);
            sender.sendMessage("§aSet " + target.getName() + "'s hearts to " + amount);
            target.sendMessage("§aYour hearts have been set to " + amount + " by " + sender.getName());
            
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount: " + args[1]);
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
            completions.add("20");
            completions.add("40");
            completions.add("10");
        }
        
        return completions;
    }
}
