package Endxel;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class HeartItemManager {

    private final LSD plugin;
    private final NamespacedKey heartItemKey;

    public HeartItemManager(LSD plugin) {
        this.plugin = plugin;
        this.heartItemKey = new NamespacedKey(plugin, "lifesteal_heart_item");
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
            
            // Add custom NBT tag to identify this as a heart item
            meta.getPersistentDataContainer().set(heartItemKey, PersistentDataType.BOOLEAN, true);
            
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
        if (meta == null) {
            return false;
        }
        
        // Check for the custom NBT tag instead of display name
        return meta.getPersistentDataContainer().has(heartItemKey, PersistentDataType.BOOLEAN);
    }

    public void giveHeartItem(org.bukkit.entity.Player player, int amount) {
        ItemStack heartItem = createHeartItem();
        heartItem.setAmount(amount);
        player.getInventory().addItem(heartItem);
        player.sendMessage("§aReceived " + amount + " heart item(s)!");
    }
}
