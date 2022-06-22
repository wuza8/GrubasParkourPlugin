package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class OnParkourCommand {
    protected boolean isPlayerOnParkour(Player player){
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        if(!session.isPlayerOnParkour()){
            player.sendMessage(ChatColor.GRAY + "You need to join parkour to use this command!");
            return false;
        }
        return true;
    }

}
