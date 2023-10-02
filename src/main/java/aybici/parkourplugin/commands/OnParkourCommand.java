package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class OnParkourCommand {
    protected boolean isPlayerOnParkour(Player player){
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        if(!session.isPlayerOnParkour()){
            player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + ChatColor.WHITE+ "Musisz dołączyć na mapę, aby użyć tej funkcji.");
            return false;
        }
        return true;
    }

}
