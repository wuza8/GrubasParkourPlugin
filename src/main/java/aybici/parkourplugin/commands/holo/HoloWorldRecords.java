package aybici.parkourplugin.commands.holo;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.users.UserFile;
import aybici.parkourplugin.utils.ChatUtil;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class HoloWorldRecords {

    public static void placeHoloExp(Location location){
        Map<String, Integer> playersMap = new HashMap<>();
        FileConfiguration configFile = UserFile.levelFile.getData();
        for(String line : configFile.getConfigurationSection("Users").getKeys(false)){
            int number = configFile.getInt("Users." + line + ".WorldRecords");
            playersMap.put(line, number);
        }
        Map<String, Integer> posortowanaMapaGraczy = playersMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer numberOne, Integer numberTwo) {
                        return -(numberOne.compareTo(numberTwo));
                    }
                })).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Hologram hologram = HolographicDisplaysAPI.get(ParkourPlugin.getInstance()).createHologram(location);
        hologram.getLines().appendText(ChatUtil.fixColor("&b=-= &a&lTop 10 World Recordów (Łącznie) &b=-="));
        List<String> listLine = new ArrayList<>();
        int index = 0;
        try{
            for(int i = 0; i < 11; i++){
                int punkty = (int) posortowanaMapaGraczy.values().toArray()[i];
                String playerLine = (String) posortowanaMapaGraczy.keySet().toArray()[i];
                if(index == 0){
                    listLine.add(ChatUtil.fixColor("&f1. &a" + playerLine + " &f- &b" + punkty));
                    index++;
                    continue;
                }
                index++;
                listLine.add(ChatUtil.fixColor("&f" + index + ". &a" + playerLine + " &f- &b" + punkty));
            }
        } catch (Exception e){
            index++;
            listLine.add(ChatUtil.fixColor("&f" + index + ". &aBrak danych"));
        } finally {
            int pozostalo = 11 - index;
            index++;
            for(int i = 0; i < pozostalo - 1; ++i){
                int to = index++;
                listLine.add(ChatUtil.fixColor("&f" + to + ". &aBrak danych"));
            }
        }

        ParkourPlugin.hologramExp = hologram;
    }
}
