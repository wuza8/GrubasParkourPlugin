package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkourevents.ParkourEventsFacade;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartEventCommand extends OnParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission(ParkourPlugin.permissionSet.configureParkourPermission)){
            player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN + "> " + ChatColor.RED + "Nie odpalisz eventu, bo nie masz permisji!");
            return true;
        }
        ParkourEventsFacade.startEvent();
        return true;
    }
}
