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

        if (event.getCurrentItem() == null) {
            return;
        }

        try{
            event.getCurrentItem().getItemMeta().getLore().get(0);
            isSomethingThere = true;
        }
        catch (Exception e){}

        if(!isSomethingThere) return;

        String command = event.getCurrentItem().getItemMeta().getLore().get(0);
        Player player = (Player) event.getWhoClicked();

        sendCommand(command, player);
    }

    public static void sendCommand(String command, Player player) {
        if(!command.startsWith("/") && !command.startsWith("$")) return;

        if (command.startsWith("/")) {
            player.performCommand(command.substring(1));
        }
        else {
            // Create the event here
            UsableBlockUsedEvent newEvent = new UsableBlockUsedEvent(player, command);
            // Call the event
            Bukkit.getServer().getPluginManager().callEvent(newEvent);
        }
    }

}
