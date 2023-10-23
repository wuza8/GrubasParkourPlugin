package aybici.parkourplugin;

import aybici.parkourplugin.blockabovereader.UnderPlayerBlockWatcher;
import aybici.parkourplugin.commands.*;
import aybici.parkourplugin.commands.holo.HoloExp;
import aybici.parkourplugin.commands.holo.HoloLevel;
import aybici.parkourplugin.commands.holo.HoloWorldRecords;
import aybici.parkourplugin.commands.holo.ParkourTopsCommand;
import aybici.parkourplugin.events.PlayerAndEnvironmentListener;
import aybici.parkourplugin.hiddens.HiddenParkourFacade;
import aybici.parkourplugin.listeners.*;
import aybici.parkourplugin.parkourevents.ParkourEventsFacade;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import aybici.parkourplugin.parkours.ParkourSet;
import aybici.parkourplugin.sessions.ParkourSessionSet;
import aybici.parkourplugin.sessions.PositionSaver;
import aybici.parkourplugin.usableblocks.UndergroundSignsFacade;
import aybici.parkourplugin.usableblocks.UsableBlocksFacade;
import aybici.parkourplugin.users.UserFile;
import aybici.parkourplugin.utils.ChatUtil;
import com.earth2me.essentials.Essentials;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class ParkourPlugin extends JavaPlugin {
    public static final ParkourSet parkourSet = new ParkourSet();
    public static ParkourSessionSet parkourSessionSet = new ParkourSessionSet(parkourSet);
    public static final UnderPlayerBlockWatcher underPlayerBlockWatcher = new UnderPlayerBlockWatcher();
    public static PositionSaver positionSaver = new PositionSaver();
    public static ParkourPlugin plugin;
    public static UUIDList uuidList = new UUIDList();
    public static PermissionSet permissionSet = new PermissionSet();
    public static Lobby lobby = new Lobby();
    public LevelFile levelFile = LevelFile.getInstance();
    public boolean placeholders = false;
    public Essentials essentials;

    public File chestLocationFile;
    public File hologramsLocationFile;
    public YamlConfiguration chestLocationConfig;
    public YamlConfiguration hologramsLocationConfig;

    public static Hologram hologramLevel;
    public static Hologram hologramExp;
    public static Hologram hologramWorldRecords;

    public File keysFile;
    public YamlConfiguration keysConfig;

    @Override
    public void onEnable() {
        ParkourCategoryFacade.init();
        plugin = this;
        uuidList.loadList();
        uuidList.loadPlayerNames();
        uuidList.loadAutoGeneratedPlayerNames();
        ParkourEventsFacade.init();
        HiddenParkourFacade.init();
        essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        //uuidList.loadAutoGeneratedPlayerNames(); //konieczne tylko jak nie mamy zaladowanych na serwerze offlineGraczy ktorzy są w topkach
                                                   //np w przypadku usuniecia nam graczy na serwerze na skutek ataku botow
        levelFile.setup(this); //to wyłączać do testów
        UserFile.loadUsers();//to wyłączać do testów


        long time = System.currentTimeMillis();
        parkourSet.loadParkours(parkourSet.parkoursFolder);
        Bukkit.getLogger().info("ładowanie wszystkich parkourów zajęło "+ (System.currentTimeMillis()-time) + "ms");

        lobby.loadLobbyLocation(lobby.directory);
        lobby.runTeleportToLobbyAllTask();
        CommandExecutorSetter.setExecutors(this);
        registerListeners();

        chestLocationFile = new File(getDataFolder(), "chest-location.yml");
        chestLocationConfig = YamlConfiguration.loadConfiguration(chestLocationFile);

        saveDefaultConfig();

        if(getConfig().getString("hologramLevel") == null || getConfig().getString("hologramExp") == null || getConfig().getString("hologramWorldRecords") == null) {
            return;
        } else{
            Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                @Override
                public void run() {
                    if(hologramLevel != null) hologramLevel.delete();
                    HoloLevel.placeHoloExp(ParkourTopsCommand.stringToLocation(getConfig().getString("hologramLevel")));
                }
            }, 10 * 20, 10 * 20);
            Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                @Override
                public void run() {
                    if(hologramExp != null) hologramExp.delete();
                    HoloExp.placeHoloExp(ParkourTopsCommand.stringToLocation(getConfig().getString("hologramExp")));
                }
            }, 10 * 20, 10 * 20);
            Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                @Override
                public void run() {
                    if(hologramWorldRecords != null) hologramWorldRecords.delete();
                    HoloWorldRecords.placeHoloExp(ParkourTopsCommand.stringToLocation(getConfig().getString("hologramWorldRecords")));
                }
            }, 10 * 20, 10 * 20);
        }

        Announcmenter.run(this);

        ParkourEventsFacade.startBossBarTask();
    }

    public void onDisable(){
        this.placeholders = false;
    }

    private void registerListeners(){
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(underPlayerBlockWatcher, this);
        pluginManager.registerEvents(new PlayerAndEnvironmentListener(), this);
        pluginManager.registerEvents(positionSaver, this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new InventoryInteractListener(), this);
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new ModifyInventory(), this);
        pluginManager.registerEvents(new UsableBlocksFacade(), this);
        pluginManager.registerEvents(new UndergroundSignsFacade(), this);
        pluginManager.registerEvents(new HiddenParkourFacade(), this);
        pluginManager.registerEvents(new ParkourEventsFacade(), this);
        pluginManager.registerEvents(new ChestListener(), this);
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
