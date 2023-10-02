package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.sessions.PositionSaver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class PlayDemoCommand extends OnParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (!isPlayerOnParkour(player)) return true;
        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(player).getParkour();

        if (args.length == 0) return false;
        String bestPlayerName = args[0];
        int slowMotion = 1;

        if (args.length > 1){
            slowMotion = Integer.parseInt(args[1]);
            if (slowMotion > 5){
                player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + "Za duże spowolnienie!");
                return false;
            }
        }

        if (args[0].equals("best")){

            OfflinePlayer demoPlayer = PositionSaver.getBestDemoPlayer(parkour);
            if (demoPlayer == null){
                player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + "Brak żadnych demek graczy, bądź pierwszy!");
                return true;
            }
            bestPlayerName = demoPlayer.getName();
            player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + ChatColor.GRAY + "Autorem przejścia jest " + ChatColor.GOLD + bestPlayerName);

        } else if (!new File(parkour.folderName + File.separator + "demos"+File.separator + args[0] + ".txt").exists()) {
            player.sendMessage("Brak zapisanego przejścia tego gracza");
            return true;
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"skin set " + player.getName() +" "+ bestPlayerName);
        ParkourPlugin.positionSaver.playDemo(player,ParkourPlugin.positionSaver.getDemo(Bukkit.getOfflinePlayer(bestPlayerName),
                parkour), slowMotion);
        return true;
    }

}