package Endxel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LifestealCommand implements CommandExecutor, TabCompleter {

    private final LSD plugin;

    public LifestealCommand(LSD plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        // Allow enable/disable commands to work even when plugin is disabled
        String subcommand = args[0].toLowerCase();
        if (!subcommand.equals("enable") && !subcommand.equals("disable") && !plugin.isPluginEnabled()) {
            sender.sendMessage("§cLifesteal Deluxe is currently disabled!");
            return true;
        }

        switch (subcommand) {
            case "reload":
                if (!sender.hasPermission("lifesteal.reload")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                plugin.reloadPlugin();
                sender.sendMessage("§aConfiguration reloaded!");
                break;

            case "info":
                if (!sender.hasPermission("lifesteal.info")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                sendInfoMessage(sender);
                break;

            case "help":
                sendHelpMessage(sender);
                break;

            case "addhearts":
                if (!sender.hasPermission("lifesteal.addhearts")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleAddHearts(sender, args);
                break;

            case "removehearts":
                if (!sender.hasPermission("lifesteal.removehearts")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleRemoveHearts(sender, args);
                break;

            case "sethearts":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleSetHearts(sender, args);
                break;

            // Removed setlimit command - use setmaxhearts instead

            case "setmaxhearts":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleSetMaxHearts(sender, args);
                break;

            case "setmax":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleSetMaxHearts(sender, args);
                break;

            case "reset":
                if (!sender.hasPermission("lifesteal.reset")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleReset(sender, args);
                break;

            case "name":
                if (!sender.hasPermission("lifesteal.setheartname")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleSetHeartName(sender, args);
                break;

            case "lore":
                if (!sender.hasPermission("lifesteal.setheartlore")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleSetHeartLore(sender, args);
                break;

            case "hearts":
                if (!sender.hasPermission("lifesteal.gethearts")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleGetHearts(sender, args);
                break;

            case "top":
                if (!sender.hasPermission("lifesteal.top")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleTop(sender);
                break;

            case "history":
                if (!sender.hasPermission("lifesteal.history")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleHistory(sender, args);
                break;

            case "debug":
                if (!sender.hasPermission("lifesteal.reload")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleDebug(sender);
                break;

            case "givehearts":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleGiveHearts(sender, args);
                break;

            case "banstatus":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleBanStatus(sender, args);
                break;

            case "unban":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleUnban(sender, args);
                break;

            case "revive":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleRevive(sender, args);
                break;

            case "aliases":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleAliases(sender, args);
                break;

            case "language":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                handleLanguage(sender, args);
                break;

            case "enable":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                if (plugin.isPluginEnabled()) {
                    sender.sendMessage("§cLifesteal Deluxe is already enabled!");
                    return true;
                }
                plugin.setPluginEnabled(true);
                sender.sendMessage("§aLifesteal Deluxe has been ENABLED!");
                break;

            case "disable":
                if (!sender.hasPermission("lifesteal.admin")) {
                    sender.sendMessage("§cYou don't have permission to use this command!");
                    return true;
                }
                if (!plugin.isPluginEnabled()) {
                    sender.sendMessage("§cLifesteal Deluxe is already disabled!");
                    return true;
                }
                plugin.setPluginEnabled(false);
                sender.sendMessage("§cLifesteal Deluxe has been DISABLED!");
                break;


            default:
                if (args.length >= 2 && sender.hasPermission("LSD.addhearts")) {
                    try {
                        int amount = Integer.parseInt(args[1]);
                        handleAddHearts(sender, new String[]{args[0], args[1]});
                        return true;
                    } catch (NumberFormatException e) {
                    }
                }
                sender.sendMessage("§cUnknown subcommand. Use /lifesteal help for more information.");
                break;
        }

        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§6§l=== Lifesteal Deluxe Help ===");
        sender.sendMessage("§e/lifesteal help §7- Show this help message");
        sender.sendMessage("§e/lifesteal info §7- Show plugin information");
        sender.sendMessage("§e/lifesteal hearts [player] §7- Check hearts");
        sender.sendMessage("§e/withdraw <amount> §7- Withdraw hearts into items");
        sender.sendMessage("§e/payhearts <player> <amount> §7- Send hearts to player");
        
        if (sender.hasPermission("lifesteal.addhearts")) {
            sender.sendMessage("§e/lifesteal <player> <amount> §7- Add hearts to player");
            sender.sendMessage("§e/lifesteal removehearts <player> <amount> §7- Remove hearts from player");
            sender.sendMessage("§e/lifesteal sethearts <player> <amount> §7- Set player hearts");
            sender.sendMessage("§e/lifesteal setlimit <player> <amount> §7- Set player heart limit");
            sender.sendMessage("§e/lifesteal reset <player> §7- Reset player hearts");
        }

        if (sender.hasPermission("lifesteal.admin")) {
            sender.sendMessage("§e/lifesteal setmaxhearts <amount> §7- Set global max hearts limit");
            sender.sendMessage("§e/lifesteal setmax <amount> §7- Set global max hearts limit (alias)");
            sender.sendMessage("§e/lifesteal givehearts <player> <amount> §7- Give hearts to offline player");
            sender.sendMessage("§e/lifesteal revive <player> §7- Revive banned player");
            sender.sendMessage("§e/lifesteal aliases §7- Manage command aliases");
            sender.sendMessage("§e/lifesteal language §7- Manage language settings");
        }
        
        if (sender.hasPermission("lifesteal.setheartname")) {
            sender.sendMessage("§e/lifesteal name <name> §7- Set custom heart item name");
        }
        
        if (sender.hasPermission("lifesteal.setheartlore")) {
            sender.sendMessage("§e/lifesteal lore <lore> §7- Set custom heart item lore");
        }
        
        if (sender.hasPermission("lifesteal.history")) {
            sender.sendMessage("§e/lifesteal history [player] §7- View heart transaction history");
        }
        
        if (sender.hasPermission("lifesteal.reload")) {
            sender.sendMessage("§e/lifesteal reload §7- Reload plugin configuration");
        }
        
        if (sender.hasPermission("lifesteal.reload")) {
            sender.sendMessage("§e/lifesteal debug §7- Show debug information");
        }

        if (sender.hasPermission("lifesteal.admin")) {
            sender.sendMessage("§e/lifesteal enable §7- Enable the plugin");
            sender.sendMessage("§e/lifesteal disable §7- Disable the plugin");
            sender.sendMessage("§e/lifesteal revive <player> §7- Revive a banned player");
        }
        
        sender.sendMessage("§e§lNote: §7Use * for all online players, ** for all players ever");
    }

    private void sendInfoMessage(CommandSender sender) {
        sender.sendMessage("§6§l=== Lifesteal Deluxe Info ===");
        sender.sendMessage("§eVersion: §7" + plugin.getDescription().getVersion());
        sender.sendMessage("§eMax Hearts: §7" + plugin.getMaxHearts());
        sender.sendMessage("§eMin Hearts: §7" + plugin.getMinHearts());
        sender.sendMessage("§ePlugin Status: §7" + (plugin.isPluginEnabled() ? "§aENABLED" : "§cDISABLED"));
        sender.sendMessage("§eLose Heart on Death: §7" + (plugin.isLoseHeartOnDeath() ? "Yes" : "No"));
        sender.sendMessage("§eGain Heart on Kill: §7" + (plugin.isGainHeartOnKill() ? "Yes" : "No"));
        sender.sendMessage("§eDrop Heart on Death: §7" + (plugin.isDropHeartOnDeath() ? "Yes" : "No"));
        if (plugin.isDropHeartOnDeath()) {
            sender.sendMessage("§eHeart Drop Chance: §7" + (plugin.getHeartDropChance() * 100) + "%");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>(Arrays.asList("help", "info", "hearts"));
            
            if (sender.hasPermission("lifesteal.addhearts")) {
                subcommands.addAll(Arrays.asList("addhearts", "removehearts", "setlimit", "reset"));
            }

            if (sender.hasPermission("lifesteal.admin")) {
                subcommands.add("setmaxhearts");
                subcommands.add("setmax");
                subcommands.add("revive");
                subcommands.add("givehearts");
                subcommands.add("aliases");
                subcommands.add("language");
            }
            
            if (sender.hasPermission("lifesteal.setheartname")) {
                subcommands.add("name");
            }
            
            if (sender.hasPermission("lifesteal.setheartlore")) {
                subcommands.add("lore");
            }
            
            if (sender.hasPermission("lifesteal.history")) {
                subcommands.add("history");
            }
            
            if (sender.hasPermission("lifesteal.reload")) {
                subcommands.add("reload");
                subcommands.add("debug");
            }

            if (sender.hasPermission("lifesteal.admin")) {
                subcommands.add("enable");
                subcommands.add("disable");
            }
            
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        } else if (args.length == 2) {
            String subcommand = args[0].toLowerCase();
            
            if (subcommand.equals("addhearts") || subcommand.equals("removehearts") || 
                subcommand.equals("sethearts") || subcommand.equals("setlimit") || 
                subcommand.equals("reset") || subcommand.equals("hearts") || 
                subcommand.equals("history") || subcommand.equals("givehearts") || 
                subcommand.equals("revive")) {
                
                completions.add("*");
                completions.add("**");
                
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            } else if (subcommand.equals("aliases")) {
                completions.addAll(Arrays.asList("list", "add", "remove", "reload"));
            } else if (subcommand.equals("language")) {
                completions.addAll(Arrays.asList("list", "set", "current"));
            }
        } else if (args.length == 3) {
            String subcommand = args[0].toLowerCase();
            
            if (subcommand.equals("addhearts") || subcommand.equals("removehearts") || 
                subcommand.equals("sethearts") || subcommand.equals("setlimit") || 
                subcommand.equals("setmaxhearts") || subcommand.equals("setmax") ||
                subcommand.equals("givehearts")) {
                
                completions.add("1");
                completions.add("5");
                completions.add("10");
                completions.add("20");
            } else if (subcommand.equals("language") && args[1].equals("set")) {
                completions.addAll(Arrays.asList("en", "es", "fr", "de", "it", "pt", "ru", "ja", "zh"));
            }
        }

        return completions;
    }

    private void handleAddHearts(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /lifesteal addhearts <player> <amount>");
            return;
        }

        String targetName = args[1];
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount: " + args[2]);
            return;
        }

        if (targetName.equals("*")) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.addHearts(player, amount);
                player.sendMessage("§a" + amount + " hearts were added by " + sender.getName() + "! You now have " + plugin.getHearts(player) + " hearts.");
            }
            sender.sendMessage("§aAdded " + amount + " hearts to all online players.");
        } else if (targetName.equals("**")) {
            for (org.bukkit.OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
                plugin.addHearts(player, amount);
            }
            sender.sendMessage("§aAdded " + amount + " hearts to all players.");
        } else {
            org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage("§cPlayer not found: " + targetName);
                return;
            }
            
            int oldHearts = plugin.getHearts(target);
            plugin.addHearts(target, amount);
            int newHearts = plugin.getHearts(target);
            
            sender.sendMessage("§aAdded " + amount + " hearts to " + target.getName() + " (" + oldHearts + " → " + newHearts + ")");
            
            if (target.isOnline()) {
                ((Player) target).sendMessage("§aYou received " + amount + " hearts from " + sender.getName() + "! You now have " + newHearts + " hearts.");
            }
        }
    }

    private void handleSetHearts(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /lifesteal sethearts <player> <amount>");
            return;
        }

        String targetName = args[1];
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount: " + args[2]);
            return;
        }

        if (amount < plugin.getMinHearts() || amount > plugin.getMaxHearts()) {
            sender.sendMessage("§cAmount must be between " + plugin.getMinHearts() + " and " + plugin.getMaxHearts() + "!");
            return;
        }

        if (targetName.equals("*")) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.setHearts(player, amount);
                player.sendMessage("§aYour hearts were set to " + amount + " by " + sender.getName() + "!");
            }
            sender.sendMessage("§aSet all online players' hearts to " + amount + ".");
        } else if (targetName.equals("**")) {
            for (org.bukkit.OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
                plugin.setHearts(player, amount);
            }
            sender.sendMessage("§aSet all players' hearts to " + amount + ".");
        } else {
            org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage("§cPlayer not found: " + targetName);
                return;
            }
            
            int oldHearts = plugin.getHearts(target);
            plugin.setHearts(target, amount);
            
            sender.sendMessage("§aSet " + target.getName() + "'s hearts to " + amount + " (" + oldHearts + " → " + amount + ")");
            
            if (target.isOnline()) {
                ((Player) target).sendMessage("§aYour hearts were set to " + amount + " by " + sender.getName() + "!");
            }
        }
    }

    private void handleRemoveHearts(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /lifesteal removehearts <player> <amount>");
            return;
        }

        String targetName = args[1];
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount: " + args[2]);
            return;
        }

        if (targetName.equals("*")) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.removeHearts(player, amount);
                player.sendMessage("§c" + amount + " hearts were removed by " + sender.getName() + "! You now have " + plugin.getHearts(player) + " hearts.");
            }
            sender.sendMessage("§aRemoved " + amount + " hearts from all online players.");
        } else if (targetName.equals("**")) {
            for (org.bukkit.OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
                plugin.removeHearts(player, amount);
            }
            sender.sendMessage("§aRemoved " + amount + " hearts from all players.");
        } else {
            org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage("§cPlayer not found: " + targetName);
                return;
            }
            
            int oldHearts = plugin.getHearts(target);
            plugin.removeHearts(target, amount);
            int newHearts = plugin.getHearts(target);
            
            sender.sendMessage("§aRemoved " + amount + " hearts from " + target.getName() + " (" + oldHearts + " → " + newHearts + ")");
            
            if (target.isOnline()) {
                ((Player) target).sendMessage("§c" + amount + " hearts were removed by " + sender.getName() + "! You now have " + newHearts + " hearts.");
            }
        }
    }



    private void handleReset(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lifesteal reset <player>");
            return;
        }

        String targetName = args[1];

        if (targetName.equals("*")) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.setHearts(player, 10);
                player.sendMessage("§aYour hearts have been reset to 10 by " + sender.getName() + "!");
            }
            sender.sendMessage("§aReset hearts to 10 for all online players.");
        } else if (targetName.equals("**")) {
            for (org.bukkit.OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
                plugin.setHearts(player, 10);
            }
            sender.sendMessage("§aReset hearts to 10 for all players.");
        } else {
            org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage("§cPlayer not found: " + targetName);
                return;
            }
            
            plugin.setHearts(target, 10);
            sender.sendMessage("§aReset " + target.getName() + "'s hearts to 10.");
            
            if (target.isOnline()) {
                ((Player) target).sendMessage("§aYour hearts have been reset to 10 by " + sender.getName() + "!");
            }
        }
    }

    private void handleSetHeartName(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lifesteal name <name>");
            return;
        }

        StringBuilder name = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            name.append(args[i]).append(" ");
        }
        
        plugin.setCustomHeartName(name.toString().trim());
        sender.sendMessage("§aSet custom heart name to: " + name.toString().trim());
    }

    private void handleSetHeartLore(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lifesteal lore <lore>");
            return;
        }

        List<String> lore = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            lore.add(args[i]);
        }
        
        plugin.setCustomHeartLore(lore);
        sender.sendMessage("§aSet custom heart lore to: " + String.join(" ", lore));
    }

    private void handleGetHearts(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command can only be used by players!");
                return;
            }
            
            Player player = (Player) sender;
            int hearts = plugin.getHearts(player);
            player.sendMessage("§6§l=== Your Hearts ===");
            player.sendMessage("§eCurrent Hearts: §7" + hearts);
            player.sendMessage("§eMax Hearts: §7" + plugin.getMaxHeartsForPlayer(player));
            player.sendMessage("§eMin Hearts: §7" + plugin.getMinHearts());
            
            StringBuilder heartBar = new StringBuilder("§c");
            for (int i = 0; i < hearts; i++) {
                heartBar.append("❤");
            }
            player.sendMessage(heartBar.toString());
            
        } else if (args.length == 2) {
            String targetName = args[1];
            org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
            
            if (!target.hasPlayedBefore()) {
                sender.sendMessage("§cPlayer not found: " + targetName);
                return;
            }
            
            int hearts = plugin.getHearts(target);
            sender.sendMessage("§6§l=== " + target.getName() + "'s Hearts ===");
            sender.sendMessage("§eCurrent Hearts: §7" + hearts);
            sender.sendMessage("§eMax Hearts: §7" + plugin.getMaxHeartsForPlayer(target));
            sender.sendMessage("§eMin Hearts: §7" + plugin.getMinHearts());
            
            StringBuilder heartBar = new StringBuilder("§c");
            for (int i = 0; i < hearts; i++) {
                heartBar.append("❤");
            }
            sender.sendMessage(heartBar.toString());
        }
    }

    private void handleTop(CommandSender sender) {
        List<org.bukkit.OfflinePlayer> topPlayers = plugin.getTopPlayers(10);
        
        sender.sendMessage("§6§l=== Top Heart Players ===");
        for (int i = 0; i < topPlayers.size(); i++) {
            org.bukkit.OfflinePlayer player = topPlayers.get(i);
            int hearts = plugin.getHearts(player);
            sender.sendMessage("§e#" + (i + 1) + " §7" + player.getName() + " §e- §7" + hearts + " hearts");
        }
    }

    private void handleHistory(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThis command can only be used by players!");
                return;
            }
            
            sender.sendMessage("§6§l=== Your Heart History ===");
            sender.sendMessage("§7No transaction history available yet.");
            
        } else if (args.length == 2) {
            String targetName = args[1];
            org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
            
            if (!target.hasPlayedBefore()) {
                sender.sendMessage("§cPlayer not found: " + targetName);
                return;
            }
            
            sender.sendMessage("§6§l=== " + target.getName() + "'s Heart History ===");
            sender.sendMessage("§7No transaction history available yet.");
        }
    }

    private void handleDebug(CommandSender sender) {
        sender.sendMessage("§6§l=== Debug Information ===");
        sender.sendMessage("§ePlugin: §7" + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
        sender.sendMessage("§eMax Hearts: §7" + plugin.getMaxHearts());
        sender.sendMessage("§eMin Hearts: §7" + plugin.getMinHearts());
        sender.sendMessage("§ePlayer Hearts Data Size: §7" + plugin.getPlayerHeartsSize());
        sender.sendMessage("§ePlayer Max Limits Size: §7" + plugin.getPlayerMaxLimitsSize());
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage("§eYour Hearts: §7" + plugin.getHearts(player));
            sender.sendMessage("§eYour Max Limit: §7" + plugin.getMaxHeartsForPlayer(player));
            sender.sendMessage("§eYour Max Health: §7" + player.getMaxHealth());
        }
    }

    private void handleSetMaxHearts(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lifesteal setmaxhearts <amount>");
            return;
        }

        int newMaxHearts;
        try {
            newMaxHearts = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount: " + args[1]);
            return;
        }

        if (newMaxHearts <= 0) {
            sender.sendMessage("§cMax hearts must be positive!");
            return;
        }

        if (newMaxHearts < plugin.getMinHearts()) {
            sender.sendMessage("§cMax hearts cannot be less than minimum hearts (" + plugin.getMinHearts() + ")!");
            return;
        }

        int oldMaxHearts = plugin.getMaxHearts();
        plugin.setGlobalMaxHearts(newMaxHearts);
        
        sender.sendMessage("§aGlobal max hearts changed from " + oldMaxHearts + " to " + newMaxHearts + "!");
        sender.sendMessage("§7Note: This affects all players and will be saved to config.");
    }

    private void handleGiveHearts(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /lifesteal givehearts <player> <amount>");
            return;
        }

        String targetName = args[1];
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount: " + args[2]);
            return;
        }

        if (amount <= 0) {
            sender.sendMessage("§cAmount must be positive!");
            return;
        }

        org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
        if (!target.hasPlayedBefore()) {
            sender.sendMessage("§cPlayer not found: " + targetName);
            return;
        }

        int oldHearts = plugin.getHearts(target);
        plugin.addHearts(target, amount);
        int newHearts = plugin.getHearts(target);
        
        sender.sendMessage("§aGave " + amount + " hearts to " + target.getName() + " (" + oldHearts + " → " + newHearts + ")");
        
        // If player is online, send them a message
        if (target.isOnline()) {
            ((org.bukkit.entity.Player) target).sendMessage("§aYou received " + amount + " hearts from " + sender.getName() + "! You now have " + newHearts + " hearts.");
        }
    }

    private void handleBanStatus(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lifesteal banstatus <player>");
            return;
        }

        String targetName = args[1];
        org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
        
        if (!target.hasPlayedBefore()) {
            sender.sendMessage("§cPlayer not found: " + targetName);
            return;
        }

        if (plugin.isPlayerTempBanned(target.getUniqueId())) {
            long remainingTime = plugin.getRemainingBanTime(target.getUniqueId());
            String formattedTime = plugin.formatRemainingTime(remainingTime);
            sender.sendMessage("§c" + target.getName() + " is temporarily banned from the lifesteal server.");
            sender.sendMessage("§cRemaining time: §e" + formattedTime);
        } else {
            sender.sendMessage("§a" + target.getName() + " is not banned from the lifesteal server.");
        }
    }

    private void handleUnban(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lifesteal unban <player>");
            return;
        }

        String targetName = args[1];
        org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
        
        if (!target.hasPlayedBefore()) {
            sender.sendMessage("§cPlayer not found: " + targetName);
            return;
        }

        if (plugin.isPlayerTempBanned(target.getUniqueId())) {
            // Remove from temp ban list
            plugin.unbanPlayer(target.getUniqueId());
            sender.sendMessage("§a" + target.getName() + " has been unbanned from the lifesteal server.");
        } else {
            sender.sendMessage("§c" + target.getName() + " is not banned from the lifesteal server.");
        }
    }

    private void handleRevive(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /lifesteal revive <player>");
            return;
        }

        String targetName = args[1];
        org.bukkit.OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetName);
        
        if (!target.hasPlayedBefore()) {
            sender.sendMessage("§cPlayer not found: " + targetName);
            return;
        }

        if (plugin.isPlayerTempBanned(target.getUniqueId())) {
            // Remove from temp ban list
            plugin.unbanPlayer(target.getUniqueId());
            
            // Give them some hearts so they can play again
            int currentHearts = plugin.getHearts(target);
            int minHearts = plugin.getMinHearts();
            int heartsToGive = Math.max(1, minHearts - currentHearts + 1);
            
            if (heartsToGive > 0) {
                plugin.addHearts(target, heartsToGive);
                sender.sendMessage("§a" + target.getName() + " has been revived and given " + heartsToGive + " hearts!");
                sender.sendMessage("§7They now have " + plugin.getHearts(target) + " hearts and can rejoin the server.");
            } else {
                sender.sendMessage("§a" + target.getName() + " has been revived!");
                sender.sendMessage("§7They can now rejoin the server.");
            }
        } else {
            sender.sendMessage("§c" + target.getName() + " is not banned from the lifesteal server.");
        }
    }

    private void handleAliases(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§6§l=== Command Aliases ===");
            sender.sendMessage("§e/ls aliases list §7- List all command aliases");
            sender.sendMessage("§e/ls aliases add <command> <alias> §7- Add an alias");
            sender.sendMessage("§e/ls aliases remove <command> <alias> §7- Remove an alias");
            sender.sendMessage("§e/ls aliases reload §7- Reload aliases from config");
            return;
        }

        String subcommand = args[1].toLowerCase();

        switch (subcommand) {
            case "list":
                sender.sendMessage("§6§l=== Current Command Aliases ===");
                for (String command : plugin.getConfig().getConfigurationSection("command-aliases").getKeys(false)) {
                    List<String> aliases = plugin.getConfig().getStringList("command-aliases." + command);
                    sender.sendMessage("§e" + command + "§7: §f" + String.join(", ", aliases));
                }
                break;

            case "add":
                if (args.length < 4) {
                    sender.sendMessage("§cUsage: /ls aliases add <command> <alias>");
                    return;
                }
                String commandName = args[2];
                String newAlias = args[3];
                plugin.getAliasManager().addAlias(commandName, newAlias);
                sender.sendMessage("§aAdded alias '" + newAlias + "' for command '" + commandName + "'");
                break;

            case "remove":
                if (args.length < 4) {
                    sender.sendMessage("§cUsage: /ls aliases remove <command> <alias>");
                    return;
                }
                String cmdName = args[2];
                String removeAlias = args[3];
                plugin.getAliasManager().removeAlias(cmdName, removeAlias);
                sender.sendMessage("§aRemoved alias '" + removeAlias + "' for command '" + cmdName + "'");
                break;

            case "reload":
                plugin.getAliasManager().updatePluginYmlAliases();
                sender.sendMessage("§aAliases reloaded! Please restart the server for changes to take effect.");
                break;

            default:
                sender.sendMessage("§cUnknown subcommand: " + subcommand);
                break;
        }
    }

    private void handleLanguage(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§6§l=== Language Settings ===");
            sender.sendMessage("§e/ls language list §7- List available languages");
            sender.sendMessage("§e/ls language set <lang> §7- Set language");
            sender.sendMessage("§e/ls language current §7- Show current language");
            sender.sendMessage("§7Current language: §f" + plugin.getLanguageManager().getCurrentLanguage());
            return;
        }

        String subcommand = args[1].toLowerCase();

        switch (subcommand) {
            case "list":
                sender.sendMessage("§6§l=== Available Languages ===");
                String[] languages = plugin.getLanguageManager().getAvailableLanguages();
                for (String lang : languages) {
                    String displayName = getLanguageDisplayName(lang);
                    String current = lang.equals(plugin.getLanguageManager().getCurrentLanguage()) ? " §a(Current)" : "";
                    sender.sendMessage("§e" + lang + " §7- §f" + displayName + current);
                }
                break;

            case "set":
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /ls language set <language>");
                    return;
                }
                String newLang = args[2].toLowerCase();
                if (plugin.getLanguageManager().isLanguageAvailable(newLang)) {
                    plugin.getLanguageManager().setLanguage(newLang);
                    sender.sendMessage("§aLanguage changed to: " + newLang);
                    sender.sendMessage("§7Some messages may require a server restart to update.");
                } else {
                    sender.sendMessage("§cLanguage '" + newLang + "' not found!");
                    sender.sendMessage("§7Use §e/ls language list §7to see available languages.");
                }
                break;

            case "current":
                String currentLang = plugin.getLanguageManager().getCurrentLanguage();
                String displayName = getLanguageDisplayName(currentLang);
                sender.sendMessage("§7Current language: §f" + currentLang + " §7- " + displayName);
                break;

            default:
                sender.sendMessage("§cUnknown subcommand: " + subcommand);
                break;
        }
    }

    private String getLanguageDisplayName(String langCode) {
        switch (langCode) {
            case "en": return "English";
            case "es": return "Español";
            case "fr": return "Français";
            case "de": return "Deutsch";
            case "it": return "Italiano";
            case "pt": return "Português";
            case "ru": return "Русский";
            case "ja": return "日本語";
            case "zh": return "中文";
            default: return langCode.toUpperCase();
        }
    }

}
