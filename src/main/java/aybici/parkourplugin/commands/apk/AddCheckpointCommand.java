package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCheckpointCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (!SenderHasPermission(sender)) return true;
        if (!isPlayerOnParkour(player)) return true;
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        session.staticCheckpoint.add(player.getLocation().getBlock().getLocation());
        return true;
    }
}