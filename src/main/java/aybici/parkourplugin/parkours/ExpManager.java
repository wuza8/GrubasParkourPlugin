package aybici.parkourplugin.parkours;

import aybici.parkourplugin.LevelFile;
import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserFile;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class ExpManager {
    public static LevelFile levelFile = LevelFile.getInstance();
    public static void calculateExpOfParkour(Parkour parkour, boolean refreshPlayersExp){
        final int SAMPLE_SIZE = 5;
        final int roundToUpBy = 5;
        int topListSize = parkour.getTopListObject().getTopList().size();
        if(topListSize < SAMPLE_SIZE) {
            if(parkour.finishExpSource == FinishExpSource.DEFAULT) return;
            Bukkit.getLogger().info("Too small sample amount: parkour " + parkour.getName() + " " + parkour.getCategory() + " " + parkour.getIdentifier());
            parkour.finishExpSource = FinishExpSource.DEFAULT;
            parkour.setExp(0, refreshPlayersExp);
            return;
        }
        List<TopLine> oldestTopList = TopListDisplay.sortTopList(parkour.getTopListObject().getTopList() , SortTimesType.DATE).subList(0,SAMPLE_SIZE);
        TopLine bestOldTopLine = TopListDisplay.getBestTime(oldestTopList);
        long time = bestOldTopLine.playerTime;
        parkour.finishExpSource = FinishExpSource.GENERATED;
        int multiplier = ParkourCategory.getExpMultiplier(parkour.getCategory());
        int exp = (((int)(time/1000)/roundToUpBy) + 1)*roundToUpBy * multiplier;
        parkour.setExp(exp, refreshPlayersExp);
    }

    // ta metoda nie tworzy nowych użytkowników User, koryguje jedynie statystyki już istniejących
    // oraz że ich statystyki są poprawnie policzone
    // wykonuje się ją bezpośrednio przed zmianą expa na mapie
    public static void refreshExpOfPlayers(Parkour parkour, int newExp){
        int oldExp = parkour.getExp();
        List<OfflinePlayer> playerList = TopListDisplay.getAllPlayersOfTop(parkour.getTopListObject().getTopList());
        for(OfflinePlayer player : playerList){
            if(!UserManager.containsUser(player.getName())) {
                Bukkit.getLogger().info("KRYTYCZNY BLAD, BRAK STATYSTYK GRACZA " + player.getName() + " a ma topke na parkourze " + parkour.getName());
            } else{
                User user = UserManager.getUserByName(player.getName());
                int timesFinished = TopListDisplay.getAllTimesOfPlayer(player,parkour.getTopListObject().getTopList()).size();
                user.addExp(- timesFinished * oldExp);
                user.addExp(timesFinished * newExp);
                UserFile.levelFile.getData().set("Users." + user.getNick() + ".Exp", user.getExp());
                UserFile.levelFile.getData().set("Users." + user.getNick() + ".Level", user.getLevel());
            }
        }
        UserFile.levelFile.saveData();
    }

    // zebranie statystyk z dawnych topek
    public static void createNonExistingUsersOfParkour(Parkour parkour){
        List<OfflinePlayer> playerList = TopListDisplay.getAllPlayersOfTop(parkour.getTopListObject().getTopList());
        for(OfflinePlayer player : playerList) {
            if(!UserManager.containsUser(player.getName()))
                UserManager.createUser(player.getName());
        }
    }

    // zebranie statystyk z dawnych topek
    public static void giveUsersExpForParkour(Parkour parkour){
        List<OfflinePlayer> playerList = TopListDisplay.getAllPlayersOfTop(parkour.getTopListObject().getTopList());
        for(OfflinePlayer player : playerList) {
            User user = UserManager.getUserByName(player.getName());
            int timesFinished = TopListDisplay.getAllTimesOfPlayer(player,parkour.getTopListObject().getTopList()).size();
            user.addExp(timesFinished * parkour.getExp());
            UserFile.levelFile.getData().set("Users." + user.getNick() + ".Exp", user.getExp());
            UserFile.levelFile.getData().set("Users." + user.getNick() + ".Level", user.getLevel());
        }
    }

    // zebranie statystyk z dawnych topek
    public static void restoreExpFromTopLists(Boolean calculateFinishExpIfNotSet){
        int allCompletions = 0;
        UserManager.resetAllUsers();
        for(Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
            createNonExistingUsersOfParkour(parkour);
            allCompletions += parkour.getTopListObject().getTopList().size();
            if(calculateFinishExpIfNotSet)
                if(!parkour.finishExpSource.equals(FinishExpSource.SET))
                    calculateExpOfParkour(parkour, false);
            giveUsersExpForParkour(parkour);
        }
        UserFile.levelFile.saveData();
        Bukkit.getLogger().info("Liczba przejsc wszystkich graczy na wszystkich parkourach: " + allCompletions);
        Bukkit.getLogger().info("Dziekujemy za gre na serwerze ! ! <3");
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
