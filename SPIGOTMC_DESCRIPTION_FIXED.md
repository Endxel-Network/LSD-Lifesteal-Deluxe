# ❤️ Lifesteal Deluxe v2.6 - Advanced Heart Economy System

**Transform your server into a heart-based economy with this feature-rich, multi-language lifesteal plugin!**

---

## 🌟 What is Lifesteal Deluxe?

Lifesteal Deluxe is the ultimate heart-based economy plugin that turns player health into a valuable, tradeable resource. Players can gain hearts by killing others, lose hearts when they die, and use hearts as currency throughout your server. With extensive customization options, multi-language support, and powerful admin tools, this plugin creates an engaging and competitive environment for your players.

---

## ✨ Key Features

### 🎯 Core Heart System
- **Heart Stealing** - Players steal hearts when killing others
- **Heart Loss** - Players lose hearts when they die
- **Heart Trading** - Send hearts to other players with `/payhearts`
- **Heart Withdrawal** - Convert hearts into physical items
- **Heart Limits** - Configurable min/max heart limits per player

### 🌍 Multi-Language Support
- **9 Languages** - English, Spanish, French, German, Italian, Portuguese, Russian, Japanese, Chinese
- **Dynamic Switching** - Change language in-game without restart
- **Auto-Fallback** - Falls back to English if translation missing
- **Easy Management** - `/ls language` commands for admins

### 🛡️ Advanced Ban System
- **Server-Only Bans** - Only affects the lifesteal server, not your entire network
- **Temporary Bans** - Configurable ban duration (1d, 1w, 1m, etc.)
- **Persistent Kicking** - Banned players get kicked on join until ban expires
- **Revive System** - Admins can revive banned players with `/ls revive`

### 🔧 Powerful Admin Tools
- **Wildcard Commands** - Use `*` for all online players, `**` for all players
- **Heart Management** - Add, remove, set hearts for any player
- **Bulk Operations** - Manage multiple players at once
- **Real-time Monitoring** - Track heart transactions and player status

### 🎨 Customization Options
- **Custom Heart Items** - Fully customizable heart item name and lore
- **Configurable Aliases** - Set custom command shortcuts
- **Color Code Support** - Full `&` color code support throughout
- **Flexible Permissions** - Granular permission system

---

## 📋 Commands & Usage

### Player Commands
```
/hearts [player]          - Check your or another player's hearts
/withdraw <amount>        - Withdraw hearts as physical items
/payhearts <player> <amount> - Send hearts to another player
```

### Admin Commands
```
/ls sethearts <player> <amount> - Set player hearts (supports * and **)
/ls addhearts <player> <amount> - Add hearts to player
/ls removehearts <player> <amount> - Remove hearts from player
/ls givehearts <player> <amount> - Give hearts to offline players
/ls setmax <amount>       - Set global maximum hearts
/ls revive <player>       - Revive banned players
/ls language [set|list]   - Manage language settings
/ls aliases [add|remove]  - Manage command aliases
/ls reload               - Reload plugin configuration
```

### Wildcard Support
- `*` = All online players
- `**` = All players (online + offline)

**Example:** `/ls sethearts * 10` - Sets all online players to 10 hearts

---

## 🔐 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `lifesteal.*` | All permissions | op |
| `lifesteal.use` | Basic commands | true |
| `lifesteal.hearts` | Check hearts | true |
| `lifesteal.withdraw` | Withdraw hearts | true |
| `lifesteal.payhearts` | Send hearts | true |
| `lifesteal.admin` | Admin commands | op |
| `lifesteal.reload` | Reload config | op |

---

## 🌐 PlaceholderAPI Support

Use these placeholders with PlaceholderAPI-compatible plugins:

```
%lifesteal_hearts%           - Player's current hearts
%lifesteal_max_hearts%       - Maximum hearts allowed
%lifesteal_min_hearts%       - Minimum hearts allowed
%lifesteal_hearts_bar%       - Visual hearts bar
%lifesteal_hearts_percentage% - Hearts as percentage
%lifesteal_hearts_remaining% - Hearts above minimum
%lifesteal_hearts_above_min% - Hearts above minimum
```

