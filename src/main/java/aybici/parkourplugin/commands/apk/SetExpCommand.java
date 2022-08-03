package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.ExpManager;
import aybici.parkourplugin.parkours.FinishExpSource;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.ParkourCategory;
import aybici.parkourplugin.utils.TabUtil;
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
        if(args.length == 1) {
            long exp;
            if (args[0].equals("calculate")) {
                ExpManager.calculateExpOfParkour(parkour, true);
            } else {
                exp = Long.parseLong(args[0]);
                parkour.finishExpSource = FinishExpSource.SET;
                parkour.setExp(exp, true);
            }
            TabUtil.refreshAllPlayersTab();
            player.sendMessage("Ustawiono " + ChatColor.BLUE + parkour.getExp() + ChatColor.WHITE + " expa za tę mapę.");
            return true;
        }
        if(args.length == 2){
            long exp;
            double time = Double.parseDouble(args[1]);
            if (args[0].equals("calculate")) {
                exp = Math.round(time * Math.pow(1.2, (time)/20.0) * ParkourCategory.getExpMultiplier(parkour.getCategory()));
                parkour.finishExpSource = FinishExpSource.SET;
                parkour.setExp(exp, true);
                TabUtil.refreshAllPlayersTab();
                player.sendMessage("Ustawiono " + ChatColor.BLUE + parkour.getExp() + ChatColor.WHITE + " expa za tę mapę.");
                return true;
            } else return false;
        }
        return false;
    }
}
