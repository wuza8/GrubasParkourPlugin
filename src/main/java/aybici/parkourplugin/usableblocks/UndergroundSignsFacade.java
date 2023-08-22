package aybici.parkourplugin.usableblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class UndergroundSignsFacade implements Listener {

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event){
        Location blockUnderLocation = event.getTo().clone().add(0,-2,0);
        Block blockUnder = null;
        if(blockUnderLocation.getY() > 0)
        try {
            blockUnder = blockUnderLocation.getBlock();
        }
        catch(Exception ex){}

        if(blockUnder == null) return;
        if(blockUnder.getType() != Material.OAK_SIGN) return;

        String command = null;

        try {
            Sign sign = (Sign) blockUnder.getState();
            String[] lines = sign.getLines();
            command = String.join(" ", lines);
        }
        catch(Exception ex){}

        if(command != null && (command.startsWith("/") || command.startsWith("$")) ){
            UsableBlocksFacade.sendCommand(command, event.getPlayer());
        }
    }
}
