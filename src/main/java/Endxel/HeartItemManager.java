package Endxel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HeartItemManager {

    private final LSD plugin;

    public HeartItemManager(LSD plugin) {
        this.plugin = plugin;
    }

    public ItemStack createHeartItem() {
        ItemStack heart = new ItemStack(Material.REDSTONE);
        ItemMeta meta = heart.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(plugin.getCustomHeartName());
            List<String> lore = new ArrayList<>(plugin.getCustomHeartLore());
            lore.add("");
            lore.add("§eClick to use");
            meta.setLore(lore);
            
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            
            heart.setItemMeta(meta);
        }
        
        return heart;
    }

    public boolean isHeartItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        
        return meta.getDisplayName().contains("❤ Extra Heart");
    }

    public void giveHeartItem(org.bukkit.entity.Player player, int amount) {
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(createHeartItem());
        }
        player.sendMessage("§aReceived " + amount + " heart item(s)!");
    }
}
