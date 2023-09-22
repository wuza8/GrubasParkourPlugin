package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.TagsFacade;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddNewTagCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;

        String tag = null;
        String description = "";

        for(String str : args){
            if(tag == null) tag = str;
            else description += str;
        }

        TagsFacade.addNewTag(tag, description);
        player.sendMessage("Dodano nowy tag.");

        return true;
    }
}
