package aybici.parkourplugin.builders;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

public class ScoreboardBuilder {
    Scoreboard scoreboard;
    Objective objective;
    public ScoreboardBuilder(String name){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(name,
                "dummy", name, RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setLine(String line, int number){
        objective.getScore(line).setScore(number);
    }

    public Scoreboard build(){
        return scoreboard;
    }
}
