package Endxel;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HeartsCommand implements CommandExecutor, TabCompleter {

    private final LSD plugin;

    public HeartsCommand(LSD plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command can only be used by players!");
                return true;
            }
            
            Player player = (Player) sender;
            int hearts = plugin.getHearts(player);
            player.sendMessage("§6§l=== Your Hearts ===");
            player.sendMessage("§eCurrent Hearts: §7" + hearts);
            player.sendMessage("§eMax Hearts: §7" + plugin.getMaxHearts());
            player.sendMessage("§eMin Hearts: §7" + plugin.getMinHearts());
            
            StringBuilder heartBar = new StringBuilder("§c");
            for (int i = 0; i < hearts; i++) {
                heartBar.append("❤");
            }
            player.sendMessage(heartBar.toString());
            
        } else if (args.length == 1) {
            if (!sender.hasPermission("lifesteal.hearts.others")) {
                sender.sendMessage("§cYou don't have permission to check other players' hearts!");
                return true;
            }
            
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found: " + args[0]);
                return true;
            }
            
            int hearts = plugin.getHearts(target);
            sender.sendMessage("§6§l=== " + target.getName() + "'s Hearts ===");
            sender.sendMessage("§eCurrent Hearts: §7" + hearts);
            sender.sendMessage("§eMax Hearts: §7" + plugin.getMaxHearts());
            sender.sendMessage("§eMin Hearts: §7" + plugin.getMinHearts());
            
            StringBuilder heartBar = new StringBuilder("§c");
            for (int i = 0; i < hearts; i++) {
                heartBar.append("❤");
            }
            sender.sendMessage(heartBar.toString());
            
        } else {
            sender.sendMessage("§cUsage: /hearts [player]");
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1 && sender.hasPermission("lifesteal.hearts.others")) {
            String input = args[0].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(input)) {
                    completions.add(player.getName());
                }
            }
        }
        
        return completions;
    }
}
