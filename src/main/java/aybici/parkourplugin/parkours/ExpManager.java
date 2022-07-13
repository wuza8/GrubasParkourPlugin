package aybici.parkourplugin.parkours;

import aybici.parkourplugin.LevelFile;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class ExpManager {
    public static LevelFile levelFile = LevelFile.getInstance();
    public static int calculateExpOfParkour(Parkour parkour){
        final int SAMPLE_SIZE = 5;
        final int roundToUpBy = 5;
        int topListSize = parkour.getTopListObject().getTopList().size();
        if(topListSize < SAMPLE_SIZE) {
            Bukkit.getLogger().info("Too small sample amount: parkour " + parkour.getName() + " " + parkour.getCategory() + " " + parkour.getIdentifier());
            return 0;
        }
        List<TopLine> oldestTopList = TopListDisplay.sortTopList(parkour.getTopListObject().getTopList() , SortTimesType.DATE).subList(0,SAMPLE_SIZE);
        TopLine bestOldTopLine = TopListDisplay.getBestTime(oldestTopList);
        long time = bestOldTopLine.playerTime;
        int multiplier = ParkourCategory.getExpMultiplier(parkour.getCategory());
        return (((int)(time/1000)/roundToUpBy) + 1)*roundToUpBy * multiplier;
    }

    public static void levelUp(Player player){
        User user = UserManager.getUserByName(player.getName());
        while(user.getNeedExp() < 1){
            int level = user.getLevel();
            ++level;
            user.setLevel(level);
            player.sendMessage(ChatUtil.fixColor("&bAwansowałeś na " + level + " poziom!"));
            Bukkit.broadcastMessage(ChatUtil.fixColor("&bGracz " + player.getName() + " awansował na " + level + " poziom!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
        user.saveUser();
    }

}
