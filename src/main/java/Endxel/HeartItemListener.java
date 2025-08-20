package Endxel;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HeartItemListener implements Listener {

    private final LSD plugin;

    public HeartItemListener(LSD plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !plugin.getHeartItemManager().isHeartItem(item)) {
            return;
        }
        
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            
            if (plugin.getHearts(player) >= plugin.getMaxHearts()) {
                player.sendMessage("§cYou already have the maximum number of hearts!");
                return;
            }
            
            plugin.addHearts(player, 1);
            player.sendMessage("§a+1 Heart! You now have " + plugin.getHearts(player) + " hearts.");
            
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
        }
    }
}
