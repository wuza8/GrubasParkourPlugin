package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.sessions.PositionSaver;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowLastTryCommand extends OnParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        /*if (!isPlayerOnParkour(player)) return true;
        int slowMotion = 1;
        if (args.length > 0){
            slowMotion = Integer.parseInt(args[0]);
            if (slowMotion > 5){
                player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + "Za duże spowolnienie!");
                return true;
            }
        }
        ParkourPlugin.positionSaver.playDemo(player, PositionSaver.playerDemosHashMap.get(player), slowMotion);*/
        player.sendMessage(ChatUtil.fixColor("&b>&a> &bKomenda tymczasowo wyłączona do rozwiązania błędu z czasami! Przepraszamy za utrudnienia"));
        return true;
    }
}