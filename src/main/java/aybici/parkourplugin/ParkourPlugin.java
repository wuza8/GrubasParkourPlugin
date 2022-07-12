package aybici.parkourplugin;

import aybici.parkourplugin.blockabovereader.UnderPlayerBlockWatcher;
import aybici.parkourplugin.commands.*;
import aybici.parkourplugin.events.PlayerAndEnvironmentListener;
import aybici.parkourplugin.listeners.ChatListener;
import aybici.parkourplugin.listeners.InventoryInteractListener;
import aybici.parkourplugin.listeners.JoinListener;
import aybici.parkourplugin.parkours.ParkourSet;
import aybici.parkourplugin.sessions.ParkourSessionSet;
import aybici.parkourplugin.sessions.PositionSaver;
import aybici.parkourplugin.users.UserFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class ParkourPlugin extends JavaPlugin {
    public static final ParkourSet parkourSet = new ParkourSet();
    public static ParkourSessionSet parkourSessionSet = new ParkourSessionSet(parkourSet);
    public static final UnderPlayerBlockWatcher underPlayerBlockWatcher = new UnderPlayerBlockWatcher();
    public static PositionSaver positionSaver = new PositionSaver();
    private static ParkourPlugin plugin;
    public static UUIDList uuidList = new UUIDList();
    public static PermissionSet permissionSet = new PermissionSet();
    public static Lobby lobby = new Lobby();
    public LevelFile levelFile = LevelFile.getInstance();

    @Override
    public void onEnable() {
        plugin = this;
        uuidList.loadList();
        uuidList.loadPlayerNames();
        levelFile.setup(this);
        UserFile.loadUsers();


        long time = System.currentTimeMillis();
        parkourSet.loadParkours(parkourSet.parkoursFolder);
        Bukkit.getLogger().info("ładowanie wszystkich parkourów zajęło "+ (System.currentTimeMillis()-time) + "ms");

        lobby.loadLobbyLocation(lobby.directory);
        lobby.runTeleportToLobbyAllTask();
        CommandExecutorSetter.setExecutors(this);
        registerListeners();
    }

    private void registerListeners(){
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(underPlayerBlockWatcher, this);
        pluginManager.registerEvents(new PlayerAndEnvironmentListener(), this);
        pluginManager.registerEvents(positionSaver, this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new InventoryInteractListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
    }


    public ParkourPlugin()
    {
        super();
    }

    protected ParkourPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    public static ParkourPlugin getInstance(){
        return plugin;
    }
}
