package aybici.parkourplugin.blockabovereader;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public interface OnNewBlockPlayerStandObserver {
    void playerStandOnNewBlock(Set<Material> materialList, PlayerMoveEvent event);
}
