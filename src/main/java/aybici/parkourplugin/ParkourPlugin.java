package aybici.parkourplugin;

import aybici.parkourplugin.blockabovereader.UnderPlayerBlockWatcher;
import aybici.parkourplugin.commands.CommandExecutorSetter;
import aybici.parkourplugin.events.PlayerAndEnvironmentListener;
import aybici.parkourplugin.listeners.InventoryInteractListener;
import aybici.parkourplugin.listeners.JoinListener;
import aybici.parkourplugin.parkours.ParkourSet;
import aybici.parkourplugin.sessions.ParkourSessionSet;
import aybici.parkourplugin.sessions.PositionSaver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class ParkourPlugin extends JavaPlugin {
    public static final ParkourSet parkourSet = new ParkourSet();
    public static ParkourSessionSet parkourSessionSet = new ParkourSessionSet(parkourSet);
    public static final UnderPlayerBlockWatcher underPlayerBlockWatcher = new UnderPlayerBlockWatcher();
    public static PositionSaver positionSaver = new PositionSaver();
    private static ParkourPlugin plugin;
    public static ParkourPlugin getInstance(){
        return plugin;
    }
    public static UUIDList uuidList = new UUIDList();
    public static PermissionSet permissionSet = new PermissionSet();
    public static Lobby lobby = new Lobby();

    @Override
    public void onEnable() {
        plugin = this;
        uuidList.loadList();
        uuidList.loadPlayerNames();

        long time = System.currentTimeMillis();
        parkourSet.loadParkours(parkourSet.parkoursFolder);
        Bukkit.getLogger().info("ładowanie wszystkich parkourów zajęło "+ (System.currentTimeMillis()-time) + "ms");

        lobby.loadLobbyLocation(lobby.directory);
        lobby.runTeleportToLobbyAllTask();
        CommandExecutorSetter.setExecutors(this);
        Bukkit.getServer().getPluginManager().registerEvents(underPlayerBlockWatcher, this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerAndEnvironmentListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(positionSaver, this);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryInteractListener(), this);
    }


    public ParkourPlugin()
    {
        super();
    }

    protected ParkourPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }
}
