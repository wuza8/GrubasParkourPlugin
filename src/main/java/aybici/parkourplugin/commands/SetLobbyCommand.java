package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission(ParkourPlugin.permissionSet.setLobbyPermission)){
            player.sendMessage(ChatColor.RED + "Nie masz permisji, żeby ustawić lobby!");
            return true;
        }
        ParkourPlugin.lobby.setLobbyLocation(player.getLocation());
        player.sendMessage("Ustawiono lokalizację lobby na: " + ParkourPlugin.lobby.getLobbyLocation().toString());
        return true;
    }
}