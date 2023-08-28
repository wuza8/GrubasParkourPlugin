package aybici.parkourplugin.parkours;

import aybici.parkourplugin.LevelFile;
import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserFile;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class ExpManager {
    public static LevelFile levelFile = LevelFile.getInstance();
    public static void calculateExpOfParkour(Parkour parkour, boolean refreshPlayersExp){
        final int SAMPLE_SIZE = 5;
        final long MAX_GENERATED_EXP = 5000;
        int topListSize = parkour.getTopListObject().getTopList(false,true,true).size();
        if(topListSize < SAMPLE_SIZE) {
            if(parkour.finishExpSource == FinishExpSource.DEFAULT) return;
            Bukkit.getLogger().info("Too small sample amount: parkour " + parkour.getName() + " " + parkour.getCategory() + " " + parkour.getIdentifier());
            parkour.finishExpSource = FinishExpSource.DEFAULT;
            parkour.setExp(0, refreshPlayersExp);
            return;
        }
        List<TopLine> oldestTopList = TopListDisplay.sortTopList(parkour.getTopListObject().getTopList(false,true,true) , SortTimesType.DATE).subList(0,SAMPLE_SIZE);
        TopLine bestOldTopLine = TopListDisplay.getBestTime(oldestTopList);
        long time = bestOldTopLine.playerTime;
        parkour.finishExpSource = FinishExpSource.GENERATED;
        int multiplier = parkour.getCategory().getXpMultiplier();
        long exp = Math.round((double)(time/1000) * Math.pow(1.2, ((double)(time/1000))/20.0) * multiplier);
        //int exp = (((int)(time/1000)/roundToUpBy) + 1)*roundToUpBy * multiplier; // formuła do zmiany
        if(exp > MAX_GENERATED_EXP) exp = MAX_GENERATED_EXP;
        parkour.setExp(exp, refreshPlayersExp);
    }

    // ta metoda nie tworzy nowych użytkowników User, koryguje jedynie statystyki już istniejących
    // oraz że ich statystyki są poprawnie policzone
    // wykonuje się ją bezpośrednio przed zmianą expa na mapie
    public static void refreshExpOfPlayers(Parkour parkour, long newExp){
        long oldExp = parkour.getExp();
        List<OfflinePlayer> playerList = TopListDisplay.getAllPlayersOfTop(parkour.getTopListObject().getTopList(false,true,true));
        for(OfflinePlayer player : playerList){
            if(!UserManager.containsUser(player.getName())) {
                Bukkit.getLogger().info("KRYTYCZNY BLAD, BRAK STATYSTYK GRACZA " + player.getName() + " a ma topke na parkourze " + parkour.getName());
            } else{
                User user = UserManager.getUserByName(player.getName());
                int timesFinished = TopListDisplay.getAllTimesOfPlayer(player,parkour.getTopListObject().getTopList(false,true,true)).size();
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
        List<OfflinePlayer> playerList = TopListDisplay.getAllPlayersOfTop(parkour.getTopListObject().getTopList(true,true,true));
        for(OfflinePlayer player : playerList) {
            if(!UserManager.containsUser(player.getName()))
                UserManager.createUser(player.getName());
        }
    }

    // zebranie statystyk z dawnych topek
    public static void giveUsersExpForParkour(Parkour parkour){
        List<OfflinePlayer> playerList = TopListDisplay.getAllPlayersOfTop(parkour.getTopListObject().getTopList(false,true,true));
        for(OfflinePlayer player : playerList) {
            User user = UserManager.getUserByName(player.getName());
            int timesFinished = TopListDisplay.getAllTimesOfPlayer(player,parkour.getTopListObject().getTopList(false,true,true)).size();
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
            allCompletions += parkour.getTopListObject().getTopList(true,true,true).size();
            if(calculateFinishExpIfNotSet)
                if(!parkour.finishExpSource.equals(FinishExpSource.SET))
                    calculateExpOfParkour(parkour, false);
            giveUsersExpForParkour(parkour);
        }
        UserFile.levelFile.saveData();
        String message1 = ChatColor.YELLOW + "Liczba przejść wszystkich graczy na wszystkich parkourach: "+ChatColor.WHITE +ChatColor.BOLD+ allCompletions;
        String message2 = ChatColor.YELLOW +"Dziękujemy za gre na serwerze ! ! <3";
//        Bukkit.broadcastMessage(message1); gdyby to zawsze działało...
//        Bukkit.broadcastMessage(message2);
        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(message1);
            player.sendMessage(message2);
        }
    }


    public static void levelUp(Player player){
        User user = UserManager.getUserByName(player.getName());
        int level = user.getLevel();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(ChatUtil.fixColor("&bAwansowałeś na " + level + " poziom!"));
        String message = ChatUtil.fixColor("&bGracz " + player.getName() + " awansował na " + level + " poziom!");
        //Bukkit.broadcastMessage(message); // wypis na konsolę i dla wszystkich graczy nie zawsze działa
        for (Player player1 : Bukkit.getOnlinePlayers()){ // wypis dla innych graczy
            if(!player1.getName().equals(player.getName()))
                player1.sendMessage(message);
        }
    }

}
