package aybici.parkourplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.enginehub.jinglenote.bukkit.BukkitJingleNotePlayer;
import org.enginehub.jinglenote.sequencer.MidiJingleSequencer;

import java.io.File;

public class FluteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

            new Thread(() -> {
                try {
                    MidiJingleSequencer sequencer = new MidiJingleSequencer(new File(args[0]), false);
                    BukkitJingleNotePlayer p = new BukkitJingleNotePlayer((Player) commandSender, sequencer);
                    p.play();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }).start();

        return false;
    }
}
