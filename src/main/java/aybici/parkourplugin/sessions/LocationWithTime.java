package aybici.parkourplugin.sessions;

import org.bukkit.Location;

public class LocationWithTime {
    public Location location;
    public Long time;
    public LocationWithTime(Long time, Location location){
        this.location = location;
        this.time = time;
    }
}