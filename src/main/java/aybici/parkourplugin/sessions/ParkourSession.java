package aybici.parkourplugin.sessions;

import aybici.parkourplugin.Lobby;
import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.blockabovereader.OnNewBlockPlayerStandObserver;
import aybici.parkourplugin.events.PlayerEndsParkourEvent;
import aybici.parkourplugin.events.PlayerStartsParkourEvent;
import aybici.parkourplugin.parkours.*;
import aybici.parkourplugin.parkours.fails.Fail;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.TabUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public class ParkourSession implements OnNewBlockPlayerStandObserver {
    private final Player player;
    private Parkour parkourPlayerOn;
    private PlayerGameplayState playerGameplayState = PlayerGameplayState.ON_PARKOUR;
    private final PlayerTimer playerTimer;
    private int startPing;
    public Checkpoint checkpoint = new Checkpoint();
    public StaticCheckpoint staticCheckpoint;
    public boolean playerWasGivenAttention = false;

    public PlayerTimer getPlayerTimer() {
        return playerTimer;
    }

    public ParkourSession(Player player){
        this.player = player;
        ParkourPlugin.underPlayerBlockWatcher.registerNewObserver(player, this);
        playerTimer = new PlayerTimer(player);
    }

    public int getStartPing(){
        return startPing;
    }
    public Parkour getParkour() {
        return parkourPlayerOn;
    }

    public Player getPlayer() {
        return this.player;
    }
    public PlayerGameplayState getPlayerGameplayState(){
        return playerGameplayState;
    }
    private boolean isPlayerJailed(){
        if(ParkourPlugin.getInstance().essentials != null)
            if(ParkourPlugin.getInstance().essentials.getUser(player).isJailed()){
                player.sendMessage( ChatColor.RED + "Jesteś w więzieniu, nie możesz wchodzić na mapy :(");
                long jailTime =
                        ParkourPlugin.getInstance().essentials.getUser(player).getJailTimeout();
                player.sendMessage(ChatColor.DARK_GRAY + "Do końca kary zostało " + (jailTime - System.currentTimeMillis()) + " ms");
                ParkourPlugin.lobby.teleportPlayerToLobby(player);
                return true;
            }
        return false;
    }
    private boolean isMapLoaded(){
        if(parkourPlayerOn.getLocation().getWorld() == null) { // do zrobienia - ladowanie topek dopiero przy wchodzeniu gracza na mape
            String directory = parkourPlayerOn.folderName + parkourPlayerOn.dataFileNameInsideFolder;
            parkourPlayerOn.loadParkour(parkourPlayerOn.folderName, false);
            if(parkourPlayerOn.getLocation().getWorld() == null){
                player.sendMessage("Parkour "+ ChatColor.GRAY +parkourPlayerOn.getName() +ChatColor.WHITE+ " jest na niezaładowanym świecie," +ChatColor.GREEN + " Załadujemy świat automatycznie, spróbuj ponownie!");
                String worldName = parkourPlayerOn.getWorldNameFromFile(directory);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"mvload " + worldName);
                ParkourPlugin.lobby.teleportPlayerToLobby(player);
                return false;
            }
        }
        return true;
    }
    public boolean teleportTo(Parkour parkour){
        PositionSaver.setPlayerWatching(player,false);

        boolean playerInJail = isPlayerJailed();
        if(playerInJail) return false;

        parkourPlayerOn = parkour;
        boolean mapIsLoaded = isMapLoaded();
        if(!mapIsLoaded) return false;

        player.teleport(parkour.getLocation());
        playerGameplayState = PlayerGameplayState.ON_PARKOUR;
        playerTimer.resetTimer();
        player.setGameMode(GameMode.ADVENTURE);
        checkpoint.reset();
        if (staticCheckpoint != null)
            staticCheckpoint.cancelSession();
        playerWasGivenAttention = false;
        staticCheckpoint = new StaticCheckpoint(this);
        staticCheckpoint.runSession();
        ParkourPlugin.positionSaver.stopSaving(player);
        return true;
    }

    public boolean isPlayerOnParkour() {
        return parkourPlayerOn != null;
    }

    public void onPlayerStandOnGreenWool() throws InterruptedException {
        if(!playerGameplayState.equals(PlayerGameplayState.ON_PARKOUR)) return;

        PlayerStartsParkourEvent event = new PlayerStartsParkourEvent(player, parkourPlayerOn);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            playerGameplayState = PlayerGameplayState.PARKOURING;
            playerTimer.startTimer();
            startPing = player.getPing();
            ParkourPlugin.positionSaver.startSaving(player);
        }
    }

    private String getCheaterBasedRedColor(){
        if(UserManager.containsUser(player.getName())) {
            if (UserManager.getUserByName(player.getName()).isCheater())
                return "" + ChatColor.RED;
        }
        return "";
    }

    public void onPlayerStandOnRedWool(Location endLocation){
        if(!playerGameplayState.equals(PlayerGameplayState.PARKOURING)) return;
        if(checkpoint.isPlaced()) return;
        if(!staticCheckpoint.playerEnteredAllCheckpoints()){
            if (!playerWasGivenAttention) {
                player.sendMessage(ChatColor.RED + "Pominięto checkpointy!");
                playerWasGivenAttention = true;
            }
            return;
        }

        ParkourPlugin.positionSaver.stopSaving(player, endLocation);
        long playerTime = playerTimer.calculateAccurateTime();

        PlayerEndsParkourEvent event = new PlayerEndsParkourEvent(player, parkourPlayerOn, playerTime);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            playerTimer.resetTimer();

            player.sendMessage(ChatColor.GREEN + "Your time: " +getCheaterBasedRedColor()+ TopListDisplay.timeToString(playerTime));

            teleportTo(parkourPlayerOn);
            ExpManager.levelUp(player);
            TabUtil.refreshTab(player);

            if(startPing <= 180 && player.getPing() <= 180) {
                List<TopLine> topListWithCheaters = parkourPlayerOn.getTopListObject()
                        .getTopList(false, true, false);

                //TopLine previousBestTop = TopListDisplay.getBestTimeOfPlayer(player,TopListDisplay.getNotCheatedTimesExcept(player,topListWithCheaters));
                TopLine previousBestTop = TopListDisplay.getBestTimeOfPlayer(player,topListWithCheaters); /// zamienic
                if (previousBestTop != null) {
                    if (playerTime < previousBestTop.playerTime)
                        ParkourPlugin.positionSaver.saveToFile(player, parkourPlayerOn.folderName);
                } else ParkourPlugin.positionSaver.saveToFile(player, parkourPlayerOn.folderName);

                displayNewBestTimeInfo(playerTime, previousBestTop);

            }
            parkourPlayerOn.getTopListObject().addTopLine(player, playerTime, startPing);
            TopListDisplay.displayTimesOnScoreboard(player, DisplayingTimesState.ALL_PLAYERS_BEST_TIMES, SortTimesType.TIME);
            TopListDisplay.displayScoreboardToOtherPlayers(parkourPlayerOn, DisplayingTimesState.ALL_PLAYERS_BEST_TIMES, SortTimesType.TIME);
        }
    }
    private void displayNewBestTimeInfo(long playerTime, TopLine previousBestTop){
        TopLine bestTime = TopListDisplay.getBestTime(parkourPlayerOn.getTopListObject().getTopList(false,true,false));
        if (bestTime != null) {//wyslwietlanie best time
            if (playerTime < bestTime.playerTime)
                displayBestTimeInfo(player, previousBestTop, playerTime);
        } else displayBestTimeInfo(player, previousBestTop, playerTime);
    }
    public void onPlayerFails(){
        try {
            new Fail(player.getUniqueId(), System.currentTimeMillis(), player.getLocation()).saveFail(parkourPlayerOn.folderName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (checkpoint.isPlaced()){
            staticCheckpoint.resetReachedCheckpoints();
            ParkourPlugin.positionSaver.stopSaving(player);
            player.teleport(checkpoint.getLocation());
        } else if (staticCheckpoint.placedCheckpointNumber == -1){
            teleportTo(parkourPlayerOn);
        } else player.teleport(staticCheckpoint.getCurrentCheckpointLocation());
    }
    private void displayBestTimeInfo(Player bestPlayer, TopLine previousBestTop, long playerTime){
        boolean cheater = UserManager.getUserByName(player.getName()).isCheater();
        if(!cheater) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (!player.equals(bestPlayer)) {
                    player.sendMessage("> " + ChatColor.AQUA + "Gracz " + bestPlayer.getName() + " ustanowił nowy rekord na mapie " +
                            ChatColor.WHITE + parkourPlayerOn.getName());
                    player.sendMessage("> " + ChatColor.AQUA + "Jego czas to: " + ChatColor.WHITE +
                            TopListDisplay.timeToString(playerTime));
                }
            }
        }
        bestPlayer.sendMessage("> "+ChatColor.AQUA+"Ustanowiłeś nowy rekord na mapie!");
        String timeDifferenceString;
        if (previousBestTop != null) {
            timeDifferenceString = TopListDisplay.timeToString(previousBestTop.playerTime - playerTime);
            bestPlayer.sendMessage("> "+ChatColor.AQUA+"Pobiłeś swój rekord o: "+ ChatColor.WHITE + timeDifferenceString);
        }
        if(cheater) bestPlayer.sendMessage(ChatColor.GRAY + "Niestety zostałeś uznany cheatera i twoje czasy nie wyświetlą się innym");
    }

    @Override
    public void playerStandOnNewBlock(Set<Material> materialList, PlayerMoveEvent event) {
        if (parkourPlayerOn == null) return;

        if(materialList.contains(Material.LIME_WOOL)) {
            try {
                onPlayerStandOnGreenWool();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(materialList.contains(Material.RED_WOOL))
            onPlayerStandOnRedWool(event.getTo());
        else if(parkourPlayerOn.hasAnyBackBlock(materialList) || player.getLocation().getY() < 0) {
            onPlayerFails();
        }
    }
    public class Checkpoint{
        private boolean placed = false;
        private Location location;

        public boolean isPlaced(){
            return placed;
        }
        public void setCheckpoint(){
            if (playerGameplayState.equals(PlayerGameplayState.PARKOURING)) {
                this.location = player.getLocation();
                placed = true;
                playerTimer.resetTimer();
                player.sendMessage(ChatColor.GRAY + "Ustawiono checkpoint");
            } else player.sendMessage(ChatColor.GRAY + "Musisz rozpocząć parkour!");
        }

        public Location getLocation() {
            return location;
        }
        public void reset(){
            placed = false;
        }
    }
}
