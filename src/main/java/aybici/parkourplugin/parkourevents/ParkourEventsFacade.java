package aybici.parkourplugin.parkourevents;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.ParkourCategory;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import aybici.parkourplugin.sessions.ParkourSession;
import aybici.parkourplugin.sessions.ParkourSessionSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ParkourEventsFacade {

    private static int standardDuration = 10*60; // 10 minutes
    private static int durationBetweenEvents = 30*60; // 30 minutes
    private static Parkour actualParkourInEvent = null;

    private static String eventStartAnnouncement = ChatColor.AQUA + "Event na mapie"+ChatColor.GREEN+" %s "+
            ChatColor.AQUA+"został wystartowany! Aby dołączyć użyj"+ChatColor.GREEN+" /event "+ChatColor.AQUA+
            "lub użyj książki.";

    private static String eventEnd = ChatColor.AQUA + "Event został zakończony!";

    private static String eventStoppedByAdmin = ChatColor.AQUA + "Event został zakończony przez administrację.";

    private static Random rand = new Random();

    private static boolean autoEventTurnedOn = true;

    private static BukkitTask autoEventTask = null;
    private static BukkitTask eventStopTask = null;

    private static List<BukkitTask> bukkitAnnouncementTasks = new ArrayList<>();

    public void init(){
        if(autoEventTurnedOn) {
            recreateEventTasks(0, durationBetweenEvents);
        }
    }

    public static void startEvent(){
        List<Parkour> eventMaps = ParkourPlugin.parkourSet.getAllMapsOfCategory(ParkourCategoryFacade.get("EVENT"));
        Parkour parkour = eventMaps.get(rand.nextInt() % eventMaps.size());
        startEvent(parkour);
    }

    public static void startEvent(Parkour parkour){
        startEvent(parkour, standardDuration);
    }

    public static void startEvent(Parkour parkour, int durationInSeconds){
        actualParkourInEvent = parkour;
        recreateEventTasks(durationInSeconds, durationBetweenEvents);
        Bukkit.broadcastMessage(String.format(eventStartAnnouncement, parkour.getName()));
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

        actualParkourInEvent = null;
    }

    private static void recreateEventTasks(int endDuration, int nextAutoEventDuration){
        if(autoEventTask != null && !autoEventTask.isCancelled())
            autoEventTask.cancel();
        if(eventStopTask != null && !eventStopTask.isCancelled())
            eventStopTask.cancel();

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
                    startEvent();
                }, endDuration * 20 + 20)
        );

        //Minutes to event tasks
        if(autoEventTurnedOn)
        Stream.of(20, 10, 5, 3, 2, 1).forEach((minutes) -> {
                if(nextAutoEventDuration > minutes*60)
                bukkitAnnouncementTasks.add(Bukkit.getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                    if(minutes < 3) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Do następnego eventu zostało " + ChatColor.GREEN
                                + minutes + ChatColor.AQUA + " minut!");
                    }
                    else if(minutes < 1){
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Do następnego eventu zostało " + ChatColor.GREEN
                                + minutes + ChatColor.AQUA + " minuty!");
                    }
                    else{
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Do następnego eventu zostało " + ChatColor.GREEN
                                + minutes + ChatColor.AQUA + " minuta!");
                    }
                }, (endDuration+nextAutoEventDuration * 20) - minutes * 60 * 20));
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

}
