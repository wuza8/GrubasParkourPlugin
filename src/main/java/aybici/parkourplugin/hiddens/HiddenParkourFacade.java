package aybici.parkourplugin.hiddens;

import aybici.parkourplugin.usableblocks.UsableBlockUsedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static org.bukkit.Bukkit.getLogger;

public class HiddenParkourFacade implements Listener {

    private static String hiddenUnlockedFilePlace = "unlockedHiddens.txt";

    //TODO: THIS IS BAD, when someone will change map name, it will not work, because parkour has no real ID!
    private static HashMap<String, List<String>> unlockedHiddens = new HashMap<>();
    private static ReentrantLock fileWriteMutex = new ReentrantLock();

    public static void init() {
        loadUnlockedHiddensFromFile();
    }

    @EventHandler
    public void onSignStand(UsableBlockUsedEvent event) {
        if (event.command.startsWith("$unlockhidden")) {
            String pkname = event.command.substring(new String("$unlockhidden").length()).replace(" ", "");

            if (!playerUnlockedHiddens(event.player).stream().anyMatch(s1 -> s1.equals(pkname))) {
                event.player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN + "> " + "Odblokowano hidden " + ChatColor.BLUE + pkname + ChatColor.GREEN + "!");
                addUnlockedParkour(event.player, pkname);
            }

            event.player.performCommand("pk " + pkname);


            event.player.playSound(event.player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        }
    }

    public static List<String> playerUnlockedHiddens(Player player) {
        List<String> unlocked = unlockedHiddens.get(player.getUniqueId().toString());
        if (unlocked == null) return new ArrayList<>();
        return unlocked;


    }

    private static void loadUnlockedHiddensFromFile(){
        if (!new File(hiddenUnlockedFilePlace).exists()){
            getLogger().info("Missing file: " + hiddenUnlockedFilePlace);
            return;
        }
        try{
            FileReader fileReader = new FileReader(hiddenUnlockedFilePlace);
            BufferedReader reader = new BufferedReader(fileReader);
            readData(reader);
            reader.close();
            fileReader.close();

        } catch(IOException a){
            System.out.println("E KURDE COS JEST NIE TAK DXD");
            System.out.println(Arrays.toString(a.getStackTrace()));
        }
    }

    private static void readData(BufferedReader reader) throws IOException{
        String line = reader.readLine();

        while (line != null) {
            String[] s = line.split(" ");

            List<String> unlocked = unlockedHiddens.get(s[0]);

            if(unlocked == null){
                unlocked = new ArrayList<>();
                unlockedHiddens.put(s[0], unlocked);
            }

            unlocked.add(s[1]);

            // read next line
            line = reader.readLine();
        }
    }

    public static void addUnlockedParkour(Player player, String parkour) {
        List<String> unlocked = unlockedHiddens.get(player.getUniqueId().toString());

        if(unlocked == null){
            unlocked = new ArrayList<>();
            unlockedHiddens.put(player.getUniqueId().toString(), unlocked);
        }

        unlocked.add(parkour);

        //Save to a file
        new Thread(() -> {
            fileWriteMutex.lock();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(hiddenUnlockedFilePlace, true));
                writer.append(player.getUniqueId().toString() + " " + parkour + "\n");
                writer.close();
            }
            catch(Exception ex){
                System.out.println("Couldn't add new unlocked HIDDEN to file!");
                ex.printStackTrace();
            }
            fileWriteMutex.unlock();
        }).start();
    }

}
