package Endxel;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

/**
 * Simple API for Lifesteal Deluxe plugin
 * Allows other plugins to interact with the heart system
 */
public class LifestealAPI {
    
    private final LSD plugin;
    
    public LifestealAPI(LSD plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Get the number of hearts a player has
     * @param player The player to check
     * @return Number of hearts (1 heart = 2 health points)
     */
    public int getHearts(OfflinePlayer player) {
        return plugin.getHearts(player);
    }
    
    /**
     * Set the number of hearts a player has
     * @param player The player to modify
     * @param hearts Number of hearts to set
     * @return true if successful, false if invalid amount
     */
    public boolean setHearts(OfflinePlayer player, int hearts) {
        if (hearts < plugin.getMinHearts() || hearts > plugin.getMaxHearts()) {
            return false;
        }
        plugin.setHearts(player, hearts);
        return true;
    }
    
    /**
     * Add hearts to a player
     * @param player The player to modify
     * @param amount Number of hearts to add
     * @return true if successful, false if would exceed max hearts
     */
    public boolean addHearts(OfflinePlayer player, int amount) {
        int currentHearts = getHearts(player);
        int newHearts = currentHearts + amount;
        
        if (newHearts > plugin.getMaxHearts()) {
            return false;
        }
        
        plugin.addHearts(player, amount);
        return true;
    }
    
    /**
     * Remove hearts from a player
     * @param player The player to modify
     * @param amount Number of hearts to remove
     * @return true if successful, false if would go below min hearts
     */
    public boolean removeHearts(OfflinePlayer player, int amount) {
        int currentHearts = getHearts(player);
        int newHearts = currentHearts - amount;
        
        if (newHearts < plugin.getMinHearts()) {
            return false;
        }
        
        plugin.removeHearts(player, amount);
        return true;
    }
    
    /**
     * Get the maximum hearts allowed
     * @return Maximum hearts
     */
    public int getMaxHearts() {
        return plugin.getMaxHearts();
    }
    
    /**
     * Get the minimum hearts allowed
     * @return Minimum hearts
     */
    public int getMinHearts() {
        return plugin.getMinHearts();
    }
    
    /**
     * Check if a player is temporarily banned from the lifesteal server
     * @param player The player to check
     * @return true if banned, false otherwise
     */
    public boolean isPlayerBanned(OfflinePlayer player) {
        return plugin.isPlayerTempBanned(player.getUniqueId());
    }
    
    /**
     * Get the remaining ban time for a player in milliseconds
     * @param player The player to check
     * @return Remaining ban time in milliseconds, 0 if not banned
     */
    public long getRemainingBanTime(OfflinePlayer player) {
        return plugin.getRemainingBanTime(player.getUniqueId());
    }
    
    /**
     * Unban a player from the lifesteal server
     * @param player The player to unban
     */
    public void unbanPlayer(OfflinePlayer player) {
        plugin.unbanPlayer(player.getUniqueId());
    }
    
    /**
     * Check if the plugin is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isPluginEnabled() {
        return plugin.isPluginEnabled();
    }
    
    /**
     * Get all player heart data
     * @return Map of UUID to heart count
     */
    public Map<UUID, Integer> getAllPlayerHearts() {
        return plugin.getPlayerHearts();
    }
    
    /**
     * Check if a player has played before (has heart data)
     * @param player The player to check
     * @return true if player has data, false otherwise
     */
    public boolean hasPlayerData(OfflinePlayer player) {
        return plugin.getPlayerHearts().containsKey(player.getUniqueId());
    }
    
    /**
     * Get the plugin instance
     * @return The LSD plugin instance
     */
    public Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * Update a player's health based on their heart count
     * @param player The player to update
     */
    public void updatePlayerHealth(Player player) {
        plugin.updatePlayerHealth(player);
    }
    
    /**
     * Check if heart stealing is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isHeartStealingEnabled() {
        return plugin.isGainHeartOnKill();
    }
    
    /**
     * Check if heart loss on death is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isHeartLossOnDeathEnabled() {
        return plugin.isLoseHeartOnDeath();
    }
}
