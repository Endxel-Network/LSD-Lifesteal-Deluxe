# Lifesteal Deluxe API v2.6

## Overview
Lifesteal Deluxe now includes a simple API that allows other plugins to interact with the heart system.

## Getting Started

### 1. Get the API Instance
```java
// Get the Lifesteal Deluxe plugin
LSD lifestealPlugin = (LSD) getServer().getPluginManager().getPlugin("Lifesteal-Deluxe");

if (lifestealPlugin == null) {
    getLogger().severe("Lifesteal Deluxe not found! Disabling plugin.");
    getServer().getPluginManager().disablePlugin(this);
    return;
}

// Get the API
LifestealAPI api = lifestealPlugin.getAPI();
```

## API Methods

### Heart Management
```java
// Get player's hearts
int hearts = api.getHearts(player);

// Set player's hearts (returns false if invalid amount)
boolean success = api.setHearts(player, 10);

// Add hearts to player (returns false if would exceed max)
boolean success = api.addHearts(player, 5);

// Remove hearts from player (returns false if would go below min)
boolean success = api.removeHearts(player, 3);
```

### Configuration
```java
// Get max/min hearts
int maxHearts = api.getMaxHearts();
int minHearts = api.getMinHearts();

// Check if plugin is enabled
boolean enabled = api.isPluginEnabled();

// Check heart stealing settings
boolean heartStealing = api.isHeartStealingEnabled();
boolean heartLoss = api.isHeartLossOnDeathEnabled();
```

### Ban System
```java
// Check if player is banned
boolean isBanned = api.isPlayerBanned(player);

// Get remaining ban time (in milliseconds)
long banTime = api.getRemainingBanTime(player);

// Unban a player
api.unbanPlayer(player);
```

### Player Data
```java
// Check if player has heart data
boolean hasData = api.hasPlayerData(player);

// Get all player heart data
Map<UUID, Integer> allHearts = api.getAllPlayerHearts();

// Update player's health display
api.updatePlayerHealth(player);
```

## Example Plugin

See `APIExample.java` in the source code for a complete example of how to use the API.

## Dependencies

Add Lifesteal Deluxe as a dependency in your plugin.yml:
```yaml
depend: [Lifesteal-Deluxe]
```

Or as a soft dependency:
```yaml
softdepend: [Lifesteal-Deluxe]
```

## Notes

- All heart values are in full hearts (1 heart = 2 health points in Minecraft)
- The API automatically handles validation (min/max hearts)
- Methods return `false` if the operation would violate heart limits
- The API is thread-safe and can be used from async tasks
