package aybici.parkourplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Announcmenter {

    private static long mins = 1; //Wysyłaj wiadomość co minutę

    private static List<String> messages = new ArrayList<>();

    private static int current = 0;

    public static void run(){
        messages.add(ChatColor.RED+ "Kup wipa");
        messages.add(ChatColor.RED+ "Wejdź na disco");

        Bukkit.getScheduler().runTaskTimer(ParkourPlugin.getInstance(), ()->{
            Bukkit.broadcastMessage(messages.get(current));
            current++;
            if(current >= messages.size()) current = 0;
        }, 0L, mins * 20 * 60);
    }
}
