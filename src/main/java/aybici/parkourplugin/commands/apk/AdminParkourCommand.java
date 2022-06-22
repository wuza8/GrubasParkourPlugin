package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.commands.OnParkourCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class AdminParkourCommand extends OnParkourCommand {
    protected boolean SenderHasPermission(CommandSender sender){
        if (!sender.hasPermission(ParkourPlugin.permissionSet.apkPermission)) {
            sender.sendMessage(ChatColor.RED + "Nie masz dostępu do komend admin-parkour!");
            return false;
        }return true;
    }
}
