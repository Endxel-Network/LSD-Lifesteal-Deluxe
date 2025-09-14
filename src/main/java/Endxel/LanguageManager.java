package Endxel;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages language translations and message formatting
 */
public class LanguageManager {
    
    private final LSD plugin;
    private FileConfiguration langConfig;
    private String currentLanguage;
    private Map<String, String> messageCache;
    
    public LanguageManager(LSD plugin) {
        this.plugin = plugin;
        this.messageCache = new HashMap<>();
        loadLanguageConfig();
    }
    
    /**
     * Load the language configuration
     */
    private void loadLanguageConfig() {
        // Create lang.yml if it doesn't exist
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        
        // Load the language file
        langConfig = YamlConfiguration.loadConfiguration(langFile);
        
        // Get current language from config
        currentLanguage = plugin.getConfig().getString("language", "en");
        
        // Validate language
        if (!langConfig.contains(currentLanguage)) {
            plugin.getLogger().warning("Language '" + currentLanguage + "' not found! Using English (en) as fallback.");
            currentLanguage = "en";
        }
        
        plugin.getLogger().info("Language set to: " + currentLanguage);
    }
    
    /**
     * Get a translated message
     * @param key The message key
     * @return The translated message with color codes converted
     */
    public String getMessage(String key) {
        return getMessage(key, new HashMap<>());
    }
    
    /**
     * Get a translated message with placeholders
     * @param key The message key
     * @param placeholders Map of placeholder replacements
     * @return The translated message with placeholders replaced and color codes converted
     */
    public String getMessage(String key, Map<String, String> placeholders) {
        // Check cache first
        String cacheKey = key + "_" + placeholders.hashCode();
        if (messageCache.containsKey(cacheKey)) {
            return messageCache.get(cacheKey);
        }
        
        // Get the message from config
        String message = langConfig.getString(currentLanguage + "." + key);
        
        // Fallback to English if not found
        if (message == null) {
            message = langConfig.getString("en." + key);
            if (message == null) {
                plugin.getLogger().warning("Message key not found: " + key);
                return "&cMessage not found: " + key;
            }
        }
        
        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        // Convert color codes
        message = message.replace("&", "§");
        
        // Cache the result
        messageCache.put(cacheKey, message);
        
        return message;
    }
    
    /**
     * Get a translated message with placeholders (convenience method)
     * @param key The message key
     * @param placeholder The placeholder key
     * @param value The placeholder value
     * @return The translated message
     */
    public String getMessage(String key, String placeholder, String value) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, value);
        return getMessage(key, placeholders);
    }
    
    /**
     * Get a translated message with multiple placeholders (convenience method)
     * @param key The message key
     * @param placeholder1 First placeholder key
     * @param value1 First placeholder value
     * @param placeholder2 Second placeholder key
     * @param value2 Second placeholder value
     * @return The translated message
     */
    public String getMessage(String key, String placeholder1, String value1, String placeholder2, String value2) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder1, value1);
        placeholders.put(placeholder2, value2);
        return getMessage(key, placeholders);
    }
    
    /**
     * Get a translated message with multiple placeholders (convenience method)
     * @param key The message key
     * @param placeholder1 First placeholder key
     * @param value1 First placeholder value
     * @param placeholder2 Second placeholder key
     * @param value2 Second placeholder value
     * @param placeholder3 Third placeholder key
     * @param value3 Third placeholder value
     * @return The translated message
     */
    public String getMessage(String key, String placeholder1, String value1, String placeholder2, String value2, String placeholder3, String value3) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder1, value1);
        placeholders.put(placeholder2, value2);
        placeholders.put(placeholder3, value3);
        return getMessage(key, placeholders);
    }
    
    /**
     * Reload the language configuration
     */
    public void reload() {
        messageCache.clear();
        loadLanguageConfig();
    }
    
    /**
     * Set the current language
     * @param language The language code
     */
    public void setLanguage(String language) {
        if (langConfig.contains(language)) {
            currentLanguage = language;
            plugin.getConfig().set("language", language);
            plugin.saveConfig();
            messageCache.clear();
            plugin.getLogger().info("Language changed to: " + language);
        } else {
            plugin.getLogger().warning("Language '" + language + "' not found! Available languages: " + getAvailableLanguages());
        }
    }
    
    /**
     * Get the current language
     * @return The current language code
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    /**
     * Get all available languages
     * @return Array of available language codes
     */
    public String[] getAvailableLanguages() {
        return langConfig.getKeys(false).toArray(new String[0]);
    }
    
    /**
     * Check if a language is available
     * @param language The language code
     * @return True if available
     */
    public boolean isLanguageAvailable(String language) {
        return langConfig.contains(language);
    }
}
