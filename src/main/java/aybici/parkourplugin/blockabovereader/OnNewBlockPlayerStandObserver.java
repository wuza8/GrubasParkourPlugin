package aybici.parkourplugin.blockabovereader;

import org.bukkit.Material;

import java.util.List;

public interface OnNewBlockPlayerStandObserver {
    void playerStandOnNewBlock(List<Material> materialList);
}
