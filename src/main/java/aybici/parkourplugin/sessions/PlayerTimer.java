package aybici.parkourplugin.sessions;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.blockabovereader.SpecialBlockFinder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class PlayerTimer{
    private Player player;
    private long time;
    private BukkitTask timerTask;
    private int slowMotion = 1;

    public PlayerTimer(Player player) {
        this.player = player;
    }
    public PlayerTimer(Player player, int slowMotion) {
        this.player = player;
        this.slowMotion = slowMotion;
    }
    public void startTimer() {
        time = System.currentTimeMillis();
        timerTask = new BukkitRunnable(){
            @Override
            public void run() {
                long level = (actualTime()/slowMotion) / 1000;
                player.setLevel((int) level);

                float exp = ((float) (actualTime()/slowMotion) % 1000) / 1000.0f;
                player.setExp(exp);
            }
        }.runTaskTimer(ParkourPlugin.getInstance(), 0, 1);
    }

    public long actualTime() {
        return System.currentTimeMillis() - time;
    }

    public long calculateAccurateTime(){
        List<LocationWithTime> playerDemo = PositionSaver.playerDemosHashMap.get(player);
        PositionSaver.correctTimeSignature(playerDemo);
        int size = playerDemo.size();
        LocationWithTime beforeStartLocation = playerDemo.get(1);
        LocationWithTime startLocation = playerDemo.get(2);
        LocationWithTime beforeEndLocation = playerDemo.get(size-2);
        LocationWithTime endLocation = playerDemo.get(size-1);

        long numberOfPositionsFromStartToEnd = playerDemo.size() - 2;
        long timeInTicksBasedOnMillis = (endLocation.time - startLocation.time)/50;

        long lagTime = 0L;
        if (numberOfPositionsFromStartToEnd - 1 > timeInTicksBasedOnMillis) {
            lagTime = (numberOfPositionsFromStartToEnd - 1 - timeInTicksBasedOnMillis)*50;
            //System.out.println("Uwaga pomiar czasu zbyt maly, róznica w tickach: " + (numberOfPositionsFromStartToEnd - 1 - timeInTicksBasedOnMillis));
        }

        LocationWithTime startEdgeLocation = getWoolEdgeLocation(beforeStartLocation, startLocation, Material.LIME_WOOL);
        LocationWithTime endEdgeLocation = getWoolEdgeLocation(beforeEndLocation, endLocation, Material.RED_WOOL);

        return endEdgeLocation.time - startEdgeLocation.time + lagTime;
    }

    public void resetTimer() {
        time = 0;
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private LocationWithTime getWoolEdgeLocation(LocationWithTime location1, LocationWithTime location2, Material specialMaterial){
        List<Material> materialList1 = SpecialBlockFinder.getCollidingBlockMaterials(location1.location);
        if(materialList1.contains(specialMaterial)) {Bukkit.getLogger().info("ERROR: 1st pos contains specMaterial!");return location1;}
        List<Material> materialList2 = SpecialBlockFinder.getCollidingBlockMaterials(location2.location);
        if(!materialList2.contains(specialMaterial)) {Bukkit.getLogger().info("ERROR: 2nd pos does not contain specMaterial!");return location2;}

        long timeError = 50; // ms

        LocationWithTime edgeLocation1 = location1;
        LocationWithTime edgeLocation2 = location2;
        LocationWithTime tempLocation;

        while(timeError >= 2){
            tempLocation = getPositionBetween(edgeLocation1,edgeLocation2);
            if(SpecialBlockFinder.getCollidingBlockMaterials(tempLocation.location).contains(specialMaterial))
                    edgeLocation2 = tempLocation;
            else edgeLocation1 = tempLocation;

            timeError = edgeLocation2.time - edgeLocation1.time;
        }
        return getPositionBetween(edgeLocation1,edgeLocation2);
    }
    private LocationWithTime getPositionBetween(LocationWithTime location1, LocationWithTime location2){
        long time = (location2.time + location1.time)/2;
        double X = (location2.location.getX() + location1.location.getX())/2;
        double Y = (location2.location.getY() + location1.location.getY())/2;
        double Z = (location2.location.getZ() + location1.location.getZ())/2;
        return new LocationWithTime(time, new Location(location1.location.getWorld(),X,Y,Z));
    }
}
