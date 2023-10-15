package aybici.parkourplugin;

import aybici.parkourplugin.blockabovereader.UnderPlayerBlockWatcher;
import aybici.parkourplugin.commands.*;
import aybici.parkourplugin.events.PlayerAndEnvironmentListener;
import aybici.parkourplugin.hiddens.HiddenParkourFacade;
import aybici.parkourplugin.listeners.ChatListener;
import aybici.parkourplugin.listeners.InventoryInteractListener;
import aybici.parkourplugin.listeners.JoinListener;
import aybici.parkourplugin.listeners.ModifyInventory;
import aybici.parkourplugin.parkourevents.ParkourEventsFacade;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import aybici.parkourplugin.parkours.ParkourSet;
import aybici.parkourplugin.sessions.ParkourSessionSet;
import aybici.parkourplugin.sessions.PositionSaver;
import aybici.parkourplugin.usableblocks.UndergroundSignsFacade;
import aybici.parkourplugin.usableblocks.UsableBlocksFacade;
import aybici.parkourplugin.users.UserFile;
import com.earth2me.essentials.Essentials;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
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
    public Chat chat = null;
    public Permission permission = null;
    public Economy economy = null;
    public boolean placeholders = false;
    public Essentials essentials;

    public File chestLocationFile;
    public YamlConfiguration chestLocationConfig;

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

        this.setupChat();//to wyłączać do testów
        this.setupEconomy();//to wyłączać do testów
        this.setupPermissions();//to wyłączać do testów

        chestLocationFile = new File(getDataFolder(), "chest-location.yml");
        chestLocationConfig = YamlConfiguration.loadConfiguration(chestLocationFile);

        Announcmenter.run();
    }

    public void onDisable(){
        this.chat = null;
        this.permission = null;
        this.economy = null;
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
    }


    public ParkourPlugin()
    {
        super();
    }

    protected ParkourPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    public boolean setupChat(){
        RegisteredServiceProvider<Chat> rsp = this.getServer().getServicesManager().getRegistration(Chat.class);
        this.chat = (Chat)rsp.getProvider();
        return this.chat != null;
    }

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = this.getServer().getServicesManager().getRegistration(Permission.class);
        this.permission = (Permission) rsp.getProvider();
        return this.permission != null;
    }

    public boolean setupEconomy(){
        if(this.getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null)
            return false;
        this.economy = (Economy) rsp.getProvider();
        return (this.economy != null);
    }

    public static ParkourPlugin getInstance(){
        return plugin;
    }
}