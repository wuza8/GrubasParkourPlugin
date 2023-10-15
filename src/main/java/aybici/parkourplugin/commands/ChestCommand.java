package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(!player.isOp()){
            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNie masz odpowiednich uprawnień :)"));
            return true;
        }

        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        ParkourPlugin.getInstance().chestLocationConfig.set("chests." + x + "," + y + "," + z, true);
        try{
            ParkourPlugin.getInstance().chestLocationConfig.save(ParkourPlugin.getInstance().chestLocationFile);
        } catch (IOException e){
            e.printStackTrace();
            return true;
        }

        player.sendMessage(ChatUtil.fixColor("&b>&a> &bUstawiono skrzynie na koordynatach: &a" + x + "&b,&a" + y + "&b,&a" + z));
        return true;
    }
}
