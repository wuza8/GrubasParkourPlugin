package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkourevents.ParkourEventsFacade;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

public class EventCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(ParkourEventsFacade.getEventParkour() != null){
            Player player = (Player) sender;
            player.performCommand("pk "+ParkourEventsFacade.getEventParkour().getName());
            sender.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + ChatColor.GREEN+"Przeteleportowano na event!");
            return true;
        }
        else{
            sender.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + ChatColor.RED+"Event jest nieaktywny!");
            return false;
        }
    }
}