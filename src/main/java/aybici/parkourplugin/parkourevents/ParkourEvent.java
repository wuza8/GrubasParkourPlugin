package aybici.parkourplugin.parkourevents;

import org.bukkit.entity.Player;

interface ParkourEvent {

    String getEventName();
    void whenPlayerMakesTimeOnEvent(Player player, Long time);
    void onEventEnd();
    void updateScoreboards();
}
