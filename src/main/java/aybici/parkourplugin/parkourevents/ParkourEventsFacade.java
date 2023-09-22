package aybici.parkourplugin.parkourevents;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.events.PlayerEndsParkourEvent;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import aybici.parkourplugin.sessions.ParkourSession;
import aybici.parkourplugin.utils.TabUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Stream;

public class ParkourEventsFacade implements Listener {

    private static int standardDuration = 10*60; // 10 minutes
    private static int durationBetweenEvents = 10*60; // 10 minutes
    private static Parkour actualParkourInEvent = null;

    private static ParkourEvent actualParkourEvent;

    private static Long nextAutoEvent;

    private static String eventStartAnnouncement = ChatColor.AQUA + "Event na mapie"+ChatColor.GREEN+" %s "+
            ChatColor.AQUA+", typu " +ChatColor.GREEN+ "%s" + ChatColor.AQUA+ " został wystartowany! Aby dołączyć użyj"
            +ChatColor.GREEN+" /event " + ChatColor.AQUA + "lub użyj książki.";

    private static String eventEnd = ChatColor.AQUA + "Event został zakończony!";

    private static String eventStoppedByAdmin = ChatColor.AQUA + "Event został zakończony przez administrację.";

    private static Random rand = new Random();

    private static boolean autoEventTurnedOn = true;

    private static BukkitTask autoEventTask = null;
    private static BukkitTask eventStopTask = null;

    private static List<BukkitTask> bukkitAnnouncementTasks = new ArrayList<>();

    public static void init(){
        if(autoEventTurnedOn) {
            recreateEventTasks(0, durationBetweenEvents);
        }

        Bukkit.getScheduler().runTaskTimer(ParkourPlugin.getInstance(), () -> {
            updateScoreboard();
            TabUtil.refreshAllPlayersTab();
        },0, 20*20);
    }

    public static void startEvent(){
        List<Parkour> eventMaps = ParkourPlugin.parkourSet.getAllMapsOfCategory(ParkourCategoryFacade.get("EVENT"));
        Parkour parkour = eventMaps.get(Math.abs(rand.nextInt()) % eventMaps.size());
        startEvent(parkour);
    }

    public static boolean isAutoEventOn(){
        return autoEventTurnedOn;
    }

    public static Long getNextAutoEventTime(){
        return nextAutoEvent;
    }

    public static Long getNextEventTimeInMinutes(){
        return (ParkourEventsFacade.getNextAutoEventTime()-System.currentTimeMillis())/1000/60;
    }

    public static void startEvent(Parkour parkour){
        startEvent(parkour, standardDuration);
    }

    public static void startEvent(Parkour parkour, int durationInSeconds){
        actualParkourInEvent = parkour;

        if(rand.nextBoolean())
            actualParkourEvent = new BestTopEvent(parkour, System.currentTimeMillis() + durationInSeconds*1000);
        else
            actualParkourEvent = new WhoFirstIsWinner(parkour, System.currentTimeMillis() + durationInSeconds*1000);

        recreateEventTasks(durationInSeconds, durationBetweenEvents);
        Bukkit.broadcastMessage(String.format(
                eventStartAnnouncement,
                parkour.getName(),
                actualParkourEvent.getEventName()
        ));
    }

    public static Parkour getEventParkour(){
        return actualParkourInEvent;
    }

    public static void makeEventLonger(int newDurationInSeconds) {
        recreateEventTasks(newDurationInSeconds, durationBetweenEvents);
    }

    public static void switchAutoEvent(boolean autoEvent) {
        autoEventTurnedOn = false;
        if(autoEventTask != null && !autoEventTask.isCancelled())
            autoEventTask.cancel();
    }

    public static void endEvent(boolean byAdmin){
        for(Player player : Bukkit.getOnlinePlayers()){
            ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
            if(session.isPlayerOnParkour() && session.getParkour().equals(actualParkourInEvent)){
                player.performCommand("lobby");
            }
        }

        if(!byAdmin) Bukkit.broadcastMessage(eventEnd);
        else Bukkit.broadcastMessage(eventStoppedByAdmin);

        actualParkourEvent.onEventEnd();

        actualParkourInEvent = null;
        actualParkourEvent = null;
    }

    private static void recreateEventTasks(int endDuration, int nextAutoEventDuration){
        if(autoEventTask != null && !autoEventTask.isCancelled())
            autoEventTask.cancel();
        if(eventStopTask != null && !eventStopTask.isCancelled())
            eventStopTask.cancel();

        nextAutoEvent = System.currentTimeMillis() + ((endDuration + nextAutoEventDuration) * 1000);

        if(nextAutoEventDuration != 0 && autoEventTurnedOn)
            autoEventTask = createTaskForNextAutoEvent(endDuration + nextAutoEventDuration);
        if(endDuration != 0)
            eventStopTask = createTaskForEventEnding(endDuration);


        //Announcement tasks
        for(BukkitTask task : bukkitAnnouncementTasks){
            if(task != null && !task.isCancelled())
                task.cancel();
        }

        //Announcement after map ends
        bukkitAnnouncementTasks.add(
                Bukkit.getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                    Bukkit.broadcastMessage(ChatColor.AQUA+"Następny event za "+(getNextEventTimeInMinutes()+1)+" min");
                }, endDuration * 20 + 20)
        );

        //Minutes to event tasks
        if(autoEventTurnedOn)
        Stream.of(20, 10, 5, 3, 2, 1).forEach((minutes) -> {
                if(nextAutoEventDuration > minutes*60)
                bukkitAnnouncementTasks.add(Bukkit.getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                    if(minutes > 3) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Do następnego eventu zostało " + ChatColor.GREEN
                                + minutes + ChatColor.AQUA + " minut!");
                    }
                    else if(minutes > 1){
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Do następnego eventu zostało " + ChatColor.GREEN
                                + minutes + ChatColor.AQUA + " minuty!");
                    }
                    else{
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Do następnego eventu zostało " + ChatColor.GREEN
                                + minutes + ChatColor.AQUA + " minuta!");
                    }
                }, ((endDuration+nextAutoEventDuration) * 20) - minutes * 60 * 20));
        });
    }

    private static BukkitTask createTaskForNextAutoEvent(int duration){
        return Bukkit.getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
            startEvent();
        }, duration * 20);
    }
    private static BukkitTask createTaskForEventEnding(int eventEndingInSeconds){
        return Bukkit.getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
            endEvent(false);
        }, eventEndingInSeconds * 20);
    }

    @EventHandler
    public void handlePlayerEndsParkourEvent(PlayerEndsParkourEvent event){
        if(event.getParkour().equals(actualParkourInEvent)) {
            actualParkourEvent.whenPlayerMakesTimeOnEvent(event.getPlayer(), event.getPlayerTimeInMillis());
        }
    }

    public static void updateScoreboard(){
        if(actualParkourEvent != null){
            actualParkourEvent.updateScoreboards();
        }
    }

}
