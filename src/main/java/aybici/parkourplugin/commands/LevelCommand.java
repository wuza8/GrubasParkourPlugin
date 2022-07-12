package aybici.parkourplugin.commands;

import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        User user = UserManager.getUserByName(player.getName());
        player.sendMessage(ChatUtil.fixColor("&bTwój Poziom: " + user.getLevel()));
        player.sendMessage(ChatUtil.fixColor("&bTwój Exp: " + user.getExp()));
        player.sendMessage(ChatUtil.fixColor("&bDo następnego poziomu brakuje ci: " + user.getNeedExp()));
        return false;
    }
}
