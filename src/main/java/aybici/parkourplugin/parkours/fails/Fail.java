package aybici.parkourplugin.parkours.fails;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.Location;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class Fail {

    public UUID uuidOfPlayer;
    public long date;
    public Location location;

    public Fail(UUID uuidOfPlayer, long date, Location location){
        this.uuidOfPlayer = uuidOfPlayer;
        this.date = date;
        this.location = location;
    }

    public void saveFail(String directory) throws IOException {
        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(Objects.requireNonNull(getPlayer(uuidOfPlayer))).getParkour();

        String fileNameInside = File.separator + "failSet.ser";
        FileOutputStream fileOut =
                new FileOutputStream(directory + fileNameInside, true);
        DataOutputStream out = new DataOutputStream(fileOut);
        writeData(out);
        out.close();
        fileOut.close();

        if (parkour.getFailSetObject() == null) {
            FailSet failSet = new FailSet(parkour);
            failSet.loadFailSet(parkour.folderName);
            return;
        }
        parkour.getFailSetObject().getFailSet().add(this);
    }

    private void writeData(DataOutputStream out) throws IOException {
        out.writeShort(ParkourPlugin.uuidList.getShortIdentifier(uuidOfPlayer));
        out.writeLong(date);

        out.writeFloat((float) location.getX());
        out.writeFloat((float) location.getY());
        out.writeFloat((float) location.getZ());
    }
}
