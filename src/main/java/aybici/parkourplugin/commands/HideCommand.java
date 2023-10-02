package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (args.length != 1) return false;
        if (args[0].equals("on")){
            for(Player otherPlayer : Bukkit.getServer().getOnlinePlayers()){
                player.hidePlayer(ParkourPlugin.getInstance(),otherPlayer);
            }
            player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + ChatColor.WHITE+ "Gracze zostali ukryci.");
            return true;
        } else if (args[0].equals("off")){
            for(Player otherPlayer : Bukkit.getServer().getOnlinePlayers()){
                player.showPlayer(ParkourPlugin.getInstance(),otherPlayer);
            }
            player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + ChatColor.WHITE+ "Gracze zostali pokazani.");
            return true;
        }
        return false;
    }
}
