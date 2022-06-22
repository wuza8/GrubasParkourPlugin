package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetIdentifier extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(player).getParkour();

        if (!SenderHasPermission(sender)) return true;
        if(!isPlayerOnParkour(player)) return true;

        if (args.length != 1) return false;
        int identifier = Integer.parseInt(args[0]);

        if (ParkourPlugin.parkourSet.categoryContainsIdentifier(parkour.getCategory(), identifier)){
            player.sendMessage("Category contains ID yet!");
            return false;
        }
        parkour.setIdentifier(identifier);
        player.sendMessage("Changed ID to " + parkour.getIdentifier());
        return true;
    }
}
