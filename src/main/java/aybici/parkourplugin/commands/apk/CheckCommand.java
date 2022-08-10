package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.TopListDisplay;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.deletePermission)) return true;
        boolean showMapNames = args.length == 2 && args[1].equals("showMapNames");
        if (args.length == 1 || args.length == 2){
            int completedMaps = 0;
            int completedDifferentMaps = 0;
            StringBuilder parkourList = new StringBuilder();
            for (Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
                int size = TopListDisplay.getAllTimesOfPlayer(Bukkit.getOfflinePlayer(args[0]),parkour.getTopListObject().getTopList()).size();
                completedMaps += size;
                if(size != 0) {
                    completedDifferentMaps++;
                    if(showMapNames)
                        parkourList.append(parkour.getName()).append(",");
                }
            }
            if (showMapNames)
                sender.sendMessage("Nazwy wszystkich map: " + ChatColor.GRAY + parkourList);
            sender.sendMessage("Ukończone parkoury gracza "+args[0]+": " + completedMaps);
            sender.sendMessage("Ukończone różne parkoury: " + completedDifferentMaps);
        }
        return false;
    }
}
