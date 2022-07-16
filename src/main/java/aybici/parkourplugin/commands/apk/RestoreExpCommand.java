package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.ExpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestoreExpCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.deletePermission)) return true;
        if(!player.getName().equals("rycerz125")){
            player.sendMessage("Nie możesz tego zrobic, tylko rycerz może");
        }
        Thread thread = new Thread(() -> ExpManager.restoreExpFromTopLists(Boolean.parseBoolean(args[0])));
        thread.start();
        return true;
    }
}
