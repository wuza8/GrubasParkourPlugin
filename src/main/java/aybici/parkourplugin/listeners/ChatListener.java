package aybici.parkourplugin.listeners;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private ParkourPlugin instance;

    public ChatListener(ParkourPlugin instance){
        this.instance = instance;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        User user = UserManager.getUserByName(player.getName());
        String cheaterPrefix = "";
        if(user.isCheater()) cheaterPrefix = " &4[Cheater]";

        if(player.hasPermission("vipman")) cheaterPrefix += " &eViper";
        /*String format = event.getFormat();

        int number = 1;
        String formatPart1 = format.substring(0, number+1);
        String formatPart2 = format.substring(number+1);
        formatPart1 += "[" + ChatColor.YELLOW + UserManager.getUserByName(player.getName()).getLevel() + ChatColor.WHITE + "]";
        format = formatPart1 + formatPart2;
        World world = Bukkit.getWorld("");*/
        event.setFormat(ChatColor.YELLOW +ChatUtil.fixColor(UserManager.tiltes[user.getLevel() - 1] + "&8"+cheaterPrefix+"&8&f" + "&8 "+ ChatColor.DARK_RED + "%s&8: &f%s"));
    }
}
