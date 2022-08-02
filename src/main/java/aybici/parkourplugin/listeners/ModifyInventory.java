package aybici.parkourplugin.listeners;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ModifyInventory implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!event.getWhoClicked().hasPermission("parkour.modifyinventory"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(final PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(!player.hasPermission("parkour.modifyinventory")){
            event.setCancelled(true);
            player.getInventory().clear();
            new BukkitRunnable(){
                public void run(){
                    player.getInventory().clear();
                    JoinListener.setItems(player);
                }
            }.runTaskLater((Plugin) ParkourPlugin.getInstance(), 1L);
        }
    }
}
