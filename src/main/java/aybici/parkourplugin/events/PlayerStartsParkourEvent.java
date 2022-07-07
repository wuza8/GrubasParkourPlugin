package aybici.parkourplugin.events;

import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStartsParkourEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled = false;

    private final Player player;
    private final Parkour parkour;

    public Player getPlayer() {
        return player;
    }

    public Parkour getParkour() {
        return parkour;
    }

    public PlayerStartsParkourEvent(Player player, Parkour parkour){
        this.player = player;
        this.parkour = parkour;
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1f,1f);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean is) {
        canceled = is;
    }
}
