package Endxel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DepositCommand implements CommandExecutor, TabCompleter {

    private final LSD plugin;

    public DepositCommand(LSD plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        
        if (!player.hasPermission("lifesteal.deposit")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /deposit <amount>");
            return true;
        }

        try {
            int amount = Integer.parseInt(args[0]);
            if (amount <= 0) {
                player.sendMessage("§cAmount must be positive!");
                return true;
            }

            int currentHearts = plugin.getHearts(player);
            if (currentHearts + amount > plugin.getMaxHearts()) {
                player.sendMessage("§cYou can't deposit that many hearts! You would exceed the maximum of " + plugin.getMaxHearts() + " hearts.");
                return true;
            }


            
            plugin.addHearts(player, amount);
            player.sendMessage("§aDeposited " + amount + " hearts! You now have " + plugin.getHearts(player) + " hearts.");
            
        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid amount: " + args[0]);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.add("1");
            completions.add("5");
            completions.add("10");
        }
        
        return completions;
    }
}
