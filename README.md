# Lifesteal Deluxe

genreated by chatgpt beacuse yeah

## Commands

### Player Commands

| Command | Permission | Description | Usage |
|---------|------------|-------------|-------|
| `/hearts` | `lifesteal.hearts` | Check your hearts | `/hearts [player]` |
| `/withdraw` | `LSD.withdraw` | Withdraw hearts into items | `/withdraw <amount>` |
| `/deposit` | `lifesteal.deposit` | Deposit hearts from items | `/deposit <amount>` |
| `/payhearts` | `LSD.payhearts` | Send hearts to another player | `/payhearts <player> <amount>` |

### Admin Commands

| Command | Permission | Description | Usage |
|---------|------------|-------------|-------|
| `/lifesteal` | `lifesteal.use` | Main plugin command | `/lifesteal [help\|info\|reload]` |
| `/lifesteal <player> <amount>` | `LSD.addhearts` | Add hearts to player | `/lifesteal <player> <amount>` |
| `/lifesteal removehearts <player> <amount>` | `LSD.rmhearts` | Remove hearts from player | `/lifesteal removehearts <player> <amount>` |
| `/lifesteal setlimit <player> <amount>` | `LSD.setlimit` | Set max heart limit for player | `/lifesteal setlimit <player> <amount>` |
| `/lifesteal reset <player>` | `LSD.resethearts` | Reset player hearts to default | `/lifesteal reset <player>` |
| `/lifesteal name <name>` | `LSD.setheartname` | Set custom heart item name | `/lifesteal name <name>` |
| `/lifesteal lore <lore>` | `LSD.setheartlore` | Set custom heart item lore | `/lifesteal lore <lore>` |
| `/lifesteal hearts [player]` | `LSD.gethearts` | View heart balance | `/lifesteal hearts [player]` |
| `/lifesteal top` | `LSD.hearttop` | View top heart players | `/lifesteal top` |
| `/lifesteal history [player]` | `LSD.hearthistory` | View heart transaction history | `/lifesteal history [player]` |
| `/lifesteal reload` | `LSD.reload` | Reload plugin configuration | `/lifesteal reload` |
| `/sethearts` | `lifesteal.admin` | Set player hearts | `/sethearts <player> <amount>` |
| `/addhearts` | `lifesteal.admin` | Add hearts to player | `/addhearts <player> <amount>` |
| `/removehearts` | `lifesteal.admin` | Remove hearts from player | `/removehearts <player> <amount>` |

### Command Aliases

- `/lifesteal` → `/ls`, `/lsd`
- `/hearts` → `/h`, `/heart`
- `/withdraw` → `/w`
- `/deposit` → `/d`
- `/sethearts` → `/sh`
- `/addhearts` → `/ah`
- `/removehearts` → `/rh`

## Placeholders

The plugin provides the following PlaceholderAPI placeholders:

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `%lifesteal_hearts%` | Current heart count | `20` |
| `%lifesteal_max_hearts%` | Maximum hearts allowed | `40` |
| `%lifesteal_min_hearts%` | Minimum hearts allowed | `2` |
| `%lifesteal_hearts_bar%` | Visual heart bar | `❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤` |
| `%lifesteal_hearts_percentage%` | Hearts as percentage | `50.0` |
| `%lifesteal_hearts_remaining%` | Hearts until max | `20` |
| `%lifesteal_hearts_above_min%` | Hearts above minimum | `18` |

## Permissions

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

## Configuration

### Heart Settings

```yaml
# Heart Settings (in full hearts)
max-hearts: 20          # Maximum hearts a player can have
min-hearts: 1           # Minimum hearts a player can have

# Custom Heart Item Settings
custom-heart-name: "§c§l❤ Extra Heart"  # Custom name for heart items
custom-heart-lore: "§7Right-click to consume"  # Custom lore for heart items
```

### Bulk Operations

The plugin supports bulk operations using special player names:
- `*` - Targets all currently online players
- `**` - Targets all players who have ever played on the server

**Examples:**
- `/lifesteal * 5` - Add 5 hearts to all online players
- `/lifesteal ** 10` - Add 10 hearts to all players ever
- `/lifesteal reset *` - Reset all online players to 10 hearts

### Death Settings

```yaml
# Death Settings
lose-heart-on-death: true    # Whether players lose hearts on death
gain-heart-on-kill: true     # Whether killers gain hearts
drop-heart-on-death: true    # Whether hearts drop on death
heart-drop-chance: 0.1       # Chance of heart dropping (0.0-1.0)
```

### World Settings

```yaml
# World Settings
worlds:
  enabled-worlds: []     # Specific worlds to enable (empty = all)
  disabled-worlds: []    # Specific worlds to disable
```

### PvP Settings

```yaml
# PvP Settings
pvp:
  enabled: true                    # Whether PvP heart stealing is enabled
  require-same-world: true         # Same world requirement
  require-same-gamemode: false     # Same gamemode requirement
```

## Installation

1. **Download** the plugin JAR file
2. **Place** it in your server's `plugins` folder
3. **Restart** your server
4. **Configure** the plugin in `plugins/Lifesteal-Deluxe/config.yml`
5. **Set permissions** for your players

## Dependencies

- **Required**: Spigot 1.21+
- **Optional**: PlaceholderAPI (for placeholders)
- **Optional**: Vault (for economy integration)

## Building from Source

```bash
# Clone the repository
git clone <repository-url>
cd Lifesteal-Deluxe

# Build with Maven
mvn clean package

# The JAR will be in the target/ folder
```

## Support

- **Author**: MrWak
- **Website**: https://endxel.com
- **Version**: 2.25

## License

This plugin is created for play.endxel.com and follows the original Lifesteal Deluxe license.

## Changelog

### Version 2.25
- Complete rework of the plugin
- Added PlaceholderAPI support
- Improved command system
- Better configuration options
- Enhanced permission system
- Fixed various bugs and issues

---

**Note**: This plugin is designed for Spigot servers and includes all the features from the original Lifesteal Deluxe plugin with improvements and bug fixes.
