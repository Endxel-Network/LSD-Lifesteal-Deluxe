# Lifesteal Deluxe - Implemented Features

## ✅ Core Features Implemented

### 1. Heart Management System
- **Dynamic Heart System**: Players can have between 2-40 hearts (configurable)
- **PvP Heart Stealing**: Killers gain +1 heart, victims lose -1 heart
- **Heart Limits**: Configurable minimum and maximum heart limits
- **Persistent Storage**: Player heart data is saved to config and persists across restarts

### 2. Command System
- **Main Command**: `/lifesteal` with subcommands (help, info, reload, hearts, top)
- **Heart Commands**: `/hearts` to check hearts (own or others)
- **Admin Commands**: `/sethearts`, `/addhearts`, `/removehearts`, `/lifesteal <player> <amount>`
- **Player Commands**: `/withdraw`, `/deposit`, `/payhearts` for heart management
- **Bulk Operations**: Use `*` for all online players, `**` for all players ever
- **Advanced Admin**: Set limits, reset hearts, customize heart items
- **Tab Completion**: All commands have intelligent tab completion
- **Permission System**: Granular permissions with LSD permission system

### 3. PlaceholderAPI Integration
- **Rich Placeholders**: 7 different placeholders for scoreboards and chat
- **Heart Information**: Current hearts, max hearts, min hearts
- **Visual Elements**: Heart bar, percentage, remaining hearts
- **Automatic Detection**: Plugin automatically detects if PlaceholderAPI is available

### 4. Heart Item System
- **Physical Heart Items**: Redstone items that can be consumed
- **Item Management**: Withdraw command gives heart items, deposit can consume them
- **Visual Design**: Custom name, lore, and item flags
- **Right-Click Usage**: Players can right-click heart items to consume them
- **Customizable**: Admins can set custom names and lore for heart items

### 5. Player Trading System
- **Heart Transfers**: Players can send hearts to other players
- **Offline Support**: Works with both online and offline players
- **Permission Based**: Requires `LSD.payhearts` permission
- **Safe Transfers**: Prevents players from going below minimum hearts

### 6. Bulk Operations
- **Online Players**: Use `*` to target all currently online players
- **All Players**: Use `**` to target all players who have ever played
- **Admin Commands**: Add/remove hearts, set limits, reset hearts in bulk
- **Efficient Processing**: Fast bulk operations for server management

### 7. Event Handling
- **Player Death Events**: Automatic heart loss/gain on PvP deaths
- **Player Join Events**: Automatic heart initialization for new players
- **Heart Item Interaction**: Right-click to consume heart items
- **Health Updates**: Automatic max health updates when hearts change

### 6. Configuration System
- **Comprehensive Config**: All settings are configurable via config.yml
- **Default Values**: Sensible defaults for all settings
- **Heart Settings**: Max/min hearts, death mechanics
- **World Settings**: Per-world enable/disable options
- **PvP Settings**: PvP restrictions and requirements
- **Message Customization**: All messages can be customized

### 7. Permission System
- **Granular Permissions**: Different permission levels for different features
- **Default Groups**: Sensible default permission assignments
- **Admin Permissions**: Special permissions for server administrators
- **Player Permissions**: Basic permissions for regular players

## 🔧 Technical Features

### 1. Spigot Compatibility
- **API Version**: 1.21+ compatibility
- **Modern Java**: Java 21 support
- **Bukkit Events**: Proper event handling and registration
- **Command Framework**: Bukkit command system integration

### 2. Error Handling
- **Graceful Degradation**: Plugin continues working even if PlaceholderAPI is missing
- **Exception Handling**: Proper error handling for all operations
- **Logging**: Comprehensive logging for debugging and monitoring
- **Fallback Methods**: Alternative implementations for compatibility

### 3. Performance
- **Efficient Data Structures**: HashMap for player data storage
- **Lazy Loading**: Data loaded only when needed
- **Minimal Memory Usage**: Efficient memory management
- **Fast Operations**: O(1) heart lookups and updates

### 4. Data Persistence
- **Config Storage**: Player data stored in plugin config
- **Automatic Saving**: Data saved on plugin disable
- **UUID Support**: Proper UUID handling for player identification
- **Data Validation**: UUID validation and error handling

## 📋 Commands Summary

