package aybici.parkourplugin.parkours;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class TopListDisplay {

    public static List<TopLine> getNotLaggedTimes(List<TopLine> topList){
        List<TopLine> notLaggedTimes = new ArrayList<>();
        for (TopLine topLine : topList){
            if (!topLine.isLagged()) notLaggedTimes.add(topLine);
        }
        return notLaggedTimes;
    }

    public static TopLine getBestTime(List<TopLine> topList){
        TopLine bestTimeLine = new TopLine();
        for (TopLine topLine : topList){
            if(topLine.playerTime < bestTimeLine.playerTime) bestTimeLine = topLine;
        }
        if(bestTimeLine.playerTime < Long.MAX_VALUE){
            return bestTimeLine;
        }
        System.out.println("Nobody finished the parkour.");
        return null;
    }

    public static List<TopLine> getAllTimesOfPlayer(OfflinePlayer player, List<TopLine> topList){
        List<TopLine> allLinesOfPlayer = new ArrayList<>();
        for (TopLine topLine : topList){
            if (topLine.player.getUniqueId().equals(player.getUniqueId())) allLinesOfPlayer.add(topLine);
        }
        return allLinesOfPlayer;
    }

    public static TopLine getBestTimeOfPlayer(OfflinePlayer player, List<TopLine> topList){
        TopLine bestTimeLine = new TopLine();

        for (TopLine topLine : getNotLaggedTimes(getAllTimesOfPlayer(player, topList))){
            if(topLine.playerTime < bestTimeLine.playerTime) bestTimeLine = topLine;
        }
        if(bestTimeLine.playerTime < Long.MAX_VALUE){
            return bestTimeLine;
        }
        return null;
    }

    public static List<OfflinePlayer> getAllPlayersOfTop(List<TopLine> topList){
        List<UUID> playerUUIDs = new ArrayList<>();
        for (TopLine topLine : topList){
            if (!playerUUIDs.contains(topLine.player.getUniqueId()))
                playerUUIDs.add(topLine.player.getUniqueId());
        }
        List<OfflinePlayer> players = new ArrayList<>();
        for (UUID uuid : playerUUIDs){
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if(player.getName() != null)
                players.add(player);
            else {
                player = Bukkit.getOfflinePlayer(ParkourPlugin.uuidList.getNameFromUUID(uuid));
                players.add(player);
                Bukkit.getLogger().info("Ostrzeżenie: Gracz " + player.getName() + "o ID " + uuid  + " jest w topce, ale nigdy nie byl na serwerze.");
            }
        }
        return players;
    }

    public static String timeToString(long playerTime){
        short hours, minutes, seconds, millis;

        millis = (short)(playerTime%1000);
        seconds = (short)((playerTime/1000)%60);
        minutes = (short)((playerTime/60_000)%60);
        hours = (short)(playerTime/3_600_000);

        String mStr = "", hStr = "", zerosBeforeMillis = "",
                zeroBeforeSeconds = "", zeroBeforeMinutes = "",
                separateMinutesSeconds = "", separateHoursMinutes = "";

        if (millis/10 == 0) zerosBeforeMillis = "00";
        else if (millis/100 == 0) zerosBeforeMillis = "0";

        if (seconds/10 == 0 && (minutes != 0 || hours != 0)) zeroBeforeSeconds = "0";
        if (minutes/10 == 0 && hours != 0) zeroBeforeMinutes = "0";

        if (minutes != 0 || hours != 0) {
            separateMinutesSeconds = ":";
            mStr = minutes + "";
        }
        if (hours != 0) {
            separateHoursMinutes = ":";
            hStr = hours + "";
        }

        return hStr + separateHoursMinutes + zeroBeforeMinutes + mStr
                + separateMinutesSeconds + zeroBeforeSeconds + seconds + ":" + zerosBeforeMillis + millis;
    }

    public static void displayTimesOnScoreboard(Player player, DisplayingTimesState displayingTimesState, SortTimesType sortTimesType){
        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(player).getParkour();
        /*Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.PLAYER_LIST); //czyszczenie glownego scoreboarda
        Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
        Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.SIDEBAR);*/
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR); // clear scoreboard
        List<TopLine> topList = parkour.getTopListObject().getTopList();
        if (topList.size()==0) return;
        List<TopLine> topLinesToSort = getTopListToSort(topList, displayingTimesState, player);
        if (topLinesToSort.size()==0) return;
        List<TopLine> topLinesToDisplay = sortTopList(topLinesToSort, sortTimesType);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(parkour.getCategory().name()+parkour.getIdentifier(),
                "dummy", parkour.getName().replaceAll("_", " "), RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        scoreboard.registerNewTeam("team");
        scoreboard.getTeam("team").setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        scoreboard.getTeam("team").addEntry(player.getName());

        if (topLinesToDisplay.size() >= 5) {
            for (int i = 0; i < 5; i++)
                objective.getScore(topLinesToDisplay.get(i).toScoreboardDisplay()).setScore(-i - 1);
        } else{
            for (int i = 0; i < topLinesToDisplay.size(); i++)
                objective.getScore(topLinesToDisplay.get(i).toScoreboardDisplay()).setScore(-i - 1);
        }

        player.setScoreboard(scoreboard);
    }
    public static void displayScoreboardToOtherPlayers(Parkour parkour, DisplayingTimesState displayingTimesState, SortTimesType sortTimesType){
        for (Player player : Bukkit.getOnlinePlayers()){
            Parkour parkourLocal = ParkourPlugin.parkourSessionSet.getSession(player).getParkour();
            if (parkourLocal != null)
                if (parkourLocal.getName().equals(parkour.getName()))
                    displayTimesOnScoreboard(player, displayingTimesState, sortTimesType);
        }
    }

    private static boolean printAlreadySortedTopList(Player player, List<TopLine> sortedTopList, int pageNumber, String info){
        if (pageNumber <= 0) {
            player.sendMessage(ChatColor.RED + "Numer strony musi być dodatni!");
            return false;
        }
        final int LINES_PER_PAGE = 5;
        int numberOfPages = sortedTopList.size()/LINES_PER_PAGE;
        int lastPageLength = sortedTopList.size() % LINES_PER_PAGE;
        if (lastPageLength != 0) numberOfPages++;
        if (pageNumber > numberOfPages) {
            player.sendMessage(ChatColor.RED + "Maksymalna strona to " + numberOfPages + "!");
            return false;
        }
        player.sendMessage(info);
        player.sendMessage(ChatColor.AQUA + "strona nr " + pageNumber + " z " + numberOfPages);

        int startIndex = LINES_PER_PAGE*(pageNumber - 1);
        if (pageNumber != numberOfPages || lastPageLength == 0)
            for (int index = startIndex; index < startIndex + LINES_PER_PAGE; index++)
                player.sendMessage(sortedTopList.get(index).toString());
        else
            for (int index = startIndex; index < startIndex + lastPageLength; index++)
                player.sendMessage(sortedTopList.get(index).toString());
        return true;
    }
    public static List<TopLine> getTopListToSort(List<TopLine> topList, DisplayingTimesState displayingTimesState, Player player){
        List<TopLine> topLinesToSort = new ArrayList<>();
        switch (displayingTimesState){
            case ALL_PLAYERS_ALL_TIMES:
                topLinesToSort = topList;
                //player.sendMessage(ChatColor.AQUA + "Oto wszystkie czasy na tej mapie");
                break;
            case ONE_PLAYER_ALL_TIMES:
                //player.sendMessage(ChatColor.AQUA + "Oto Twoje czasy na tej mapie");
                topLinesToSort = getAllTimesOfPlayer(player, topList);
                break;
            case ALL_PLAYERS_BEST_TIMES:
                //player.sendMessage(ChatColor.AQUA + "Oto najlepsze czasy wszystkich graczy na tej mapie");
                for (OfflinePlayer playerLocal : getAllPlayersOfTop(topList)) {
                    TopLine bestTimeOfPlayer = getBestTimeOfPlayer(playerLocal, topList);
                    if (bestTimeOfPlayer != null)
                    topLinesToSort.add(bestTimeOfPlayer);
                }
                break;
        }
        return topLinesToSort;
    }
    public static List<TopLine> sortTopList(List<TopLine> topList, SortTimesType sortTimesType){
        List<TopLine> topLinesToDisplay = new ArrayList<>();
        switch (sortTimesType){
            case DATE:
                //player.sendMessage(ChatColor.AQUA + "posortowane wg daty:");
                topLinesToDisplay.addAll(topList);
                topLinesToDisplay.sort(Comparator.comparing(topLine -> topLine.date));
                break;
            case TIME:
                //player.sendMessage(ChatColor.AQUA + "posortowane wg czasu:");
                topLinesToDisplay.addAll(topList);
                topLinesToDisplay.sort(Comparator.comparing(topLine -> topLine.playerTime));
                break;
            case PLAYERS:
                //player.sendMessage(ChatColor.AQUA + "posortowane alfabetycznie wg graczy:");
                List<String> playerNames = new ArrayList<>();

                for (OfflinePlayer playerLocal : getAllPlayersOfTop(topList)){
                    playerNames.add(playerLocal.getName());
                }
                Collections.sort(playerNames);
                for (String playerName : playerNames){
                    for (TopLine topLine : topList){
                        if (topLine.player.getName().equals(playerName))
                            topLinesToDisplay.add(topLine);
                    }
                }
                break;
        }
        return topLinesToDisplay;
    }

    public static boolean printTopList(Player player, List<TopLine> topList, DisplayingTimesState displayingTimesState, SortTimesType sortTimesType, int page){
        List<TopLine> topLinesToSort = getTopListToSort(topList, displayingTimesState, player);
        List<TopLine> topLinesToDisplay = sortTopList(topLinesToSort, sortTimesType);
        String info1 = null;
        String info2 = null;
        switch (displayingTimesState){
            case ALL_PLAYERS_ALL_TIMES:
                info1 = ChatColor.AQUA + "Oto wszystkie czasy na tej mapie ";
                break;
            case ONE_PLAYER_ALL_TIMES:
                info1 = ChatColor.AQUA + "Oto Twoje czasy na tej mapie ";
                break;
            case ALL_PLAYERS_BEST_TIMES:
                info1 = ChatColor.AQUA + "Oto najlepsze czasy wszystkich graczy na tej mapie ";
                break;
        }
        switch (sortTimesType){
            case DATE:
                info2 = ChatColor.AQUA + "posortowane wg daty:";
                break;
            case TIME:
                info2 = ChatColor.AQUA + "posortowane wg czasu:";
                break;
            case PLAYERS:
                info2 = ChatColor.AQUA + "posortowane alfabetycznie wg graczy:";
                break;
        }
        return printAlreadySortedTopList(player, topLinesToDisplay, page, info1 + info2);
    }
}
