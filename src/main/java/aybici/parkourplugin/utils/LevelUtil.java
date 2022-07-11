package aybici.parkourplugin.utils;

import aybici.parkourplugin.LevelFile;
import aybici.parkourplugin.users.User;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LevelUtil {

    public static LevelFile levelFile = LevelFile.getInstance();

    public static void levelup(Player player){
        User user = User.getUserByName(player.getName());
        while(user.getNeedExp() < 1){
            int level = user.getLevel();
            ++level;
            user.setLevel(level);
            levelFile.getData().set("Users." + player.getName() + ".Level", level);
            levelFile.saveData();
            player.sendMessage(ChatUtil.fixColor("&bAwansowałeś na " + level + " poziom!"));
            Bukkit.broadcastMessage(ChatUtil.fixColor("&bGracz " + player.getName() + " awansował na " + level + " poziom!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
}
