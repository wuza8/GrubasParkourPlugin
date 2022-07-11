package aybici.parkourplugin.listeners;

import aybici.parkourplugin.users.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        String format = event.getFormat().replace("{LVL}", String.valueOf(new User(event.getPlayer().getName()).getLevel()));
        event.setFormat(format);
    }
}
