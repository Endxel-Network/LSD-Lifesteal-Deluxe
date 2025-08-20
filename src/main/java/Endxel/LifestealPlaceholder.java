package Endxel;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LifestealPlaceholder extends PlaceholderExpansion {

    private final LSD plugin;

    public LifestealPlaceholder(LSD plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lifesteal";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MrWak";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        switch (params.toLowerCase()) {
            case "hearts":
                return String.valueOf(plugin.getHearts(player));
                
            case "max_hearts":
                return String.valueOf(plugin.getMaxHearts());
                
            case "min_hearts":
                return String.valueOf(plugin.getMinHearts());
                
            case "hearts_bar":
                return getHeartsBar(plugin.getHearts(player));
                
            case "hearts_percentage":
                double percentage = (double) plugin.getHearts(player) / plugin.getMaxHearts() * 100;
                return String.format("%.1f", percentage);
                
            case "hearts_remaining":
                return String.valueOf(plugin.getMaxHearts() - plugin.getHearts(player));
                
            case "hearts_above_min":
                return String.valueOf(plugin.getHearts(player) - plugin.getMinHearts());
                
            default:
                return null;
        }
    }

    private String getHeartsBar(int hearts) {
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < hearts; i++) {
            bar.append("❤");
        }
        return bar.toString();
    }
}
