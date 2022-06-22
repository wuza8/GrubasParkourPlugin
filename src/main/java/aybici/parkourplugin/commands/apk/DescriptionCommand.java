package aybici.parkourplugin.commands.apk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DescriptionCommand extends AdminParkourCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!SenderHasPermission(sender)) return true;
        if (!isPlayerOnParkour((Player) sender)) return true;

        return false;
    }
}
