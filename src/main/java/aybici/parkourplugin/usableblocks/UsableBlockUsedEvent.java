package aybici.parkourplugin.usableblocks;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UsableBlockUsedEvent extends Event {
    public String command;
    public Player player;

    private static final HandlerList handlers = new HandlerList();

    public UsableBlockUsedEvent(Player player, String command) {
        this.player = player;
        this.command = command;
    }

    public String getMessage() {
        return command;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
