package aybici.parkourplugin.blockabovereader;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public interface OnNewBlockPlayerStandObserver {
    void playerStandOnNewBlock(List<Material> materialList, PlayerMoveEvent event);
}
