package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class GlobalDeleteTopsCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.deletePermission)) return true;
        if (!sender.getName().equals("rycerz125")){
            sender.sendMessage("nie ma opcji");
            return false;
        }
        if (args.length == 1){
            int removes = 0;
            for (Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
                removes += parkour.getTopListObject().removeAllTimesOfPlayer(Bukkit.getOfflinePlayer(args[0]), true);
                parkour.getTopListObject().saveTopList();
                File demoFile = new File(parkour.folderName + File.separator + "demos"+File.separator  + args[0] + ".txt");
                if (demoFile.exists()) demoFile.delete(); // odkomentować jeśli chcemy usuwać demo razem z topką
            }
            sender.sendMessage("Usunięto czasy gracza " + Bukkit.getOfflinePlayer(args[0]).getName() + ": " + removes);
            if (removes != 0) return true;
            if (args[0].equals("jestemPewienZeChceUsunacWszystkieCzasyWszystkichGraczy")){
                removes = 0;
                for (Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
                    removes += parkour.getTopListObject().getTopList().size();
                    parkour.getTopListObject().clearTopList(true);
                }
                sender.sendMessage("Usunięto wszystkie czasy na wszystkich parkourach: " + removes);
                return true;
            }
            sender.sendMessage("Nie znaleziono żadnych czasów tego gracza: " + Bukkit.getOfflinePlayer(args[0]).getName());
            return true;
        }
        sender.sendMessage("Niepoprawna ilość argumentów!");
        return false;
    }
}
