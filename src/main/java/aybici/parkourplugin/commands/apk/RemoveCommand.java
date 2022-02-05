package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission(ParkourPlugin.permissionSet.apkPermission)) {
            player.sendMessage(ChatColor.RED + "Nie masz dostępu do komend admin-parkour!");
            return true;
        }

        try {
            if (ParkourPlugin.parkourSet.removeParkour(args[0]))
            sender.sendMessage("Parkour with name \""+args[0]+"\" removed!");
            else sender.sendMessage("Path doesn't exist or contains not empty directory.");
        }
        catch(IllegalStateException exception){
            sender.sendMessage(exception.getMessage());
        }
        return true;
    }
}
