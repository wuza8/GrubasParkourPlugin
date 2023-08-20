package aybici.parkourplugin.usableblocks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UsableBlocksFacade implements Listener {

    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent event) {
        //.... this java thing is
        boolean isSomethingThere = false;

        try{
            event.getCurrentItem().getItemMeta().getLore().get(0);
            isSomethingThere = true;
        }
        catch (Exception e){}

        if(!isSomethingThere) return;

        String command = event.getCurrentItem().getItemMeta().getLore().get(0).substring(1);
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem().getItemMeta().getLore().get(0).startsWith("/")) {
            if (event.getCurrentItem() == null) {
                return;
            }

            player.performCommand(command);
        }
        else if (event.getCurrentItem().getItemMeta().getLore().get(0).startsWith("$")) {
            // Create the event here
            UsableBlockUsedEvent newEvent = new UsableBlockUsedEvent(player, command);
            // Call the event
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

}
