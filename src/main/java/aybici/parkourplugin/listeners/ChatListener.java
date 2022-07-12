package aybici.parkourplugin.listeners;

import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){ // nie zamienia prefixa dalej
        String format = event.getFormat().replace("{LVL}", String.valueOf(UserManager.getUserByName(event.getPlayer().getName()).getLevel()));
        event.setFormat(format);
    }
}
