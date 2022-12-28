package aybici.parkourplugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;

import static java.lang.Math.abs;
import static org.bukkit.Bukkit.getLogger;

public class Lobby {
    private Location lobbyLocation;
    public String directory = "dataBase"+File.separator + "lobbyLocation.txt";
    private BukkitTask lobbyTask;

    public Location getLobbyLocation(){
        return lobbyLocation;
    }
    private void saveLobbyLocation(String directory) {
        if (!new File(directory).exists()) FileCreator.createFile(directory);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(directory, false));
            writer.write(lobbyLocation.getWorld().getName()+"\n");
            writer.write(lobbyLocation.getX()+"\n");
            writer.write(lobbyLocation.getY()+"\n");
            writer.write(lobbyLocation.getZ()+"\n");
            writer.write(lobbyLocation.getPitch()+"\n");
            writer.write(lobbyLocation.getYaw()+"");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setLobbyLocation(Location lobbyLocation){
        this.lobbyLocation = lobbyLocation;
        saveLobbyLocation(directory);
    }
    public void loadLobbyLocation(String directory){
        if (!new File(directory).exists()) return;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(directory));
            double x, y, z;
            float yaw, pitch;
            String worldName;

            worldName = reader.readLine();
            x = Double.parseDouble(reader.readLine());
            y = Double.parseDouble(reader.readLine());
            z = Double.parseDouble(reader.readLine());
            pitch = Float.parseFloat(reader.readLine());
            yaw = Float.parseFloat(reader.readLine());

            World world = Bukkit.getWorld(worldName);
            lobbyLocation = new Location(world, x, y, z, yaw, pitch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("Wczytano lokacje spawnu: " + lobbyLocation.toString());
    }
    public void teleportAllPlayersToLobby(){
        if(getLobbyLocation() != null)
        for(Player player : Bukkit.getOnlinePlayers()){
            teleportPlayerToLobby(player);
        }
    }
    public void teleportPlayerToLobby(Player player){
        ParkourPlugin.parkourSessionSet.deleteParkourSession(player);
        ParkourPlugin.positionSaver.stopSaving(player);
        ParkourPlugin.positionSaver.setPlayerWatching(player,false);
        player.teleport(ParkourPlugin.lobby.getLobbyLocation());
        player.setGameMode(GameMode.ADVENTURE);
    }
    public void runTeleportToLobbyAllTask(){
        if (lobbyLocation == null){
            getLogger().info("Proszę ustawić lobby komendą \"/setobby\"");
            return;
        }
        lobbyTask = new BukkitRunnable(){
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    if (!player.hasPermission(ParkourPlugin.permissionSet.goingAwayFromLobby))
                        if(ParkourPlugin.parkourSessionSet.getSession(player).getParkour() == null && (!ParkourPlugin.positionSaver.isPlayerWatching(player))){
                            int deltaX = (int) abs(player.getLocation().getX() - lobbyLocation.getX());
                            int deltaZ = (int) abs(player.getLocation().getZ() - lobbyLocation.getZ());
                            if(deltaX > 200 || deltaZ > 200 || (!player.getLocation().getWorld().getName().equals(lobbyLocation.getWorld().getName())))
                                teleportPlayerToLobby(player);
                        }
                }
            }
        }.runTaskTimer(ParkourPlugin.getInstance(), 0, 100);

    }
}
