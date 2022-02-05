package aybici.parkourplugin.parkours.fails;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FailSet {
    private final int BYTES_PER_FAIL = 22;
    private List<Fail> failSet = new ArrayList<>();
    public final String fileNameInside = File.separator + "failSet.ser";
    public Parkour parkour;

    public FailSet(Parkour parkour){
        this.parkour = parkour;
    }

    public List<Fail> getFailSet(){
        return failSet;
    }

    public void loadFailSet(String directory) throws IOException {
        if(!new File(directory + fileNameInside).exists()) return;

        ReadDataRunnable readDataRunnable = new ReadDataRunnable(directory);
        Thread thread = new Thread(readDataRunnable);
        thread.start();
    }

    public int getNumberOfPlayerFails(Player player){
        int numberOfFails = 0;
        for (Fail fail : failSet){
            if (fail.uuidOfPlayer.equals(player.getUniqueId())) numberOfFails++;
        }
        return numberOfFails;
    }

    private class ReadDataRunnable implements Runnable{
        String directory;
        public ReadDataRunnable(String directory){
            this.directory = directory;
        }
        public void run() {
            try {
                FileInputStream fileInputStream = new FileInputStream(directory + fileNameInside);
                DataInputStream in = new DataInputStream(fileInputStream);
                int size = fileInputStream.available();

                World world = parkour.getLocation().getWorld();
                for (int i = 0; i < size / BYTES_PER_FAIL; i++) {
                    short shortID = in.readShort();
                    long date = in.readLong();

                    float x = in.readFloat();
                    float y = in.readFloat();
                    float z = in.readFloat();

                    Location location = new Location(world, x, y, z, 0f, 0f);
                    UUID uuidOfPlayer = ParkourPlugin.uuidList.getUUIDFromShort(shortID);
                    failSet.add(new Fail(uuidOfPlayer, date, location));
                }
                in.close();
                fileInputStream.close();
                parkour.setFailSet(FailSet.this);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
