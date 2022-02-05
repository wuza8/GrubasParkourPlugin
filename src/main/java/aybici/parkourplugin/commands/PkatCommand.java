package aybici.parkourplugin.commands;

import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PkatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        Player searchedPlayer = Bukkit.getPlayer(args[0]);

        if(searchedPlayer == null){
            player.sendMessage("Player with name \""+args[0]+"\" doesn't exist!");
            return false;
        }

        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(searchedPlayer).getParkour();

        if(parkour == null){
            player.sendMessage("This player doesn't play any parkour!");
            return false;
        }

        ParkourPlugin.parkourSessionSet.teleportToParkour(player, parkour.getName());
        return true;
    }
}
