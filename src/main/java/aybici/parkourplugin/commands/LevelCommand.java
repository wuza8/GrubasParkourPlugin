package aybici.parkourplugin.commands;

import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        User user = UserManager.getUserByName(player.getName());
        player.sendMessage(ChatUtil.fixColor("&b>&a> &bTwój Poziom: " + ChatColor.GREEN + user.getLevel()));
        player.sendMessage(ChatUtil.fixColor("&b>&a> &bTwój Exp: " + ChatColor.GREEN + user.getExp()));
        player.sendMessage(ChatUtil.fixColor("&b>&a> &bDo następnego poziomu brakuje ci: " + ChatColor.GREEN + user.getNeedExp()));
        return false;
    }
}
