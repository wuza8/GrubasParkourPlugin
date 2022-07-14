package aybici.parkourplugin.blockabovereader;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

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

        Location locationTo = event.getTo();
        Location locationFrom = event.getFrom();
        Set<Material> interactiveMaterialList = SpecialBlockFinder.getCollidingBlockMaterials(locationTo);
        locationFrom.setY(event.getTo().getY()); // fix fall to edge from height
        Set<Material> materialList = SpecialBlockFinder.getCollidingBlockMaterials(locationFrom);
        materialList.remove(Material.LIME_WOOL);
        materialList.remove(Material.RED_WOOL);

        if(interactiveMaterialList.contains(Material.LIME_WOOL))
            materialList.add(Material.LIME_WOOL);
        if(interactiveMaterialList.contains(Material.RED_WOOL))
            materialList.add(Material.RED_WOOL);

            for(OnNewBlockPlayerStandObserver observer : getPlayerObservers(event.getPlayer())){
            observer.playerStandOnNewBlock(materialList, event);
        }
    }


}
