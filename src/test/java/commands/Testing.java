package commands;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.junit.Test;

import java.io.File;

public class Testing {

    @Test
    public void test() throws Exception{
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Pattern pattern = MidiFileManager.loadPatternFromMidi(new File("test.mid"));

        Player player = new Player();
        System.out.println(pattern.getTokens());
        player.play(pattern);

    }
}
