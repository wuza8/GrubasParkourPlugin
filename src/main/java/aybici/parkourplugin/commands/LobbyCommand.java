package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            ParkourPlugin.lobby.teleportPlayerToLobby((Player) sender);
            sender.sendMessage(ChatColor.GOLD + "Przeteleportowano na lobby.");
            return true;
        }
        if (sender.hasPermission(ParkourPlugin.permissionSet.tpAllToLobbyPermission)) {
            if (args[0].equals("tpall")) {
                ParkourPlugin.lobby.teleportAllPlayersToLobby();
                sender.sendMessage("Teleportowano wszystkich graczy na lobby.");
                return true;
            }
            return false;
        }
        sender.sendMessage(ChatColor.RED + "Nie masz permisji, żeby wykonywac inne komendy związane z lobby.");
        return true;
    }
}