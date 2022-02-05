package aybici.parkourplugin.sessions;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class StaticCheckpoint{
    private ParkourSession parkourSession;
    private List<Location> checkpointList;
    private Player player;
    public int placedCheckpointNumber = -1;
    private int checkpointAttentionIndex = -1;
    private BukkitTask checkpointTask;

    public StaticCheckpoint(ParkourSession parkourSession){
        this.parkourSession = parkourSession;
        this.checkpointList = parkourSession.getParkour().getCheckpoints();
        this.player = parkourSession.getPlayer();
    }

    public void runSession(){
        checkpointTask = new BukkitRunnable(){
            @Override
            public void run() {
                if (isActive())
                    setIfPlayerStandOnNextCheckpoint();
            }
        }.runTaskTimer(ParkourPlugin.getInstance(), 0, 1);
    }
    public void cancelSession(){
        if(checkpointTask != null)
            checkpointTask.cancel();
        resetReachedCheckpoints();
    }
    public Location getCurrentCheckpointLocation(){
        if (placedCheckpointNumber == -1) return null;
        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        Location location = checkpointList.get(placedCheckpointNumber).clone().add(0.5,0,0.5);
        location.setPitch(pitch);
        location.setYaw(yaw);
        return location;
    }

    public int getCheckpointIndexThatPlayerIsStanding(){ //zwraca index checkpointa na którym gracz teraz stoi
        for(Location location : checkpointList)
            if(player.getLocation().getBlock().getLocation().equals(location))
                return checkpointList.indexOf(location);
        return -1; //gdy nie wdepnął
    }
    public void setIfPlayerStandOnNextCheckpoint(){
        int checkpointNumber = getCheckpointIndexThatPlayerIsStanding();
        if (checkpointNumber == -1)
            return;
        if (placedCheckpointNumber == checkpointNumber - 1){
            placedCheckpointNumber = checkpointNumber;
            parkourSession.playerWasGivenAttention = false;
            player.sendMessage(ChatColor.DARK_GREEN + "Nadepnięto na checkpoint " + (checkpointNumber+1)); // zmiana o 1 bo numery sa od 0
        } else if (checkpointAttentionIndex != checkpointNumber && placedCheckpointNumber < checkpointNumber - 1){
            checkpointAttentionIndex = checkpointNumber;
            player.sendMessage(ChatColor.RED + "Pominięto checkpoint " + (placedCheckpointNumber + 1+1));
        }
    }
    public boolean playerEnteredAllCheckpoints(){
        if (checkpointList.isEmpty()) return true;
        return (checkpointList.size() - 1 == placedCheckpointNumber);
    }


    private boolean isActive(){
        if (parkourSession != null)
            if (parkourSession.getPlayerGameplayState().equals(PlayerGameplayState.PARKOURING))
                if (!parkourSession.checkpoint.isPlaced()) return true;
        return false;
    }
    public void remove(int checkpointNumber){
        if (checkpointNumber > checkpointList.size()){
            player.sendMessage("Jest tylko " + checkpointList.size()+ " checkpointów!");
            return;
        }
        if (checkpointNumber <= 0){
            player.sendMessage("Numer musi być dodatni!");
            return;
        }
        Parkour parkour = parkourSession.getParkour();
        checkpointList.remove(checkpointNumber - 1);
        parkour.saveParkour(parkour.folderName + parkour.dataFileNameInsideFolder);
        player.sendMessage("Usunięto checkpoint");
    }
    public void add(Location location){
        Parkour parkour = parkourSession.getParkour();
        checkpointList.add(location.getBlock().getLocation());
        parkour.saveParkour(parkour.folderName + parkour.dataFileNameInsideFolder);
        player.sendMessage("Dodano " +checkpointList.size()+ ". checkpoint");
    }
    public void resetReachedCheckpoints(){
        placedCheckpointNumber = -1;
    }
}
