package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowLastTryCommand extends OnParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (!isPlayerOnParkour(player)) return true;
        int slowMotion = 1;
        if (args.length > 0){
            slowMotion = Integer.parseInt(args[0]);
            if (slowMotion > 5){
                player.sendMessage("Za duże spowolnienie!");
                return false;
            }
        }
        ParkourPlugin.positionSaver.playDemo(player,ParkourPlugin.positionSaver.playerDemosHashMap.get(player), slowMotion);
        return true;
    }
}