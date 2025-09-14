# Lifesteal Deluxe v2.6 - Changelog

## 🆕 **What's New in v2.6**

### **🌍 Multi-Language Support**
- Added support for 9 languages: English, Spanish, French, German, Italian, Portuguese, Russian, Japanese, Chinese
- New `lang.yml` file with all translations
- Language switching without server restart
- Automatic fallback to English if translation missing
- New commands: `/ls language list`, `/ls language set <lang>`, `/ls language current`

### **🔧 Enhanced Commands**
- **Added `/ls sethearts <player> <amount>`** - Set hearts for any player
- **Added `/ls setmax <amount>`** - Alias for setmaxhearts command
- **Wildcard Support** - Use `*` for all online players, `**` for all players (online + offline)
- **Enhanced Tab Completion** - Better command suggestions

### **⚙️ Configurable Command Aliases**
- New `command-aliases` section in config.yml
- Customize command shortcuts (e.g., `/ls` instead of `/lifesteal`)
- In-game alias management: `/ls aliases add/remove/list/reload`
- Dynamic alias system

### **🛡️ Improved Ban System**
- Server-only bans
- Persistent kicking system for banned players
- Better ban duration parsing (1d, 1w, 1m, etc.)
- Enhanced ban messages and warnings

### **🔌 Simple API**
- New `LifestealAPI` class for external plugins
- Easy integration with other plugins
- Methods for heart management, ban checking, settings
- Thread-safe operations

### **📁 New Files Added**
- `lang.yml` - Language translations
- `LanguageManager.java` - Handles translations
- `AliasManager.java` - Manages command aliases
- `LifestealAPI.java` - API for external plugins

### **🔧 Configuration Updates**
- Added `language: "en"` setting in config.yml
- Added `command-aliases` section
- Removed old message system (now uses language manager)
- Cleaner, more organized config structure

### **🐛 Bug Fixes**
- Fixed `/ls enable` command not working when plugin disabled
- Improved error handling and validation
- Better message formatting and color code conversion
- Enhanced plugin stability

### **📊 Performance Improvements**
- Message caching for better performance
- Optimized language loading
- Reduced memory usage
- Faster command processing

---

## **Commands Added/Updated**

| Command | Description | Permission |
|---------|-------------|------------|
| `/ls sethearts <player> <amount>` | Set player hearts | `lifesteal.admin` |
| `/ls setmax <amount>` | Set global max hearts (alias) | `lifesteal.admin` |
| `/ls language [list/set/current]` | Manage language settings | `lifesteal.admin` |
| `/ls aliases [add/remove/list/reload]` | Manage command aliases | `lifesteal.admin` |

---

## **Configuration Changes**

### **New Settings in config.yml:**
```yaml
# Language settings
language: "en" # Available: en, es, fr, de, it, pt, ru, ja, zh

# Command aliases
command-aliases:
  lifesteal: ["ls", "lsd"]
  hearts: ["h", "heart"]
  withdraw: ["w"]
  sethearts: ["sh"]
  setmax: ["sm"]
  addhearts: ["ah"]
  removehearts: ["rh"]
  payhearts: ["ph"]
```

### **Removed Settings:**
- Old message system (now in lang.yml)
- Redundant configuration options

---

## **Breaking Changes**
- None! This update is fully backward compatible
- Old configurations will work with new defaults
- All existing commands still work the same way

---

## **Migration Guide**
1. **Update plugin** - Replace old JAR with new v2.6 JAR
2. **Restart server** - New features will be available
3. **Optional: Change language** - Use `/ls language set <lang>` to change language
4. **Optional: Customize aliases** - Use `/ls aliases` to manage command shortcuts

---

**Total Files Changed:** 8 files modified, 4 new files added
**New Features:** 6 major features
**Languages Added:** 9 languages
**Commands Added:** 4 new commands
**API Methods:** 15+ new API methods