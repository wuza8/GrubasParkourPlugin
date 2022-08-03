package aybici.parkourplugin.utils;

import aybici.parkourplugin.users.UserManager;
import com.connorlinfoot.titleapi.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabUtil {
    public static void refreshTab(Player player){
        refreshTab(player, Bukkit.getOnlinePlayers().size());
    }
    public static void refreshTab(Player player, int onlinePlayersNumber){
            TitleAPI.sendTabTitle(player,
                    ChatUtil.hex("#ff0000G#ff420ar#ff8413u#ffc20ab#ffff00a#80ff00s#00ff00K#00ff80r#00ffffa#5c81fff#b803fft\n" +
                            "&7Graczy Online: &e" + onlinePlayersNumber + "&7/&e" + Bukkit.getMaxPlayers()),
                    ChatUtil.fixColor("&7Witaj, &e" + player.getName() +
                            "\n&7Twój poziom &e" + UserManager.getUserByName(player.getName()).getLevel() +
                            "\n&7Exp do następnego poziomu: &e" + UserManager.getUserByName(player.getName()).getNeedExp() +
                            "\n&7Baw się dobrze :)"));
    }
    public static void refreshAllPlayersTab(){
        for(Player player : Bukkit.getOnlinePlayers())
            refreshTab(player);
    }
}
