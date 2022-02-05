package aybici.parkourplugin.commands.pklist;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PrintUnfinishedParkours implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        ArgsHandler argsHandler = new ArgsHandler();
        Displayer displayer = new Displayer();
        if (!argsHandler.handleArgs(args, player)) return false;

        List<Parkour> parkoursToDisplay = new ArrayList<>();
        if (argsHandler.category == null){
            for (Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
                if (!parkour.didPlayerFinishParkour(player))
                    parkoursToDisplay.add(parkour);
            }
        }
        else {
            for (Parkour parkour : ParkourPlugin.parkourSet.getAllMapsOfCategory(argsHandler.category)) {
                if (!parkour.didPlayerFinishParkour(player))
                    parkoursToDisplay.add(parkour);
            }
        }
        displayer.display(player, parkoursToDisplay, argsHandler.pageNumber);
        return true;
    }
}