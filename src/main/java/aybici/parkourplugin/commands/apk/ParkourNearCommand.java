package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private Args specifyArgs(String[] args){
        Args specifiedArgs = new Args();

        if (args.length > 4) {
            specifiedArgs.isArgsOK = false;
            return specifiedArgs;
        }
        for (String arg : args){
            if(arg.equals("-id")) {
                if (specifiedArgs.idDisplaySpecified) {
                    specifiedArgs.isArgsOK = false;
                    break;
                }
                specifiedArgs.displayById = true;
                specifiedArgs.idDisplaySpecified = true;
                continue;
            }
            if(arg.equals("-distance")){
                if(specifiedArgs.displayDistanceSpecified){
                    specifiedArgs.isArgsOK = false;
                    break;
                }
                specifiedArgs.displayDistance = true;
                specifiedArgs.displayDistanceSpecified = true;
                continue;
            }
            if(arg.equals("-idshort")){
                if(specifiedArgs.shortDisplayByIdSpecified){
                    specifiedArgs.isArgsOK = false;
                    break;
                }
                specifiedArgs.shortDisplayById = true;
                specifiedArgs.shortDisplayByIdSpecified = true;
                continue;
            }
            try{
                specifiedArgs.maxDisplayedMaps = Integer.parseInt(arg);
                if(specifiedArgs.maxNumberSpecified) {
                    specifiedArgs.isArgsOK = false;
                    break;
                }
                specifiedArgs.maxNumberSpecified = true;
                continue;
            }
            catch (Exception ignored){
            }
            specifiedArgs.isArgsOK = false;
            break;
        }
        return specifiedArgs;
    }
    private List<Parkour> getMapsInWorld(World world){
        List<Parkour> mapsInWorld = new ArrayList<>();
        for (Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
            if(parkour.getLocation() == null){
                Bukkit.getLogger().info("Parkour " + parkour.getName() + " nie ma ustalonej lokalizacji!");
            } else
            if(world.equals(parkour.getLocation().getWorld()))
                mapsInWorld.add(parkour);
        }
        return mapsInWorld;
    }
    private String constructParkourListString(List<Parkour> mapsInWorld, Args args, Location playerLocation){
        StringBuilder parkours = new StringBuilder();
        int parkoursDisplayedCounter = 0;
        for (Parkour parkour : mapsInWorld) {
            if (parkoursDisplayedCounter < args.maxDisplayedMaps) {
                parkours.append(getMapName(parkour, args, playerLocation)).append(", ");
                parkoursDisplayedCounter++;
            } else break;
        }
        String message = parkours.toString();
        if(message.length() == 0) return "nie udało się znaleźć map :(";
        return message.substring(0,message.length() - 2);
    }
    private String getMapName(Parkour parkour, Args args, Location playerLocation){
        String mapName = parkour.getName();
        if(args.displayById) mapName = parkour.getCategory().toString().toLowerCase(Locale.ROOT) + " " + parkour.getIdentifier();
        if(args.shortDisplayById) mapName = parkour.getCategory().toString().substring(0, 2) + parkour.getIdentifier();
        if(args.displayDistance) mapName += ChatColor.YELLOW + " " + (int)parkour.getLocation().distance(playerLocation) + "m" + ChatColor.RESET;
        return mapName;
    }
    private class Args{
        // wybór wyświetlania mapy opcjonalnie po id ["-id"]
        // czy ma wyświetlić krótką nazwę po id
        // wybor maksymalnej ilosci map do wyświetlenia [number]
        // czy ma wyświetlić dystans do mapy

        boolean displayById;
        boolean shortDisplayById;
        int maxDisplayedMaps;
        boolean displayDistance;

        boolean idDisplaySpecified;
        boolean shortDisplayByIdSpecified;
        boolean maxNumberSpecified;
        boolean displayDistanceSpecified;

        boolean isArgsOK;

        public Args(){
            displayById = false;
            shortDisplayById = false;
            maxDisplayedMaps = 5;
            displayDistance = false;

            idDisplaySpecified = false;
            shortDisplayByIdSpecified = false;
            maxNumberSpecified = false;
            displayDistanceSpecified = false;

            isArgsOK = true;
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;
        Player player = (Player) sender;
        Location playerLocation = player.getLocation();

        Args specifiedArgs = specifyArgs(args);

        if (!specifiedArgs.isArgsOK) {
            return false;
        }

        List<Parkour> mapsInWorld = getMapsInWorld(playerLocation.getWorld());
        mapsInWorld.sort(Comparator.comparing(map -> playerLocation.distanceSquared(map.getLocation())));
        player.sendMessage("Znalezione parkoury w tym świecie: " + mapsInWorld.size());
        player.sendMessage("Najbliższe mapy to: ");
        player.sendMessage(constructParkourListString(mapsInWorld, specifiedArgs, playerLocation));
        return true;
    }
}
