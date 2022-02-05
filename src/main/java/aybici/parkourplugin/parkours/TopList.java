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

import static org.bukkit.Bukkit.getLogger;

public class TopList {

    private List<TopLine> topList = new ArrayList<>();
    private final Parkour parkour;
    public final String fileNameInsideFolder = File.separator + "topList.txt";

    TopList(Parkour parkour){
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

    public int removeAllTimesOfPlayer(OfflinePlayer player){
        int removes = 0;
        for (TopLine topLine : ParkourPlugin.topListDisplay.getAllTimesOfPlayer(player, topList)){
            topList.remove(topLine);
            removes++;
        }
        return removes;
    }

    public boolean removeTopLine(TopLine topLine, Parkour parkour){
        File topListFile = new File(parkour.folderName + fileNameInsideFolder);
        File demoFile = new File(parkour.folderName + File.separator + "demos"+File.separator + topLine.player.getName() + ".txt");
        if (demoFile.exists()) demoFile.delete();
        topListFile.delete();
        if (topLine != null) return topList.remove(topLine);
        return false;
    }

    public void clearTopList(Parkour parkour){
        topList.clear();
        File topListFile = new File(parkour.folderName + fileNameInsideFolder);
        topListFile.delete();
    }

    public void loadTopListString(String directory) throws IOException, CloneNotSupportedException {
        TopLine topLine = new TopLine();
        String currentLine;
        String file = directory + fileNameInsideFolder;
        BufferedReader reader = new BufferedReader(new FileReader(file));

        boolean lineExists = true;
        while (lineExists) {
            currentLine = reader.readLine();
            if (currentLine == null) lineExists = false;
            else {
                short shortUUID = Short.parseShort(getSubstringBetweenComas(currentLine, 0));
                topLine.date = Long.parseLong(getSubstringBetweenComas(currentLine, 1));
                topLine.playerTime = Long.parseLong(getSubstringBetweenComas(currentLine, 2));
                topLine.startPing = Integer.parseInt(getSubstringBetweenComas(currentLine, 3));
                topLine.endPing = Integer.parseInt(getSubstringBetweenComas(currentLine, 4));

                topLine.player = Bukkit.getOfflinePlayer(ParkourPlugin.uuidList.getUUIDFromShort(shortUUID));

                topList.add((TopLine) topLine.clone());
            }
        }
        reader.close();
        //getLogger().info("Zaladowano topki");
    }

    private String getSubstringBetweenComas(String sequence, int fromComa){
        String sequenceCopy = sequence;
        int[] comaIndexes = new int[4];
        for(int i = 0; i<4; i++){
            comaIndexes[i] = sequenceCopy.indexOf(',');
            sequenceCopy = sequenceCopy.replaceFirst(",",";");
        }
        int substringStartIndex;
        int substringEndIndex;
        if (fromComa == 0) {
            substringStartIndex = 0;
            substringEndIndex = comaIndexes[0];
        } else if (fromComa == 4){
            if (comaIndexes[3] == -1) return "10000";
            substringStartIndex = comaIndexes[3] + 1;
            substringEndIndex = sequence.length();
        } else {
            if (comaIndexes[2] == -1 && fromComa >= 3) return "10000";
            substringStartIndex = comaIndexes[fromComa - 1] + 1;
            if (fromComa == 2 && comaIndexes[2] == -1) substringEndIndex = sequence.length();
            else substringEndIndex = comaIndexes[fromComa];
        }
        return sequence.subSequence(substringStartIndex, substringEndIndex).toString();
    }

    public void saveTopList(){
        File file = new File(parkour.folderName + fileNameInsideFolder);
        file.delete();
        for (TopLine topLine : topList){
            topLine.saveTopLineString(parkour.folderName + fileNameInsideFolder);
        }
    }
}
