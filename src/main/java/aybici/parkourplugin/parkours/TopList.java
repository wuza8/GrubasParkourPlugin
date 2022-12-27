package aybici.parkourplugin.parkours;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopList {

    private List<TopLine> topList = new ArrayList<>();
    private final Parkour parkour;
    public final String fileNameInsideFolder = File.separator + "topList.txt";

    public TopList(Parkour parkour){
        this.parkour = parkour;
    }

    public void addTopLine(Player player, long playerTime, int startPing) {
        TopLine topLine = new TopLine(player, playerTime, startPing);
        topList.add(topLine);
        topLine.saveTopLineString(parkour.folderName + fileNameInsideFolder);
        if (topLine.isLagged()) player.sendMessage(ChatColor.RED + "Twój ping jest większy niz 180, czas nie będzie wyswietlony w najlepszych czasach.");
    }

    public List<TopLine> getTopList() {
        return topList;
    }

    public int removeAllTimesOfPlayer(OfflinePlayer player, boolean removeDemo){
        int removes = 0;
        for (TopLine topLine : TopListDisplay.getAllTimesOfPlayer(player, topList)){
            topList.remove(topLine);
            removes++;
        }
        File demoFile = new File(parkour.folderName + File.separator + "demos"+File.separator + player.getName() + ".txt");
        if (demoFile.exists() && removeDemo) demoFile.delete();
        return removes;
    }

    public boolean removeTopLine(TopLine topLine, boolean removeDemo){
        //po co to tu jest wtf
//        File topListFile = new File(parkour.folderName + fileNameInsideFolder);
        File demoFile = new File(parkour.folderName + File.separator + "demos"+File.separator + topLine.player.getName() + ".txt");
        if (demoFile.exists() && removeDemo) demoFile.delete();
//        topListFile.delete();
        if (topLine != null) return topList.remove(topLine);
        return false;
    }
    public boolean hideTopLine(TopLine topLine){
        return true;
    }
    public void hideTopList(){

    }

    public void clearTopList(boolean removeDemo){
        topList.clear();
        File topListFile = new File(parkour.folderName + fileNameInsideFolder);
        topListFile.delete();
        File directory = new File(parkour.folderName + File.separator + "demos");
        if(removeDemo && directory.exists())
            for(File file : directory.listFiles())
                file.delete();
    }

    public void loadTopListString(String directory) throws IOException, CloneNotSupportedException {
        TopLine topLine = new TopLine();
        String currentLine;
        String file = directory + fileNameInsideFolder;
        BufferedReader reader = new BufferedReader(new FileReader(file));

        topList = new ArrayList<>(); // teraz nie powinny sie dublowac topki nawet gdy zaladujemy je ponownie gdy sa zaladowane
        boolean lineExists = true;
        while (lineExists) {
            currentLine = reader.readLine();
            if (currentLine == null) lineExists = false;
            else {
                List<String> convertedLine = Stream.of(currentLine.split(",", -1))
                        .collect(Collectors.toList());
                short shortUUID = Short.parseShort(convertedLine.get(0));
                topLine.date = Long.parseLong(convertedLine.get(1));
                topLine.playerTime = Long.parseLong(convertedLine.get(2));
                topLine.startPing = Integer.parseInt(convertedLine.get(3));
                topLine.endPing = Integer.parseInt(convertedLine.get(4));

                topLine.player = Bukkit.getOfflinePlayer(ParkourPlugin.uuidList.getUUIDFromShort(shortUUID));

                topList.add((TopLine) topLine.clone());
            }
        }
        reader.close();
        //getLogger().info("Zaladowano topki");
    }


    public void saveTopList(){
        File file = new File(parkour.folderName + fileNameInsideFolder);
        file.delete();
        for (TopLine topLine : topList){
            topLine.saveTopLineString(parkour.folderName + fileNameInsideFolder);
        }
    }
}
