package aybici.parkourplugin.commands;

import aybici.parkourplugin.commands.arguments.ArgumentManager;
import aybici.parkourplugin.commands.arguments.DominantStringArgument;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.sessions.PositionSaver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PkatCommand implements CommandExecutor {
    private DominantStringArgument playerName;
    private boolean parseArgs(String[] args){
        playerName = new DominantStringArgument(false);
        ArgumentManager argumentManager = new ArgumentManager();
        argumentManager.addArgument(playerName);
        return argumentManager.parseAllArgs(args);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if(!parseArgs(args)){
            player.sendMessage(ChatColor.GREEN + "Usage:\n" + ChatColor.DARK_GREEN +
                    " /pkat"+ ChatColor.GRAY+" - teleports to start of the parkour\n" + ChatColor.DARK_GREEN +
                    " /pkat [player]"+ ChatColor.GRAY+" - teleports to parkour playing by specific player");
            return true;
        }
        Player searchedPlayer;
        if(playerName.isSpecified()) {
            searchedPlayer = Bukkit.getPlayer(playerName.getValue());
            if(searchedPlayer == null){
                player.sendMessage("Player with name \""+playerName.getValue()+"\" doesn't exist!");
                return true;
            }
        }
        else searchedPlayer = player;

        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(searchedPlayer).getParkour();

        ParkourPlugin.parkourSessionSet.teleportToParkour(player, parkour.getName());
        return true;
    }
}
