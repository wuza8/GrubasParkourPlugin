package aybici.parkourplugin.blockabovereader;

import org.bukkit.block.Block;

import java.util.List;

public interface OnNewBlockPlayerStandObserver {
    void playerStandOnNewBlock(List<Block> blockList);
}
