package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.commands.arguments.ArgumentManager;
import aybici.parkourplugin.commands.arguments.BooleanArgument;
import aybici.parkourplugin.commands.arguments.DominantIntArgument;
import aybici.parkourplugin.commands.arguments.IntArgument;
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
    private BooleanArgument idArgument;
    private BooleanArgument idShortArgument ;
    private BooleanArgument showDistanceArgument;
    private IntArgument maxDistanceArgument;
    private DominantIntArgument maxAmountDisplay;
    private boolean parseArgs(String[] args){
        idArgument = new BooleanArgument("-id", false);
        idShortArgument = new BooleanArgument("-idshort", false);
        showDistanceArgument = new BooleanArgument("-distance", false);
        maxDistanceArgument = new IntArgument("-maxdist=", Integer.MAX_VALUE);
        maxAmountDisplay = new DominantIntArgument(5);
        ArgumentManager argumentManager = new ArgumentManager();
        argumentManager.addArgument(idArgument);
        argumentManager.addArgument(idShortArgument);
        argumentManager.addArgument(showDistanceArgument);
        argumentManager.addArgument(maxDistanceArgument);
        argumentManager.addArgument(maxAmountDisplay);
        return argumentManager.parseAllArgs(args);
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
    private String constructParkourListString(List<Parkour> mapsInWorld, Location playerLocation){
        StringBuilder parkours = new StringBuilder();
        int parkoursDisplayedCounter = 0;
        for (Parkour parkour : mapsInWorld) {
            if (parkoursDisplayedCounter < maxAmountDisplay.getValue()) {
                parkours.append(getMapName(parkour, playerLocation)).append(", ");
                parkoursDisplayedCounter++;
            } else break;
        }
        String message = parkours.toString();
        if(message.length() == 0) return "nie udało się znaleźć map :(";
        return message.substring(0,message.length() - 2);
    }
    private String getMapName(Parkour parkour, Location playerLocation){
        String mapName = parkour.getName();
        if(idArgument.getValue()) mapName = parkour.getCategory().toString().toLowerCase(Locale.ROOT) + " " + parkour.getIdentifier();
        if(idShortArgument.getValue()) mapName = parkour.getCategory().toString().substring(0, 2) + parkour.getIdentifier();
        if(showDistanceArgument.getValue()) mapName += ChatColor.YELLOW + " " + (int)parkour.getLocation().distance(playerLocation) + "m" + ChatColor.RESET;
        return mapName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;
        Player player = (Player) sender;
        Location playerLocation = player.getLocation();

        boolean isArgsOK = parseArgs(args);

        if (!isArgsOK) {
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
        if(maxDistanceArgument.isSpecified()) {
            mapsInWorld.removeIf(o -> o.getLocation().distanceSquared(playerLocation) > Math.pow(maxDistanceArgument.getValue(), 2));
            distanceMessage = " w odległości " + maxDistanceArgument.getValue();
        }
        mapsInWorld.sort(Comparator.comparing(map -> playerLocation.distanceSquared(map.getLocation())));
        player.sendMessage("Znalezione parkoury w tym świecie" + distanceMessage +": " + mapsInWorld.size());
        player.sendMessage("Najbliższe mapy to: ");
        player.sendMessage(constructParkourListString(mapsInWorld, playerLocation));
        return true;
    }
}
