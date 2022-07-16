package aybici.parkourplugin.listeners;

import aybici.parkourplugin.users.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler//(priority = EventPriority.HIGHEST) chyba nie potrzebne
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String format = event.getFormat();

        int number = 1;
        String formatPart1 = format.substring(0, number+1);
        String formatPart2 = format.substring(number+1);
        formatPart1 += "[" + ChatColor.YELLOW + UserManager.getUserByName(player.getName()).getLevel() + ChatColor.WHITE + "]";
        format = formatPart1 + formatPart2;
        event.setFormat(format);

    }
}
