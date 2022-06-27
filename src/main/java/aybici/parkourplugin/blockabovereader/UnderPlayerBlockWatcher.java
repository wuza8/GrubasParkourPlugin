package aybici.parkourplugin.blockabovereader;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UnderPlayerBlockWatcher implements Listener {
    public final HashMap<UUID, List<OnNewBlockPlayerStandObserver>> underPlayerBlockObservers = new HashMap<>();

    public void registerNewObserver(Player player, OnNewBlockPlayerStandObserver observer){
        getPlayerObservers(player).add(observer);
    }

    private List<OnNewBlockPlayerStandObserver> getPlayerObservers(Player player){
        if(underPlayerBlockObservers.containsKey(player.getUniqueId()))
            return underPlayerBlockObservers.get(player.getUniqueId());
        else{
            List<OnNewBlockPlayerStandObserver> newList = new ArrayList<>();
            underPlayerBlockObservers.put(player.getUniqueId(), newList);
            return newList;
        }
    }


    @EventHandler
    public void onAnyPlayerMove(PlayerMoveEvent event){
        Location location = event.getTo();
        List<Material> materialList = SpecialBlockFinder.getCollidingBlockMaterials(location);
        
        for(OnNewBlockPlayerStandObserver observer : getPlayerObservers(event.getPlayer())){
            observer.playerStandOnNewBlock(materialList, event);
        }
    }


}
