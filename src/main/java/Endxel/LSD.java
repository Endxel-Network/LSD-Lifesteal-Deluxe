package Endxel;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.OfflinePlayer;

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
    private boolean useHalfHearts;
    private boolean dropToFloor;
    private boolean pluginEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        
        playerHearts = new HashMap<>();
        playerMaxLimits = new HashMap<>();
        heartItemManager = new HeartItemManager(this);
        
        getLogger().info("Initialized with max hearts: " + (useHalfHearts ? maxHearts : maxHearts/2) + ", min hearts: " + (useHalfHearts ? minHearts : minHearts/2));
        
        if (pluginEnabled) {
            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(new HeartItemListener(this), this);
            registerCommands();
            getLogger().info("Lifesteal Deluxe is ENABLED and ready to use!");
        } else {
            getLogger().info("Lifesteal Deluxe is DISABLED - no functionality will work until enabled in config!");
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

    @Override
    public void onDisable() {
        savePlayerData();
        getLogger().info("Lifesteal Deluxe has been disabled!");
    }

    private void registerCommands() {
        getCommand("lifesteal").setExecutor(new LifestealCommand(this));
        getCommand("hearts").setExecutor(new HeartsCommand(this));
        getCommand("withdraw").setExecutor(new WithdrawCommand(this));
        getCommand("deposit").setExecutor(new DepositCommand(this));
        getCommand("sethearts").setExecutor(new SetHeartsCommand(this));
        getCommand("addhearts").setExecutor(new AddHeartsCommand(this));
        getCommand("removehearts").setExecutor(new RemoveHeartsCommand(this));
        getCommand("payhearts").setExecutor(new PayHeartsCommand(this));
    }

    private void loadConfig() {
        reloadConfig();
        config = getConfig();
        
        maxHearts = config.getInt("max-hearts", 20) * 2;
        minHearts = config.getInt("min-hearts", 1) * 2;
        loseHeartOnDeath = config.getBoolean("lose-heart-on-death", true);
        gainHeartOnKill = config.getBoolean("gain-heart-on-kill", true);
        dropHeartOnDeath = config.getBoolean("drop-heart-on-death", true);
        heartDropChance = config.getDouble("heart-drop-chance", 0.1);
        customHeartName = config.getString("custom-heart-name", "§c§l❤ Extra Heart");
        useHalfHearts = config.getBoolean("use-half-hearts", false);
        dropToFloor = config.getBoolean("drop-to-floor", true);
        pluginEnabled = config.getBoolean("plugin-enabled", true);
        
        customHeartLore = config.getStringList("custom-heart-lore");
        if (customHeartLore.isEmpty()) {
            customHeartLore = List.of("§7Right-click to consume", "§7Gives you +1 heart");
        }
        
        loadPlayerData();
    }

    private void loadPlayerData() {
        if (config.contains("players") && config.getConfigurationSection("players") != null) {
            for (String uuidString : config.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int hearts = config.getInt("players." + uuidString + ".hearts", 20);
                    playerHearts.put(uuid, hearts);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Invalid UUID in config: " + uuidString);
                }
            }
        }
    }

    private void savePlayerData() {
        for (Map.Entry<UUID, Integer> entry : playerHearts.entrySet()) {
            config.set("players." + entry.getKey().toString() + ".hearts", entry.getValue());
        }
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!pluginEnabled) return;
        
        Player player = event.getPlayer();
        if (!playerHearts.containsKey(player.getUniqueId())) {
            playerHearts.put(player.getUniqueId(), 20);
        }
        updatePlayerHealth(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!pluginEnabled) return;
        
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer != null && gainHeartOnKill) {
            addHearts(killer, 1);
            killer.sendMessage("§a+1 Heart! You now have " + getHearts(killer) + " hearts.");
        }
        
        if (loseHeartOnDeath) {
            removeHearts(victim, 1);
            victim.sendMessage("§c-1 Heart! You now have " + getHearts(victim) + " hearts.");
        }
        
        if (dropHeartOnDeath && Math.random() < heartDropChance) {
            dropHeartItem(victim, killer);
        }
        
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
        int currentHearts = playerHearts.getOrDefault(uuid, 20);
        int amountInHearts = useHalfHearts ? amount : amount * 2;
        
        int newHearts = Math.min(currentHearts + amountInHearts, maxHearts);
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
        int currentHearts = playerHearts.getOrDefault(uuid, 20);
        int amountInHearts = useHalfHearts ? amount : amount * 2;
        
        int newHearts = Math.max(currentHearts - amountInHearts, minHearts);
        playerHearts.put(uuid, newHearts);
        updatePlayerHealth(player);
        savePlayerData();
    }

    public void setHearts(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int amountInHearts = useHalfHearts ? amount : amount * 2;
        int clampedHearts = Math.max(minHearts, Math.min(amountInHearts, maxHearts));
        playerHearts.put(uuid, clampedHearts);
        updatePlayerHealth(player);
        savePlayerData();
    }

    public int getHearts(Player player) {
        int hearts = playerHearts.getOrDefault(player.getUniqueId(), 20);
        return useHalfHearts ? hearts : hearts / 2;
    }

    private void updatePlayerHealth(Player player) {
        int hearts = getHearts(player);
        try {
            player.setMaxHealth(hearts);
        } catch (Exception e) {
            getLogger().warning("Failed to update player health for " + player.getName() + ": " + e.getMessage());
        }
    }

    public void reloadPlugin() {
        loadConfig();
        getLogger().info("Configuration reloaded!");
    }

    public int getMaxHearts() {
        return useHalfHearts ? maxHearts : maxHearts / 2;
    }

    public int getMinHearts() {
        return useHalfHearts ? minHearts : minHearts / 2;
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
        int currentHearts = playerHearts.getOrDefault(uuid, 20);
        int amountInHearts = useHalfHearts ? amount : amount * 2;
        int newHearts = Math.min(currentHearts + amountInHearts, getMaxHeartsForPlayer(player));
        playerHearts.put(uuid, newHearts);
        savePlayerData();
    }

    public int getHearts(OfflinePlayer player) {
        int hearts = playerHearts.getOrDefault(player.getUniqueId(), 20);
        return useHalfHearts ? hearts : hearts / 2;
    }

    public void setHearts(OfflinePlayer player, int amount) {
        UUID uuid = player.getUniqueId();
        int amountInHearts = useHalfHearts ? amount : amount * 2;
        int clampedHearts = Math.max(minHearts, Math.min(amountInHearts, getMaxHeartsForPlayer(player)));
        playerHearts.put(uuid, clampedHearts);
        savePlayerData();
    }

    public void addHearts(OfflinePlayer player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentHearts = playerHearts.getOrDefault(uuid, 20);
        int amountInHearts = useHalfHearts ? amount : amount * 2;
        int newHearts = Math.min(currentHearts + amountInHearts, getMaxHeartsForPlayer(player));
        playerHearts.put(uuid, newHearts);
        savePlayerData();
    }

    public void removeHearts(OfflinePlayer player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentHearts = playerHearts.getOrDefault(uuid, 20);
        int amountInHearts = useHalfHearts ? amount : amount * 2;
        int newHearts = Math.max(currentHearts - amountInHearts, minHearts);
        playerHearts.put(uuid, newHearts);
        savePlayerData();
    }

    public int getMaxHeartsForPlayer(OfflinePlayer player) {
        int hearts = playerMaxLimits.getOrDefault(player.getUniqueId(), maxHearts);
        return useHalfHearts ? hearts : hearts / 2;
    }

    public int getMaxHeartsForPlayer(Player player) {
        int hearts = playerMaxLimits.getOrDefault(player.getUniqueId(), maxHearts);
        return useHalfHearts ? hearts : hearts / 2;
    }

    public void setPlayerMaxLimit(OfflinePlayer player, int limit) {
        int limitInHearts = useHalfHearts ? limit : limit * 2;
        playerMaxLimits.put(player.getUniqueId(), limitInHearts);
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

    public boolean isUseHalfHearts() {
        return useHalfHearts;
    }

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
}
