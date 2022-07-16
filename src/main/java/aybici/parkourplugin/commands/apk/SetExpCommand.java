package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.ExpManager;
import aybici.parkourplugin.parkours.FinishExpSource;
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

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;
        if (!isPlayerOnParkour(player)) return true;
        if(args.length == 1){
            int exp;
            if(args[0].equals("calculate")){
                ExpManager.calculateExpOfParkour(parkour, true);
            }
            else {
                exp = Integer.parseInt(args[0]);
                parkour.finishExpSource = FinishExpSource.SET;
                parkour.setExp(exp, true);
            }
            player.sendMessage("Ustawiono " + ChatColor.BLUE + parkour.getExp() + ChatColor.WHITE + " expa za tę mapę.");
            return true;
        }
        return false;
    }
}
