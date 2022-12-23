import aybici.parkourplugin.ParkourPlugin;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;


public class ParkourPluginTest {
    private PlayerMock player;

    private Location parkourSpawn;
    private Location parkourStartBlockLocation;
    private Location parkourStopBlockLocation;
    private Location parkourBackBlock;
    private ParkourPlugin plugin;
    private Random random;

    @Before
    public void setUp() {
        ServerMock server = MockBukkit.mock();
        plugin = MockBukkit.load(ParkourPlugin.class);
//        player = server.addPlayer();
//        World world = player.getLocation().getWorld();
//        parkourSpawn = new Location(world, 100, 10, 100);
//        parkourStartBlockLocation = new Location(world, 102, 10, 100);
//        parkourStopBlockLocation = new Location(world, 115, 10, 100);
//        parkourBackBlock = new Location(world, 105, 9, 100);
//        createParkour();
//        random = new Random();
    }

    private void createParkour(){
        parkourStartBlockLocation.clone().add(0, -1, 0).getBlock().setType(Material.LIME_WOOL);
        parkourStopBlockLocation.clone().add(0, -1, 0).getBlock().setType(Material.RED_WOOL);
        parkourBackBlock.clone().add(0,-1,0).getBlock().setType(Material.OAK_LOG);
        player.simulatePlayerMove(parkourSpawn);
        player.performCommand("apk add pk1");
        player.nextMessage();
        player.performCommand("apk addbb OAK_LOG");
        player.nextMessage();
        player.assertTeleported(player.getLocation(), 1);
    }



    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void gameplayTest(){
        long time = Math.abs((random.nextInt() % 10) * 1000 + 500);
        stepOnGreenWool();
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        stepOnRedWool();
        //TODO: Find out why this line doesn't work on MockBukkit
        //player.assertTeleported(parkourSpawn, 1);
        //assertThat(player.nextMessage(),startsWith("Your time: "+time/1000+":"));
    }

    @Test
    public void backBlockTest(){
        player.simulatePlayerMove(parkourBackBlock);
        //TODO: Find out why this line doesn't work on MockBukkit
        //player.assertTeleported(parkourSpawn, 1);
    }
    @Test
    public void checkEssentials(){
        if(plugin.essentials != null){
            System.out.println("passed essentials");
        }else System.out.println("not passed essentials");
    }

    private void stepOnGreenWool(){
        player.simulatePlayerMove(parkourStartBlockLocation);
    }

    private void stepOnRedWool(){
        player.simulatePlayerMove(parkourStopBlockLocation);
    }
}
