package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.commands.OnParkourCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public abstract class AdminParkourCommand extends OnParkourCommand {
    public static boolean SenderHasPermission(CommandSender sender, Permission permission){
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "Nie masz dostępu do komendy bez permisji " + permission);
            return false;
        } return true;
    }
}