**Perfect for:** Scoreboards, tab lists, holograms, and chat messages!

---

## ⚙️ Configuration

### Main Settings
```yaml
# Language settings
language: "en" # Available: en, es, fr, de, it, pt, ru, ja, zh

# Heart limits
max-hearts: 20
min-hearts: 1

# Heart behavior
lose-heart-on-death: true
gain-heart-on-kill: true

# Ban system
ban-system:
  enabled: true
  ban-duration: "1d"
  prevent-join: true
```

### Command Aliases
```yaml
command-aliases:
  lifesteal: ["ls", "lsd"]
  hearts: ["h", "heart"]
  withdraw: ["w"]
  sethearts: ["sh"]
  # ... and more
```

---

## 🚀 Installation

1. **Download** the plugin JAR file
2. **Place** it in your server's `plugins` folder
3. **Restart** your server
4. **Configure** settings in `config.yml`
5. **Set permissions** for your players
6. **Enjoy** your new heart economy!

---

## 🔄 API Integration

Lifesteal Deluxe includes a comprehensive API for developers:

```java
// Get the API
LifestealAPI api = lifestealPlugin.getAPI();

// Basic operations
int hearts = api.getHearts(player);
api.setHearts(player, 10);
api.addHearts(player, 5);

// Check settings
boolean heartStealing = api.isHeartStealingEnabled();
boolean isBanned = api.isPlayerBanned(player);
```

---

## 🌟 Why Choose Lifesteal Deluxe?

### For Server Owners
- ✅ **Easy Setup** - Simple configuration and installation
- ✅ **Multi-Language** - Support players from around the world
- ✅ **Flexible Permissions** - Control who can do what
- ✅ **Network Safe** - Only affects the lifesteal server
- ✅ **Regular Updates** - Active development and support

### For Players
- ✅ **Engaging Gameplay** - Adds risk and reward to PvP
- ✅ **Economic System** - Hearts become valuable currency
- ✅ **Fair Competition** - Balanced heart limits and rules
- ✅ **Clear Communication** - Messages in their language

### For Developers
- ✅ **Clean API** - Easy integration with other plugins
- ✅ **PlaceholderAPI** - Works with popular display plugins
- ✅ **Extensible** - Add custom features easily
- ✅ **Well Documented** - Clear code and documentation

---

## 📈 Performance

- **Optimized Code** - Efficient heart calculations and storage
- **Memory Management** - Smart caching and cleanup
- **Minimal Impact** - Low server resource usage
- **Scalable** - Works with any number of players

---

## 🆘 Support & Updates

- **GitHub Repository** - Full source code and issue tracking
- **Regular Updates** - New features and bug fixes
- **Community Support** - Help from other server owners
- **Documentation** - Comprehensive guides and examples

---

## 🎯 Perfect For

- **PvP Servers** - Add heart-based risk/reward
- **Economy Servers** - Use hearts as currency
- **Survival Servers** - Create unique gameplay mechanics
- **Network Servers** - Server-specific heart system
- **Custom Servers** - Flexible and configurable

---

## 📝 Changelog v2.6

### New Features
- 🌍 Multi-language support (9 languages)
- 🔧 Configurable command aliases
- 🛡️ Advanced ban system with server-only kicks
- ⚡ Wildcard support for bulk operations
- 🔌 Simple API for external plugins
- 📊 Enhanced admin tools and monitoring

### Improvements
- 🎨 Better message system with placeholders
- ⚙️ More configuration options
- 🔐 Improved permission system
- 📱 Better tab completion
- 🐛 Bug fixes and optimizations

---

## ❤️ Run your server with love – with Lifesteal Deluxe! ❤️

**Download now and transform your server into an engaging heart-based economy!**

---

*Made with ❤️ for the Minecraft community*
