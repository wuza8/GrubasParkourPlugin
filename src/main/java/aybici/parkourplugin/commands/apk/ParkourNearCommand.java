package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ParkourNearCommand extends AdminParkourCommand implements CommandExecutor {
    // wybór wyświetlania mapy opcjonalnie po id ["-id"]
    // wybor maksymalnej ilosci map do wyświetlenia [number]
    boolean displayById = false;
    int maxDisplayedMaps = 5;

    boolean idDisplaySpecified = false;
    boolean maxNumberSpecified = false;

    boolean isArgsOK = true;

    private void specifyArgs(String[] args){
        if (args.length > 2) {
            isArgsOK = false;
            return;
        }
        for (String arg : args){
            if(arg.equals("-id")) {
                if (idDisplaySpecified) {
                    isArgsOK = false;
                    return;
                }
                displayById = true;
                idDisplaySpecified = true;
            }
            try{
                maxDisplayedMaps = Integer.parseInt(arg);
                if(maxNumberSpecified) {
                    isArgsOK = false;
                    return;
                }
                maxNumberSpecified = true;
            }
            catch (Exception ignored){
            }

        }
    }
    private List<Parkour> getMapsInWorld(World world){
        List<Parkour> mapsInWorld = new ArrayList<>();
        for (Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
            if(world.equals(parkour.getLocation().getWorld()))
                mapsInWorld.add(parkour);
        }
        return mapsInWorld;
    }
    private String constructParkourListString(List<Parkour> mapsInWorld){
        StringBuilder parkours = new StringBuilder();
        int parkoursDisplayedCounter = 0;
        for(int i = 0; i < mapsInWorld.size(); i++){
            if(parkoursDisplayedCounter < maxDisplayedMaps) {
                parkours.append(getMapName(mapsInWorld.get(i)) + ", ");
                parkoursDisplayedCounter++;
            } else break;
        }
        String message = parkours.toString();
        return message.substring(0,message.length() - 2);
    }
    private String getMapName(Parkour parkour){
        if(displayById) return parkour.getCategory().toString().toLowerCase(Locale.ROOT) + parkour.getIdentifier();
        else return parkour.getName();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;
        Player player = (Player) sender;
        Location playerLocation = player.getLocation();

        specifyArgs(args);
        if (!isArgsOK) return false;

        List<Parkour> mapsInWorld = getMapsInWorld(playerLocation.getWorld());
        mapsInWorld.sort(Comparator.comparing(map -> playerLocation.distanceSquared(map.getLocation())));
        player.sendMessage("Znalezione parkoury w tym świecie: " + mapsInWorld.size());
        player.sendMessage("Najbliższe mapy to: ");
        player.sendMessage(constructParkourListString(mapsInWorld));
        return true;
    }
}
