package aybici.parkourplugin.parkours;

import aybici.parkourplugin.DateAndTime;
import aybici.parkourplugin.FileCreator;
import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.users.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.*;


public class TopLine implements Cloneable {
    TopLine(Player player, long playerTime, int startPing) {
        this.date = System.currentTimeMillis();
        this.player = player;
        this.playerTime = playerTime;
        this.startPing = startPing;
        this.endPing = player.getPing();
        this.hidden = false;
    }
    TopLine(){
        this.date = 0;
        this.player = null;
        this.playerTime = Long.MAX_VALUE;
        this.startPing = 0;
        this.endPing = 0;
        this.hidden = false;
    }
    public OfflinePlayer player;
    long date;
    public long playerTime;
    int startPing;
    int endPing;
    boolean hidden;

    public void saveTopLineString(BufferedWriter writer) throws IOException {
        String hiddenFlag = "";
        if(hidden) hiddenFlag = ",hidden";
        writer.write(ParkourPlugin.uuidList.getShortIdentifier(player.getUniqueId()) + ","
                    +date+"," +playerTime+","+startPing+","+endPing + hiddenFlag + "\n");
    }
    public void saveTopLineString(String directory) {
        if(!new File(directory).exists()) FileCreator.createFile(directory);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(directory, true));
            saveTopLineString(writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public boolean isLagged(){
        return (startPing > 180 || endPing > 180);
    }
    public String toScoreboardDisplay(){
        String cheaterColor = "";
        if(isPlayerCheater()) cheaterColor = ChatColor.RED + "";
        String timeToString = TopListDisplay.timeToString(playerTime);
        return ChatColor.AQUA +cheaterColor + timeToString + ChatColor.DARK_GREEN + ", " + ChatColor.WHITE + ParkourPlugin.uuidList.getNameFromUUID(player.getUniqueId());
    }

    /*private String getName(OfflinePlayer player){
        if (player.getName() != null) {
            return player.getName();
        } else return player.getUniqueId().toString().substring(0,10);
    }*/

    @Override
    public String toString(){
        String color;
        if(hidden) color = "DARK_GRAY";
        else if(isPlayerCheater()) color = "DARK_RED";
        else if (isLagged()) color = "RED";
        else color = "WHITE";
        String timeToString = TopListDisplay.timeToString(playerTime);
        return ParkourPlugin.uuidList.getNameFromUUID(player.getUniqueId()) + ", " +ChatColor.DARK_GREEN+ DateAndTime.getDateString(date)+
                ChatColor.WHITE + ", " + ChatColor.valueOf(color) + timeToString;
    }
    private boolean isPlayerCheater(){
        boolean cheater = false;
        if(UserManager.containsUser(player.getName()))
            cheater = UserManager.getUserByName(player.getName()).isCheater();
        return cheater;
    }
}