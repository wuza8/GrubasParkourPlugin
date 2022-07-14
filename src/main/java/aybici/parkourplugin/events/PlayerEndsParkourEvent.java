package aybici.parkourplugin.events;

import aybici.parkourplugin.parkours.ExpManager;
import aybici.parkourplugin.parkours.FinishExpSource;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEndsParkourEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled = false;

    private final Player player;
    private final Parkour parkour;
    private final long timeInMillis;

    public Player getPlayer() {
        return player;
    }

    public Parkour getParkour() {
        return parkour;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public PlayerEndsParkourEvent(Player player, Parkour parkourPlayerOn, long playerTime) {
        this.player = player;
        this.parkour = parkourPlayerOn;
        this.timeInMillis = playerTime;
        User user = UserManager.getUserByName(player.getName());
        if(parkourPlayerOn.finishExpSource == FinishExpSource.DEFAULT)
            ExpManager.calculateExpOfParkour(parkour, true);
        player.sendMessage(ChatColor.DARK_GREEN + "Exp za przejście: " +ChatColor.GREEN + parkourPlayerOn.getExp());
        user.addExp(parkourPlayerOn.getExp());
        // user.save jest wykonany później w parkourSession w metodzie levelUp, po przeteleportowaniu gracza na start
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
