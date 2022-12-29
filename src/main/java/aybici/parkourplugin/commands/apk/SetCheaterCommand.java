package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.commands.arguments.ArgumentManager;
import aybici.parkourplugin.commands.arguments.DominantStringArgument;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.TopLine;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetCheaterCommand extends AdminParkourCommand implements CommandExecutor {
    boolean value;
    private DominantStringArgument playerName;
    public SetCheaterCommand(boolean value){
        this.value = value;
    }
    private boolean parseArgs(String[] args){
        playerName = new DominantStringArgument(true);
        ArgumentManager argumentManager = new ArgumentManager();
        argumentManager.addArgument(playerName);
        return argumentManager.parseAllArgs(args);
    }
    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.deletePermission)) return true;
        boolean ArgsOk = parseArgs(args);
        if(!ArgsOk) return false;
        if(UserManager.containsUser(playerName.getValue())) {
            User user = UserManager.getUserByName(playerName.getValue());
            user.setCheater(value);
            user.saveUser();
            long startTime = System.currentTimeMillis();
            new Thread(() ->{
                for(Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
                    if(parkour.isTopListLoaded){
                        for(TopLine topLine : parkour.getTopListObject().getTopList(true,true,true)){
                            if(topLine.player.getUniqueId().equals(Bukkit.getOfflinePlayer(playerName.getValue()).getUniqueId()))
                                topLine.isPlayerCheater = value;
                        }
                    }
                }
                sender.sendMessage("Od teraz flaga Cheater dla gracza " + playerName.getValue() +" to: " + value);
                sender.sendMessage("Zmiana zajęła " + (System.currentTimeMillis() - startTime) + " ms");
            }).start();
        }
        else sender.sendMessage("Brak gracza o nazwie " + playerName.getValue());
        return true;
    }
}
