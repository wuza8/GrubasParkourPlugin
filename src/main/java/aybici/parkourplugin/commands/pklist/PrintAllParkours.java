package aybici.parkourplugin.commands.pklist;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PrintAllParkours implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        ArgsHandler argsHandler = new ArgsHandler();
        Displayer displayer = new Displayer();
        if (!argsHandler.handleArgs(args, player)) return false;

        List<Parkour> parkoursToDisplay;
        if (argsHandler.category == null){
            parkoursToDisplay = new ArrayList<>(ParkourPlugin.parkourSet.getParkours());
        }
        else {
            parkoursToDisplay = ParkourPlugin.parkourSet.getAllMapsOfCategory(argsHandler.category);
        }
        displayer.display(player, parkoursToDisplay, argsHandler.pageNumber);
        return true;
    }
}
