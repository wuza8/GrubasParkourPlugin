package aybici.parkourplugin.listeners;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.chests.ChestManager;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import jdk.tools.jmod.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Action action = event.getAction();

        if(action != Action.RIGHT_CLICK_BLOCK)
            return;

        Material blockType = event.getClickedBlock().getType();

        if(blockType != Material.CHEST)
            return;

        int x = event.getClickedBlock().getX();
        int y = event.getClickedBlock().getY();
        int z= event.getClickedBlock().getZ();

        if(!ParkourPlugin.getInstance().chestLocationConfig.getBoolean("chests." + x + "," + y + "," + z))
            return;

        Player player = event.getPlayer();
        User user = UserManager.getUserByName(player.getName());

        if(user.getKeys() < 1){
            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNie posiadasz kluczy!"));
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        ChestManager.openChest(player);
    }
}