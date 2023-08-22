package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.hiddens.HiddenParkourFacade;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.ParkourCategory;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import aybici.parkourplugin.parkours.ParkourSet;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static aybici.parkourplugin.commands.apk.AdminParkourCommand.SenderHasPermission;

public class PkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        ParkourSet parkourSet = ParkourPlugin.parkourSet;
        if (args.length == 1) {
            try {
                ParkourPlugin.parkourSessionSet.teleportToParkour(player, args[0]);
            } catch (IllegalStateException exception) {
                player.sendMessage(exception.getMessage());
                return false;
            }
            return true;
        }
        if (args.length == 2) {
            if (ParkourCategoryFacade.get(args[0].toUpperCase()).getName().equals("UNKNOWN")
                    && !SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)){
                player.sendMessage("Nie ma takiej kategorii!");
                return true;
            }


            ParkourCategory parkourCategory = ParkourCategoryFacade.get(args[0].toUpperCase());
            for (Parkour parkour : parkourSet.getAllMapsOfCategory(parkourCategory)){
                if (parkour.getIdentifier() == Integer.parseInt(args[1])) {


                    ParkourPlugin.parkourSessionSet.teleportToParkour(player, parkour.getName());
                    return true;
                }
            }
            player.sendMessage("Parkour doesn't exist!");
            return false;
        }
        player.sendMessage("Niepoprawna liczba argumentów");
        return false;
    }
}
