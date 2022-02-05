package aybici.parkourplugin.sessions;

import aybici.parkourplugin.ParkourPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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

    public void resetTimer() {
        time = 0;
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
