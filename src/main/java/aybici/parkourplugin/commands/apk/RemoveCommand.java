package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.deletePermission)) return true;

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
