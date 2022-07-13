package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.ExpManager;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetExpCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(player).getParkour();

        if (!SenderHasPermission(sender)) return true;
        if (!isPlayerOnParkour(player)) return true;
        if(args.length == 1){
            int exp;
            if(args[0].equals("calculate")){
                exp = ExpManager.calculateExpOfParkour(parkour);
            }
            else {
                exp = Integer.parseInt(args[0]);
            }
            parkour.setExp(exp);
            player.sendMessage("Ustawiono " + ChatColor.BLUE + exp + ChatColor.WHITE + " expa za tę mapę.");
            return true;
        }
        return false;
    }
}
