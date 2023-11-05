package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.blockabovereader.SpecialBlockFinder;
import aybici.parkourplugin.sessions.ParkourSession;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;


public class CheckpointCommand extends OnParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (!isPlayerOnParkour(player)) return true;
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        Set<Material> collidedBlocks = SpecialBlockFinder.getCollidingBlockMaterials(player.getLocation());

        if(collidedBlocks.size() == 1 && collidedBlocks.contains(Material.AIR)) {
            player.sendMessage(ChatUtil.fixColor("&b>&a> &bMusisz stać na bloku, aby ustawić checkpoint!"));
            return true;
        }

        if(session.getParkour().hasAnyBackBlock(collidedBlocks)){
            return true;
        }

        session.checkpoint.setCheckpoint();
        return true;
    }
}