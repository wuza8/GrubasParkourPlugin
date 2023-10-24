package aybici.parkourplugin.listeners;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.chests.ChestManager;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Random;

public class ChestListener implements Listener {

    public static HashMap<Player, Long> cooldown = new HashMap<>();

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
            player.sendMessage(ChatUtil.fixColor("&b>&a> &cNie posiadasz kluczy!"));
            event.setCancelled(true);
            return;
        }

        if(cooldown.containsKey(player)){
            if(cooldown.get(player) > System.currentTimeMillis()){
                player.sendMessage(ChatUtil.fixColor("&b>&a> &bMusisz poczekać &a5 &bsekund, aby otworzyć następną skrzynie"));
                event.setCancelled(true);
            } else{
                user.removeKeys(1);
                user.saveUser();

                event.setCancelled(true);

                ChestManager.openChest(player);
                cooldown.put(player, System.currentTimeMillis() + 5000);
            }
        } else{
            user.removeKeys(1);
            user.saveUser();

            event.setCancelled(true);

            ChestManager.openChest(player);
            cooldown.put(player, System.currentTimeMillis() + 5000);
        }
    }
}
