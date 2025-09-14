package Endxel;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public final class LSD extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private Map<UUID, Integer> playerHearts;
    private int maxHearts;
    private int minHearts;
    private boolean loseHeartOnDeath;
    private boolean gainHeartOnKill;
    private boolean dropHeartOnDeath;
    private double heartDropChance;
    private HeartItemManager heartItemManager;
    private String customHeartName;
    private List<String> customHeartLore;
    private Map<UUID, Integer> playerMaxLimits;
    // Removed half-heart system
    private boolean dropToFloor;
    private boolean pluginEnabled;
    
    // Ban system (LIFESTEAL SERVER ONLY)
    private boolean banSystemEnabled;
    private String banDuration;
    private String banMessage;
    private String warningMessage;
    private String kickMessage;
    private boolean preventJoin;
    
    // Temporary ban tracking
    private Map<UUID, Long> tempBannedPlayers; // UUID -> ban end time
    
    // API
    private LifestealAPI api;
    private AliasManager aliasManager;
    private LanguageManager languageManager;
    
    // Messages
    private String heartStolenMessage;
    private String victimHeartStolenMessage;
    private String cannotLoseHeartMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        // Initialize maps first
        playerHearts = new HashMap<>();
        playerMaxLimits = new HashMap<>();
        tempBannedPlayers = new HashMap<>();
        heartItemManager = new HeartItemManager(this);
        api = new LifestealAPI(this);
        aliasManager = new AliasManager(this);
        languageManager = new LanguageManager(this);
        
        // Now load config (which will call loadPlayerData)
        loadConfig();
        
        getLogger().info("Initialized with max hearts: " + maxHearts + ", min hearts: " + minHearts);
        
        // Always register commands, but only enable functionality if plugin is enabled
        registerCommands();
        
        if (pluginEnabled) {
            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(new HeartItemListener(this), this);
            getLogger().info("Lifesteal Deluxe is ENABLED and ready to use!");
        } else {
            getLogger().info("Lifesteal Deluxe is DISABLED - commands will work but no functionality will work until enabled in config!");
        }
        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                new LifestealPlaceholder(this).register();
                getLogger().info("PlaceholderAPI found! Placeholders registered.");
            } catch (Exception e) {
                getLogger().warning("Failed to register PlaceholderAPI expansion: " + e.getMessage());
            }
        } else {
            getLogger().info("PlaceholderAPI not found. Placeholders will not be available.");
        }
        
        getLogger().info("Lifesteal Deluxe has been enabled!");
    }
    
    // Temporary ban management methods
    public void tempBanPlayer(UUID playerUUID, String duration) {
        long banEndTime = System.currentTimeMillis() + parseDurationToMillis(duration);
        tempBannedPlayers.put(playerUUID, banEndTime);
        getLogger().info("Player " + playerUUID + " temporarily banned for " + duration);
    }
    
    public boolean isPlayerTempBanned(UUID playerUUID) {
        if (!tempBannedPlayers.containsKey(playerUUID)) {
            return false;
        }
        
        long banEndTime = tempBannedPlayers.get(playerUUID);
        if (System.currentTimeMillis() >= banEndTime) {
            // Ban expired, remove it
            tempBannedPlayers.remove(playerUUID);
            return false;
        }
        
        return true;
    }
    
    public long getRemainingBanTime(UUID playerUUID) {
        if (!tempBannedPlayers.containsKey(playerUUID)) {
            return 0;
        }
        
        long banEndTime = tempBannedPlayers.get(playerUUID);
        long remaining = banEndTime - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
    
    public String formatRemainingTime(long millis) {
        if (millis <= 0) return "0 seconds";
        
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + " day(s), " + (hours % 24) + " hour(s)";
        } else if (hours > 0) {
            return hours + " hour(s), " + (minutes % 60) + " minute(s)";
        } else if (minutes > 0) {
            return minutes + " minute(s), " + (seconds % 60) + " second(s)";
        } else {
            return seconds + " second(s)";
        }
    }
    
    public void unbanPlayer(UUID playerUUID) {
        tempBannedPlayers.remove(playerUUID);
        getLogger().info("Player " + playerUUID + " manually unbanned from lifesteal server");
    }
    
    private long parseDurationToMillis(String duration) {
        try {
            if (duration.endsWith("s")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 1000L;
            } else if (duration.endsWith("m")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 60000L;
            } else if (duration.endsWith("h")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 3600000L;
            } else if (duration.endsWith("d")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 86400000L;
            } else if (duration.endsWith("w")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 604800000L;
            } else if (duration.endsWith("M")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 2592000000L;
            } else if (duration.endsWith("y")) {
                return Long.parseLong(duration.substring(0, duration.length() - 1)) * 31536000000L;
            } else {
                // Default to seconds
                return Long.parseLong(duration) * 1000L;
            }
        } catch (NumberFormatException e) {
            getLogger().warning("Invalid ban duration format: " + duration + ". Using 1 day as default.");
            return 86400000L; // 1 day default
        }
    }

    @Override
    public void onDisable() {
        savePlayerData();
        getLogger().info("Lifesteal Deluxe has been disabled!");
    }

    private void registerCommands() {
        // Check if commands exist before registering them
        if (getCommand("lifesteal") != null) {
        getCommand("lifesteal").setExecutor(new LifestealCommand(this));
            getLogger().info("Registered command: lifesteal");
        } else {
            getLogger().warning("Command 'lifesteal' not found in plugin.yml!");
        }
        if (getCommand("hearts") != null) {
        getCommand("hearts").setExecutor(new HeartsCommand(this));
            getLogger().info("Registered command: hearts");
        } else {
            getLogger().warning("Command 'hearts' not found in plugin.yml!");
        }
        if (getCommand("withdraw") != null) {
        getCommand("withdraw").setExecutor(new WithdrawCommand(this));
            getLogger().info("Registered command: withdraw");
        } else {
            getLogger().warning("Command 'withdraw' not found in plugin.yml!");
        }
        if (getCommand("sethearts") != null) {
        getCommand("sethearts").setExecutor(new SetHeartsCommand(this));
            getLogger().info("Registered command: sethearts");
        } else {
            getLogger().warning("Command 'sethearts' not found in plugin.yml!");
        }
        if (getCommand("addhearts") != null) {
        getCommand("addhearts").setExecutor(new AddHeartsCommand(this));
            getLogger().info("Registered command: addhearts");
        } else {
            getLogger().warning("Command 'addhearts' not found in plugin.yml!");
        }
        if (getCommand("removehearts") != null) {
        getCommand("removehearts").setExecutor(new RemoveHeartsCommand(this));
            getLogger().info("Registered command: removehearts");
        } else {
            getLogger().warning("Command 'removehearts' not found in plugin.yml!");
        }
        if (getCommand("payhearts") != null) {
        getCommand("payhearts").setExecutor(new PayHeartsCommand(this));
            getLogger().info("Registered command: payhearts");
        } else {
            getLogger().warning("Command 'payhearts' not found in plugin.yml!");
        }
    }

    private void loadConfig() {
        reloadConfig();
        config = getConfig();
        
        // Remove half-heart system completely - hearts are now full hearts
        maxHearts = config.getInt("max-hearts", 20);
        minHearts = config.getInt("min-hearts", 1);
        loseHeartOnDeath = config.getBoolean("lose-heart-on-death", true);
        gainHeartOnKill = config.getBoolean("gain-heart-on-kill", true);
        dropHeartOnDeath = config.getBoolean("drop-heart-on-death", false); // Changed to false by default
        heartDropChance = config.getDouble("heart-drop-chance", 0.1);
        customHeartName = config.getString("custom-heart-name", "&c&l❤ Extra Heart");
        dropToFloor = config.getBoolean("drop-to-floor", false); // Changed to false by default
        pluginEnabled = config.getBoolean("plugin-enabled", true);
        
        // Convert color codes from & to §
        customHeartLore = config.getStringList("custom-heart-lore");
        if (customHeartLore.isEmpty()) {
            customHeartLore = List.of("&7Right-click to consume", "&7Gives you +1 heart");
        }
        
        // Convert & to § in lore
        customHeartLore = customHeartLore.stream()
            .map(line -> line.replace("&", "§"))
            .collect(java.util.stream.Collectors.toList());
        
        // Convert & to § in heart name
        customHeartName = customHeartName.replace("&", "§");
        
        // Load ban system configuration (LIFESTEAL SERVER ONLY)
        banSystemEnabled = config.getBoolean("ban-system.enabled", true);
        banDuration = config.getString("ban-system.ban-duration", "1d");
        preventJoin = config.getBoolean("ban-system.prevent-join", true);
        
        // Load ban messages from language manager
        banMessage = languageManager.getMessage("ban-message");
        warningMessage = languageManager.getMessage("warning-message");
        kickMessage = languageManager.getMessage("kick-message");
        
        // Load messages from language manager
        heartStolenMessage = languageManager.getMessage("heart-stolen");
        victimHeartStolenMessage = languageManager.getMessage("victim-heart-stolen");
        cannotLoseHeartMessage = languageManager.getMessage("cannot-lose-heart");
        
        loadPlayerData();
    }

    private void loadPlayerData() {
        File dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            return;
        }
        
        FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        if (dataConfig.contains("players") && dataConfig.getConfigurationSection("players") != null) {
            for (String uuidString : dataConfig.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int hearts = dataConfig.getInt("players." + uuidString + ".hearts", 10); // 10 hearts = 20 health points
                    playerHearts.put(uuid, hearts);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Invalid UUID in data: " + uuidString);
                }
            }
        }
        
        if (dataConfig.contains("maxLimits") && dataConfig.getConfigurationSection("maxLimits") != null) {
            for (String uuidString : dataConfig.getConfigurationSection("maxLimits").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int limit = dataConfig.getInt("maxLimits." + uuidString, maxHearts);
                    playerMaxLimits.put(uuid, limit);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Invalid UUID in maxLimits: " + uuidString);
                }
            }
        }
        
        // Load temporary bans
        if (dataConfig.contains("tempBans")) {
            for (String uuidString : dataConfig.getConfigurationSection("tempBans").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    long banEndTime = dataConfig.getLong("tempBans." + uuidString);
                    
                    // Only load if ban hasn't expired
                    if (System.currentTimeMillis() < banEndTime) {
                        tempBannedPlayers.put(uuid, banEndTime);
                    }
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Invalid UUID in tempBans: " + uuidString);
                }
            }
        }
    }

    private void savePlayerData() {
        File dataFile = new File(getDataFolder(), "data.yml");
        FileConfiguration dataConfig = new YamlConfiguration();
        
        for (Map.Entry<UUID, Integer> entry : playerHearts.entrySet()) {
            dataConfig.set("players." + entry.getKey().toString() + ".hearts", entry.getValue());
        }
        
        for (Map.Entry<UUID, Integer> entry : playerMaxLimits.entrySet()) {
            dataConfig.set("maxLimits." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save temporary bans
        for (Map.Entry<UUID, Long> entry : tempBannedPlayers.entrySet()) {
            dataConfig.set("tempBans." + entry.getKey().toString(), entry.getValue());
        }
        
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            getLogger().severe("Could not save player data: " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!pluginEnabled) return;
        
        Player player = event.getPlayer();
        if (!playerHearts.containsKey(player.getUniqueId())) {
            playerHearts.put(player.getUniqueId(), 10); // 10 hearts = 20 health points (default)
        }
        
        // Check if player is temporarily banned from the lifesteal server
        if (banSystemEnabled && isPlayerTempBanned(player.getUniqueId())) {
            long remainingTime = getRemainingBanTime(player.getUniqueId());
            String formattedTime = formatRemainingTime(remainingTime);
            
            String kickMsg = banMessage + "\n§cRemaining time: §e" + formattedTime;
            player.kickPlayer(kickMsg);
            
            getLogger().info("Player " + player.getName() + " was kicked from joining due to temporary ban. Remaining: " + formattedTime);
            return;
        }
        
        // Check if player should be kicked for having minimum hearts (if prevent-join is enabled)
        if (banSystemEnabled && preventJoin && getHearts(player) <= minHearts) {
            // Kick them back off the server
            player.kickPlayer(kickMessage);
            getLogger().info("Player " + player.getName() + " was kicked from joining due to having minimum hearts");
            return;
        }
        
        updatePlayerHealth(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!pluginEnabled) return;
        
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        
        if (killer != null && gainHeartOnKill) {
            // Killer gets the victim's heart instead of just +1
            int victimHearts = getHearts(victim);
            if (victimHearts > minHearts) {
                // Take 1 heart from victim and give it to killer
                removeHearts(victim, 1);
            addHearts(killer, 1);
                
                // Send messages with placeholders replaced
                String killerMsg = heartStolenMessage
                    .replace("{victim}", victim.getName())
                    .replace("{hearts}", String.valueOf(getHearts(killer)));
                String victimMsg = victimHeartStolenMessage
                    .replace("{killer}", killer.getName())
                    .replace("{hearts}", String.valueOf(getHearts(victim)));
                
                killer.sendMessage(killerMsg);
                victim.sendMessage(victimMsg);
            } else {
                // Victim is at minimum hearts, can't lose more
                killer.sendMessage(cannotLoseHeartMessage);
            }
        } else if (loseHeartOnDeath) {
            // Only remove heart on death if not killed by another player
            removeHearts(victim, 1);
            victim.sendMessage("§c-1 Heart! You now have " + getHearts(victim) + " hearts.");
        }
        
        // Check if player should be banned
        if (banSystemEnabled && getHearts(victim) <= minHearts) {
            victim.sendMessage(warningMessage);
            
            // Temporarily ban the player from the lifesteal server
            if (banSystemEnabled) {
                // Add them to temporary ban list
                tempBanPlayer(victim.getUniqueId(), banDuration);
                
                // Kick them with the ban message
                victim.kickPlayer(banMessage);
                
                // Log the action
                getLogger().info("Player " + victim.getName() + " was temporarily banned from the lifesteal server for " + banDuration + " for reaching minimum hearts");
            }
        }
        
        // Remove heart dropping to floor - hearts are now stolen directly
        // if (dropHeartOnDeath && Math.random() < heartDropChance) {
        //     dropHeartItem(victim, killer);
        // }
        
        updatePlayerHealth(victim);
        if (killer != null) {
            updatePlayerHealth(killer);
        }
    }

    private void dropHeartItem(Player victim, Player killer) {
        Location dropLocation = victim.getLocation();
        ItemStack heartItem = heartItemManager.createHeartItem();
        
        if (killer != null && !dropToFloor) {
            if (killer.getInventory().firstEmpty() != -1) {
                killer.getInventory().addItem(heartItem);
                killer.sendMessage("§6You received a heart from " + victim.getName() + "'s death!");
                victim.sendMessage("§6A heart was given to " + killer.getName() + " from your death!");
            } else {
                victim.getWorld().dropItemNaturally(dropLocation, heartItem);
                victim.sendMessage("§6A heart dropped from your death!");
                killer.sendMessage("§6A heart dropped from " + victim.getName() + "'s death (inventory full)!");
            }
        } else {
            victim.getWorld().dropItemNaturally(dropLocation, heartItem);
            victim.sendMessage("§6A heart dropped from your death!");
        }
    }

    public void addHearts(Player player, int amount) {
        if (!pluginEnabled) {
            player.sendMessage("§cLifesteal Deluxe is currently disabled!");
            return;
        }
        
        UUID uuid = player.getUniqueId();
        int currentHearts = playerHearts.getOrDefault(uuid, 10); // 10 hearts = 20 health points
        // Removed half-heart system - amount is now full hearts
        
        int newHearts = Math.min(currentHearts + amount, maxHearts);
        playerHearts.put(uuid, newHearts);
        updatePlayerHealth(player);
        savePlayerData();
    }

    public void removeHearts(Player player, int amount) {
        if (!pluginEnabled) {
            player.sendMessage("§cLifesteal Deluxe is currently disabled!");
            return;
        }
        
        UUID uuid = player.getUniqueId();
        int currentHearts = playerHearts.getOrDefault(uuid, 10); // 10 hearts = 20 health points
        // Removed half-heart system - amount is now full hearts
        
        int newHearts = Math.max(currentHearts - amount, minHearts);
        
        // Debug logging (only when debug mode is enabled)
        if (config.getBoolean("advanced.debug-mode", false)) {
            getLogger().info("DEBUG: removeHearts called for " + player.getName() + 
                " - Current: " + currentHearts + ", Amount: " + amount + 
                ", New: " + newHearts + ", Min: " + minHearts);
        }
        
        playerHearts.put(uuid, newHearts);
        updatePlayerHealth(player);
        savePlayerData();
    }

    public void setHearts(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        // Removed half-heart system - amount is now full hearts
        int clampedHearts = Math.max(minHearts, Math.min(amount, maxHearts));
        playerHearts.put(uuid, clampedHearts);
        updatePlayerHealth(player);
        savePlayerData();
    }

    public int getHearts(Player player) {
        int hearts = playerHearts.getOrDefault(player.getUniqueId(), 10); // 10 hearts = 20 health points
        return hearts; // Removed half-heart system
    }

    public void updatePlayerHealth(Player player) {
        int hearts = getHearts(player);
        try {
            // Convert hearts to health (1 heart = 2 health points in Minecraft)
            player.setMaxHealth(hearts * 2);
            
            // Debug logging (only when debug mode is enabled)
            if (config.getBoolean("advanced.debug-mode", false)) {
                getLogger().info("DEBUG: updatePlayerHealth called for " + player.getName() + 
                    " - Hearts: " + hearts + ", Health: " + (hearts * 2));
            }
        } catch (Exception e) {
            getLogger().warning("Failed to update player health for " + player.getName() + ": " + e.getMessage());
        }
    }

    public void reloadPlugin() {
        // Reload configuration
        loadConfig();
        
        // Re-register commands to ensure they work properly
        registerCommands();
        
        getLogger().info("Configuration and commands reloaded!");
    }

    public int getMaxHearts() {
        return maxHearts; // Removed half-heart system
    }

    public int getMinHearts() {
        return minHearts; // Removed half-heart system
    }

    public boolean isLoseHeartOnDeath() {
        return loseHeartOnDeath;
    }

    public boolean isGainHeartOnKill() {
        return gainHeartOnKill;
    }

    public boolean isDropHeartOnDeath() {
        return dropHeartOnDeath;
    }

    public double getHeartDropChance() {
        return heartDropChance;
    }

    public HeartItemManager getHeartItemManager() {
        return heartItemManager;
    }

    public void addOfflineHearts(OfflinePlayer player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentHearts = playerHearts.getOrDefault(uuid, 10); // 10 hearts = 20 health points
        // Removed half-heart system - amount is now full hearts
        int newHearts = Math.min(currentHearts + amount, getMaxHeartsForPlayer(player));
        playerHearts.put(uuid, newHearts);
        
        // Update health if player is online
        if (player.isOnline()) {
            updatePlayerHealth((Player) player);
        }
        
        savePlayerData();
    }

    public int getHearts(OfflinePlayer player) {
        int hearts = playerHearts.getOrDefault(player.getUniqueId(), 10); // 10 hearts = 20 health points
        return hearts; // Removed half-heart system
    }

    public void setHearts(OfflinePlayer player, int amount) {
        UUID uuid = player.getUniqueId();
        // Removed half-heart system - amount is now full hearts
        int clampedHearts = Math.max(minHearts, Math.min(amount, getMaxHeartsForPlayer(player)));
        playerHearts.put(uuid, clampedHearts);
        
        // Update health if player is online
        if (player.isOnline()) {
            updatePlayerHealth((Player) player);
        }
        
        savePlayerData();
    }

    public void addHearts(OfflinePlayer player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentHearts = playerHearts.getOrDefault(uuid, 10); // 10 hearts = 20 health points
        // Removed half-heart system - amount is now full hearts
        int newHearts = Math.min(currentHearts + amount, getMaxHeartsForPlayer(player));
        playerHearts.put(uuid, newHearts);
        
        // Update health if player is online
        if (player.isOnline()) {
            updatePlayerHealth((Player) player);
        }
        
        savePlayerData();
    }

    public void removeHearts(OfflinePlayer player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentHearts = playerHearts.getOrDefault(uuid, 10); // 10 hearts = 20 health points
        // Removed half-heart system - amount is now full hearts
        int newHearts = Math.max(currentHearts - amount, minHearts);
        playerHearts.put(uuid, newHearts);
        
        // Update health if player is online
        if (player.isOnline()) {
            updatePlayerHealth((Player) player);
        }
        
        savePlayerData();
    }

    public int getMaxHeartsForPlayer(OfflinePlayer player) {
        int hearts = playerMaxLimits.getOrDefault(player.getUniqueId(), maxHearts);
        return hearts; // Removed half-heart system
    }

    public int getMaxHeartsForPlayer(Player player) {
        int hearts = playerMaxLimits.getOrDefault(player.getUniqueId(), maxHearts);
        return hearts; // Removed half-heart system
    }

    public void setPlayerMaxLimit(OfflinePlayer player, int limit) {
        // Removed half-heart system - limit is now full hearts
        playerMaxLimits.put(player.getUniqueId(), limit);
    }

    public String getCustomHeartName() {
        return customHeartName;
    }

    public void setCustomHeartName(String name) {
        this.customHeartName = name;
        config.set("custom-heart-name", name);
        saveConfig();
    }

    public List<String> getCustomHeartLore() {
        return customHeartLore;
    }

    public void setCustomHeartLore(List<String> lore) {
        this.customHeartLore = lore;
        config.set("custom-heart-lore", lore);
        saveConfig();
    }

    // Removed half-heart system - this method is no longer needed

    public boolean isDropToFloor() {
        return dropToFloor;
    }

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public void setPluginEnabled(boolean enabled) {
        this.pluginEnabled = enabled;
        config.set("plugin-enabled", enabled);
        saveConfig();
        
        if (enabled) {
            getLogger().info("Lifesteal Deluxe has been ENABLED!");
        } else {
            getLogger().info("Lifesteal Deluxe has been DISABLED!");
        }
    }

    public void setGlobalMaxHearts(int newMaxHearts) {
        this.maxHearts = newMaxHearts * 2;
        config.set("max-hearts", newMaxHearts);
        saveConfig();
        
        getLogger().info("Global max hearts changed to " + newMaxHearts + " (internal: " + this.maxHearts + ")");
        
        for (Player player : getServer().getOnlinePlayers()) {
            updatePlayerHealth(player);
        }
    }

    public List<OfflinePlayer> getTopPlayers(int count) {
        return playerHearts.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(count)
                .map(entry -> Bukkit.getOfflinePlayer(entry.getKey()))
                .collect(java.util.stream.Collectors.toList());
    }

    public int getPlayerHeartsSize() {
        return playerHearts.size();
    }

    public int getPlayerMaxLimitsSize() {
        return playerMaxLimits.size();
    }

    public Map<UUID, Integer> getPlayerHearts() {
        return playerHearts;
    }
    
    /**
     * Get the API instance for external plugins
     * @return LifestealAPI instance
     */
    public LifestealAPI getAPI() {
        return api;
    }
    
    /**
     * Get the alias manager
     * @return AliasManager instance
     */
    public AliasManager getAliasManager() {
        return aliasManager;
    }
    
    /**
     * Get the language manager
     * @return LanguageManager instance
     */
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
    
}