| Command | Permission | Description | Aliases |
|---------|------------|-------------|---------|
| `/lifesteal` | `lifesteal.use` | Main plugin command | `ls`, `lsd` |
| `/lifesteal <player> <amount>` | `LSD.addhearts` | Add hearts to player | - |
| `/lifesteal removehearts <player> <amount>` | `LSD.rmhearts` | Remove hearts from player | - |
| `/lifesteal setlimit <player> <amount>` | `LSD.setlimit` | Set player heart limit | - |
| `/lifesteal reset <player>` | `LSD.resethearts` | Reset player hearts | - |
| `/lifesteal name <name>` | `LSD.setheartname` | Set custom heart name | - |
| `/lifesteal lore <lore>` | `LSD.setheartlore` | Set custom heart lore | - |
| `/lifesteal hearts [player]` | `LSD.gethearts` | View heart balance | - |
| `/lifesteal top` | `LSD.hearttop` | View top players | - |
| `/lifesteal history [player]` | `LSD.hearthistory` | View transaction history | - |
| `/lifesteal reload` | `LSD.reload` | Reload configuration | - |
| `/hearts` | `lifesteal.hearts` | Check hearts | `h`, `heart` |
| `/withdraw` | `LSD.withdraw` | Withdraw hearts into items | `w` |
| `/deposit` | `lifesteal.deposit` | Deposit hearts from items | `d` |
| `/payhearts` | `LSD.payhearts` | Send hearts to player | `ph` |
| `/sethearts` | `lifesteal.admin` | Set player hearts | `sh` |
| `/addhearts` | `lifesteal.admin` | Add hearts to player | `ah` |
| `/removehearts` | `lifesteal.admin` | Remove hearts from player | `rh` |

## 🎯 Placeholders Available

| Placeholder | Description | Example |
|-------------|-------------|---------|
| `%lifesteal_hearts%` | Current heart count | `20` |
| `%lifesteal_max_hearts%` | Maximum hearts allowed | `40` |
| `%lifesteal_min_hearts%` | Minimum hearts allowed | `2` |
| `%lifesteal_hearts_bar%` | Visual heart bar | `❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤` |
| `%lifesteal_hearts_percentage%` | Hearts as percentage | `50.0` |
| `%lifesteal_hearts_remaining%` | Hearts until max | `20` |
| `%lifesteal_hearts_above_min%` | Hearts above minimum | `18` |

## 🔐 Permission Structure

| Permission | Default | Description |
|------------|---------|-------------|
| `lifesteal.*` | `false` | Access to all commands |
| `lifesteal.use` | `true` | Basic plugin commands |
| `lifesteal.hearts` | `true` | Check own hearts |
| `lifesteal.hearts.others` | `op` | Check other players' hearts |
| `lifesteal.withdraw` | `true` | Withdraw hearts |
| `lifesteal.deposit` | `true` | Deposit hearts |
| `lifesteal.admin` | `op` | Admin commands |
| `lifesteal.info` | `true` | View plugin info |
| `LSD.payhearts` | `true` | Send hearts to other players |
| `LSD.withdraw` | `true` | Withdraw hearts into items |
| `LSD.addhearts` | `op` | Add hearts to players |
| `LSD.rmhearts` | `op` | Remove hearts from players |
| `LSD.setlimit` | `op` | Set player heart limits |
| `LSD.resethearts` | `op` | Reset player hearts |
| `LSD.setheartname` | `op` | Set custom heart item name |
| `LSD.setheartlore` | `op` | Set custom heart item lore |
| `LSD.gethearts` | `true` | View heart balances |
| `LSD.hearttop` | `true` | View top heart players |
| `LSD.hearthistory` | `op` | View heart transaction history |
| `LSD.reload` | `op` | Reload plugin configuration |

## ⚙️ Configuration Options

### Heart Settings
- `max-hearts`: Maximum hearts (default: 40)
- `min-hearts`: Minimum hearts (default: 2)
- `custom-heart-name`: Custom name for heart items (default: "§c§l❤ Extra Heart")
- `custom-heart-lore`: Custom lore for heart items (default: "§7Right-click to consume")

### Death Settings
- `lose-heart-on-death`: Lose heart on death (default: true)
- `gain-heart-on-kill`: Gain heart on kill (default: true)
- `drop-heart-on-death`: Drop heart item on death (default: true)
- `heart-drop-chance`: Heart drop probability (default: 0.1)

### World Settings
- `enabled-worlds`: Specific worlds to enable
- `disabled-worlds`: Specific worlds to disable

### PvP Settings
- `enabled`: PvP heart stealing (default: true)
- `require-same-world`: Same world requirement (default: true)
- `require-same-gamemode`: Same gamemode requirement (default: false)

## 🚀 Installation & Usage

1. **Download** the compiled JAR file
2. **Place** in plugins folder
3. **Restart** server
4. **Configure** settings in config.yml
5. **Set permissions** for players
6. **Enjoy** the lifesteal experience!

## 🔮 Future Enhancements

- **Database Support**: MySQL/SQLite integration
- **Economy Integration**: Vault support for heart trading
- **Advanced PvP**: Team-based heart stealing
- **Heart Crafting**: Craftable heart items
- **Statistics**: Player statistics and leaderboards
- **API**: Public API for other plugins

---

**Plugin Status**: ✅ **FULLY IMPLEMENTED AND READY FOR USE**

This plugin includes all the core features from the original Lifesteal Deluxe with modern improvements, better code structure, and enhanced functionality for Spigot servers.
