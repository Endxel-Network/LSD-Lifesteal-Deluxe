package Endxel;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Manages command aliases from configuration
 */
public class AliasManager {
    
    private final LSD plugin;
    
    public AliasManager(LSD plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Update plugin.yml with aliases from config
     */
    public void updatePluginYmlAliases() {
        try {
            File pluginYmlFile = new File(plugin.getDataFolder().getParentFile(), "Lifesteal-Deluxe/plugin.yml");
            if (!pluginYmlFile.exists()) {
                plugin.getLogger().warning("plugin.yml not found! Cannot update aliases.");
                return;
            }
            
            FileConfiguration pluginYml = YamlConfiguration.loadConfiguration(pluginYmlFile);
            FileConfiguration config = plugin.getConfig();
            
            // Update aliases for each command
            if (config.contains("command-aliases")) {
                for (String commandName : config.getConfigurationSection("command-aliases").getKeys(false)) {
                    List<String> aliases = config.getStringList("command-aliases." + commandName);
                    if (aliases != null && !aliases.isEmpty()) {
                        pluginYml.set("commands." + commandName + ".aliases", aliases);
                        plugin.getLogger().info("Updated aliases for " + commandName + ": " + String.join(", ", aliases));
                    }
                }
                
                // Save the updated plugin.yml
                pluginYml.save(pluginYmlFile);
                plugin.getLogger().info("Updated plugin.yml with new aliases. Please restart the server for changes to take effect.");
            }
            
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to update plugin.yml aliases: " + e.getMessage());
        }
    }
    
    /**
     * Get aliases for a command from config
     */
    public List<String> getAliases(String commandName) {
        return plugin.getConfig().getStringList("command-aliases." + commandName);
    }
    
    /**
     * Add an alias for a command
     */
    public void addAlias(String commandName, String alias) {
        List<String> aliases = getAliases(commandName);
        if (!aliases.contains(alias)) {
            aliases.add(alias);
            plugin.getConfig().set("command-aliases." + commandName, aliases);
            plugin.saveConfig();
            plugin.getLogger().info("Added alias '" + alias + "' for command '" + commandName + "'");
        }
    }
    
    /**
     * Remove an alias for a command
     */
    public void removeAlias(String commandName, String alias) {
        List<String> aliases = getAliases(commandName);
        if (aliases.remove(alias)) {
            plugin.getConfig().set("command-aliases." + commandName, aliases);
            plugin.saveConfig();
            plugin.getLogger().info("Removed alias '" + alias + "' for command '" + commandName + "'");
        }
    }
}
