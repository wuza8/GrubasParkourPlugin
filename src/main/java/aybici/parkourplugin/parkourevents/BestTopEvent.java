package aybici.parkourplugin.parkourevents;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.TopListDisplay;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

class BestTopEvent implements ParkourEvent{

    class EventTops{
        public Player player;
        public Long time;
    }

    public String getEventName(){
        return "Najlepsza topa";
    }

    private Parkour parkour;
    private Long eventEnds;

    public BestTopEvent(Parkour parkour, Long eventEnds){
        this.parkour = parkour;
        this.eventEnds = eventEnds;
    }

    private List<EventTops> topLines = new ArrayList<>();

    @Override
    public void whenPlayerMakesTimeOnEvent(Player player, Long time) {
        Optional<EventTops> lastTop = topLines.stream().filter(eventTops -> eventTops.player.equals(player)).findFirst();

        if(lastTop.isPresent()) {
            if(lastTop.get().time > time){
                lastTop.get().time = time;
            }
        }
        else{
            EventTops t = new EventTops();
            t.player = player;
            t.time = time;
            topLines.add(t);
        }

        updateScoreboards();
    }

    @Override
    public void onEventEnd(){
        Bukkit.broadcastMessage(ChatColor.GREEN+"Zwycięzcy eventu to: ");
        topLines.sort(Comparator.comparingLong(o -> o.time));

        int xp[] = {1000, 800, 600, 400, 200, 150, 100, 100, 100, 100 };

        int i=0;
        for(EventTops t : topLines){
            Bukkit.broadcastMessage(ChatColor.YELLOW+((i+1)+". ")+ChatColor.GREEN+t.player.getName()
                    + ": "+ChatColor.YELLOW+ TopListDisplay.timeToString(t.time)
                    +" - "+ChatColor.BOLD+ChatColor.GOLD + xp[i] +" xp");
            User user = UserManager.getUserByName(t.player.getName());
            user.addExp(xp[i]);
            i++;
            if(i>=10) break;
        }
    }

    public void updateScoreboards(){

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Event",
                "dummy", ChatColor.GREEN+parkour.getName().replace("_"," "), RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        topLines.sort(Comparator.comparingLong(o -> o.time));
        if(topLines.size() == 0){
            objective.getScore(ChatColor.GRAY+ "Jeszcze nikomu nie").setScore(-1);
            objective.getScore(ChatColor.GRAY+ "udało się przejść.").setScore(-2);
        }
        int i=0;
        for(EventTops t : topLines){
            i++;
            if(i>10) break;

            objective.getScore(ChatColor.GRAY+ t.player.getName() + ": "+ ChatColor.BOLD+TopListDisplay.timeToString(t.time)).setScore(-i);
        }

        Long mins = (eventEnds - System.currentTimeMillis()) / 1000 / 60;

        if(mins != 0)
            objective.getScore(
                    ChatColor.BLUE + "Koniec za "+ ChatColor.BOLD
                            + mins + " min").setScore(-15);
        else
            objective.getScore(
                    ChatColor.RED + "Koniec za jest bliski...").setScore(-15);

        //Meh
        Team team = scoreboard.registerNewTeam("team");
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        for(Player player : Bukkit.getOnlinePlayers()){
            if(ParkourPlugin.parkourSessionSet.getSession(player).getParkour() != null &&
                    ParkourPlugin.parkourSessionSet.getSession(player).getParkour().equals(parkour)){
                team.addEntry(player.getName()); // no col
                player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR); // clear scoreboard
                player.setScoreboard(scoreboard);
            }
        }
    }
}
