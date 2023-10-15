package aybici.parkourplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Announcmenter {

    private static long mins = 11; //Wysyłaj wiadomość co 11 minut

    private static List<String> messages = new ArrayList<>();

    private static int current = 0;

    public static void run(){
        messages.add(ChatColor.AQUA+ ">"+ ChatColor.GREEN + "> "+ ChatColor.WHITE + "Zakup usługę "+ ChatColor.YELLOW + "VipMan "+ ChatColor.WHITE + "na naszym sklepie już teraz! Wszystkie dodatkowe przywileje możesz sprawdzić na naszej stronie: "+ ChatColor.YELLOW + "http://itemshop.pl/842866.html");
        messages.add(ChatColor.AQUA+ ">"+ ChatColor.GREEN + "> "+ ChatColor.WHITE + "Wejdź na naszego "+ ChatColor.BLUE + "discorda"+ ChatColor.WHITE + ", aby być na bieżąco - "+ ChatColor.BLUE + "https://discord.gg/AAAkcX3zae");

        Bukkit.getScheduler().runTaskTimer(ParkourPlugin.getInstance(), ()->{
            Bukkit.broadcastMessage(messages.get(current));
            current++;
            if(current >= messages.size()) current = 0;
        }, 0L, mins * 20 * 60);
    }
}
