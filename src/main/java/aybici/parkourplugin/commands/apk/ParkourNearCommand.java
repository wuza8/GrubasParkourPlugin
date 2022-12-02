package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import com.github.aybici.Subcommand;
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
            if(arg.startsWith("-maxdist=")){
                if(specifiedArgs.maxDistanceSpecified){
                    specifiedArgs.isArgsOK = false;
                    break;
                }
                specifiedArgs.maxDistance = Double.parseDouble(arg.substring(arg.indexOf("=") + 1));
                specifiedArgs.maxDistanceSpecified = true;
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
            else if(parkour.getLocation().getWorld() == null) {
                String worldName = parkour.getWorldNameFromFile(parkour.folderName + parkour.dataFileNameInsideFolder);
                if(worldName != null)
                if (worldName.equals(world.getName())) {
                    // dodajemy temu parkourowi worlda bo najwyraźniej jest (world) załadowany
                    Location parkourLocation = parkour.getLocation();
                    parkourLocation.setWorld(Bukkit.getWorld(worldName));
                    parkour.setLocation(parkourLocation, false);

                    mapsInWorld.add(parkour);
                }
            }
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
        double maxDistance;

        boolean idDisplaySpecified;
        boolean shortDisplayByIdSpecified;
        boolean maxNumberSpecified;
        boolean displayDistanceSpecified;
        boolean maxDistanceSpecified;

        boolean isArgsOK;

        public Args(){
            displayById = false;
            shortDisplayById = false;
            maxDisplayedMaps = 5;
            displayDistance = false;
            maxDistance = Integer.MAX_VALUE;

            idDisplaySpecified = false;
            shortDisplayByIdSpecified = false;
            maxNumberSpecified = false;
            displayDistanceSpecified = false;
            maxDistanceSpecified = false;

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
            player.sendMessage(ChatColor.GREEN + "/apk pknear "+
                    "[maxDisplayNumber] [\"-id\"] [\"-idshort\"] [\"-distance\"] [\"-maxdist={meters}\"]\n"+ ChatColor.WHITE +
                    "shows parkours near player in player's world");
            player.sendMessage("Specify valid arguments, order doesn't matter");

            player.sendMessage(ChatColor.GREEN + "maxDisplayNumber" + ChatColor.DARK_GREEN +" - positive integer, specifies how many found maps should be displayed, default: 5");
            player.sendMessage(ChatColor.GREEN + "\"-id\"" + ChatColor.DARK_GREEN +" - specifies display variant <CATEGORY PARKOUR_ID>, default: no");
            player.sendMessage(ChatColor.GREEN + "\"-idshort\"" + ChatColor.DARK_GREEN +" - specifies shortened display variant of <CATEGORY PARKOUR_ID>, default: no");
            player.sendMessage(ChatColor.GREEN + "\"-distance\"" + ChatColor.DARK_GREEN +" - shows corresponding distance from the player, default: no");
            player.sendMessage(ChatColor.GREEN + "\"-maxdist={meters}\"" + ChatColor.DARK_GREEN +" - only show maps which distance is less than specified, e.g -maxdist=1000, default: inf");
            return true;
        }

        List<Parkour> mapsInWorld = getMapsInWorld(playerLocation.getWorld());
        String distanceMessage = "";
        if(specifiedArgs.maxDistanceSpecified) {
            mapsInWorld.removeIf(o -> o.getLocation().distanceSquared(playerLocation) > Math.pow(specifiedArgs.maxDistance, 2));
            distanceMessage = " w odległości " + specifiedArgs.maxDistance;
        }
        mapsInWorld.sort(Comparator.comparing(map -> playerLocation.distanceSquared(map.getLocation())));
        player.sendMessage("Znalezione parkoury w tym świecie" + distanceMessage +": " + mapsInWorld.size());
        player.sendMessage("Najbliższe mapy to: ");
        player.sendMessage(constructParkourListString(mapsInWorld, specifiedArgs, playerLocation));
        return true;
    }
}
