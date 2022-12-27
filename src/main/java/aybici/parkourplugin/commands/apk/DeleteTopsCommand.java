package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.commands.arguments.ArgumentManager;
import aybici.parkourplugin.commands.arguments.BooleanArgument;
import aybici.parkourplugin.commands.arguments.DominantStringArgument;
import aybici.parkourplugin.parkours.TopLine;
import aybici.parkourplugin.parkours.TopList;
import aybici.parkourplugin.parkours.TopListDisplay;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class DeleteTopsCommand extends AdminParkourCommand implements CommandExecutor  {
    private BooleanArgument deleteDemoFile;
    private BooleanArgument deleteOnlyBest;
    private DominantStringArgument playerName;
    private BooleanArgument deleteAllOnParkour;
    private BooleanArgument safeDelete;
    private OfflinePlayer playerToRemove;
    private File demoFile;
    private String deletedOrHidden;
    private ParkourSession session;
    private Player player;
    private TopList topListObject;
    private List<TopLine> topList;
    private File demoFolder;


    private boolean parseArgs(String[] args){
        deleteDemoFile = new BooleanArgument("demo", false);
        deleteOnlyBest = new BooleanArgument("best", false);
        playerName = new DominantStringArgument(false);
        deleteAllOnParkour = new BooleanArgument("all", false);
        safeDelete = new BooleanArgument("permanently", true);
        ArgumentManager argumentManager = new ArgumentManager();
        argumentManager.addArgument(deleteDemoFile);
        argumentManager.addArgument(deleteOnlyBest);
        argumentManager.addArgument(playerName);
        argumentManager.addArgument(deleteAllOnParkour);
        argumentManager.addArgument(safeDelete);
        return argumentManager.parseAllArgs(args);
    }
    private void sendUsageMessage(){
        player.sendMessage(ChatColor.GREEN + "/apk deltop "+
                "<playerName/\"all\"> [\"best\"] [\"demo\"] [\"permanently\"]\n"+ ChatColor.WHITE +
                "deletes times on parkour");
        player.sendMessage("Specify valid arguments, order doesn't matter");

        player.sendMessage(ChatColor.GREEN + "playerName" + ChatColor.DARK_GREEN +" - specify name of player to delete times");
        player.sendMessage(ChatColor.GREEN + "\"all\"" + ChatColor.DARK_GREEN +" - delete all times on parkour, if \"best\" specified, delete only best time of all times");
        player.sendMessage(ChatColor.GREEN + "\"best\"" + ChatColor.DARK_GREEN +" - delete only best time of player");
        player.sendMessage(ChatColor.GREEN + "\"demo\"" + ChatColor.DARK_GREEN +" - delete also demo file, default: no");
        player.sendMessage(ChatColor.GREEN + "\"permanently\"" + ChatColor.DARK_GREEN +" - delete times permanently, only rycerz125 can do");
    }
    private boolean checkArgsCorrect(){
        if(playerName.isSpecified() && deleteAllOnParkour.isSpecified()) {
            player.sendMessage("Cannot specify both player and \"all\".");
            sendUsageMessage();
            return false;
        }
        if((!playerName.isSpecified()) && (!deleteAllOnParkour.isSpecified())){
            player.sendMessage("please specify player or type \"all\".");
            sendUsageMessage();
            return false;
        }
        if(!safeDelete.getValue()){
            if(!player.getName().equals("rycerz125")) {
                player.sendMessage("tylko rycerz może trwale usuwać czasy, choć nie ma powodu, aby tak robić");
                return false;
            }
        }
        return true;
    }
    private void assignArgumentsBasedFields(){
        if(playerName.isSpecified()) {
            playerToRemove = ParkourPlugin.getInstance().getServer().getOfflinePlayer(playerName.getValue());
            demoFile = new File(session.getParkour().folderName + File.separator + "demos"+File.separator  + playerToRemove.getName() + ".txt");
        }

        if(safeDelete.getValue())
            deletedOrHidden = "Ukryto";
        else
            deletedOrHidden = "Usunięto";

    }
    private void deleteAllDemos(){
        boolean succeed = false;
        int filesNumber = demoFolder.listFiles().length;
        for (File file : demoFolder.listFiles())
            if(file.delete()) succeed = true;

        if(succeed) {
            player.sendMessage("Usunięto wszystkie dema: " + filesNumber);
        } else {
            player.sendMessage("Brak dem do usunięcia");
        }
    }
    private void deleteBestDemo(){
        OfflinePlayer demoPlayer;
        if(TopListDisplay.getBestTime(topList) != null) {
            demoPlayer = TopListDisplay.getBestTime(topList).player;
            if(new File(session.getParkour().folderName + File.separator + "demos"+File.separator  + demoPlayer.getName() + ".txt")
                    .delete())
                player.sendMessage("Usunięto najlepsze demo, gracz: " + demoPlayer.getName());
            else player.sendMessage("Brak dema gracza "+demoPlayer.getName()+" do usunięcia");
        }
    }
    private void deleteDemoOfSpecifiedPlayer(){
        if (demoFile.exists())
            if(demoFile.delete()) {
                player.sendMessage("Usunięto demo gracza " + playerToRemove.getName());
            } else {
                player.sendMessage("Brak dema do usunięcia");
            }
    }
    private void callDemoDeletion(){
        if(playerName.isSpecified()){
            deleteDemoOfSpecifiedPlayer();
        } else
        if(deleteAllOnParkour.getValue()) {
            if (demoFolder.exists() && !deleteOnlyBest.getValue()) {
                deleteAllDemos();
            }
            else {
                deleteBestDemo();
            }
        }
    }
    private void deleteAllTimes(){
        int size = topListObject.getTopList().size();
        if(safeDelete.getValue())
            topListObject.hideTopList();
        else
            topListObject.clearTopList(false);

        topListObject.saveTopList();
        player.sendMessage(deletedOrHidden + " wszystkie czasy na tym parkourze: " + size);
    }
    private void deleteAllTimesOfPlayer(){
        int times = TopListDisplay.getAllTimesOfPlayer(playerToRemove, topList).size();
        if(times == 0){
            player.sendMessage("Brak czasów gracza " + playerToRemove.getName());
            return;
        }

        if(safeDelete.getValue())
            for (TopLine topLine : TopListDisplay.getAllTimesOfPlayer(playerToRemove, topList))
                topListObject.hideTopLine(topLine);
        else
            topListObject.removeAllTimesOfPlayer(player,false);

        topListObject.saveTopList();

        player.sendMessage(deletedOrHidden + " czasy gracza " + playerToRemove.getName() + ": " + times);
    }
    private void deleteBestTimeOfAll(){
        if(safeDelete.getValue())
            topListObject.hideTopLine(TopListDisplay.getBestTime(topList));
        else
            topListObject.removeTopLine(TopListDisplay.getBestTime(topList),false);

        topListObject.saveTopList();
        player.sendMessage(deletedOrHidden + " najlepszy czas na mapie.");
    }
    private void deleteBestTimeOfSpecifiedPlayer(){

        boolean succeed;

        if(safeDelete.getValue())
            succeed = topListObject.hideTopLine(TopListDisplay.getBestTimeOfPlayer(playerToRemove, topList));
        else
            succeed = topListObject.removeTopLine(TopListDisplay.getBestTimeOfPlayer(playerToRemove, topList),false);

        topListObject.saveTopList();

        if (succeed){
            player.sendMessage(deletedOrHidden + " najlepszy czas gracza " + playerToRemove.getName());
        } else
        player.sendMessage("Brak czasów gracza " + playerToRemove.getName());
    }
    private boolean callTimesDeletion(){
        if(deleteAllOnParkour.getValue() && !deleteOnlyBest.getValue()){
            deleteAllTimes();
            return true;
        }

        if(playerName.isSpecified() && !deleteOnlyBest.getValue()){
            deleteAllTimesOfPlayer();
            return true;
        }

        if(deleteAllOnParkour.getValue() && deleteOnlyBest.getValue()){
            deleteBestTimeOfAll();
            return true;
        }

        if(playerName.isSpecified() && deleteOnlyBest.getValue()){
            deleteBestTimeOfSpecifiedPlayer();
            return true;
        }
        return false;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        player = (Player) sender;
        session = ParkourPlugin.parkourSessionSet.getSession(player);

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.deletePermission)) return true;
        if (!isPlayerOnParkour(player)) return true;
        topList = session.getParkour().getTopListObject().getTopList();
        topListObject = session.getParkour().getTopListObject();
        demoFolder = new File(session.getParkour().folderName + File.separator + "demos");


        // sprawdzanie poprawnosci wyboru argumentów
        boolean isArgsOk = parseArgs(args);
        if(!isArgsOk) {
            sendUsageMessage();
            return true;
        }
        if(!checkArgsCorrect()) return true;

        assignArgumentsBasedFields();
        if(deleteDemoFile.getValue())
            callDemoDeletion();

        if(topList.size() == 0){
            player.sendMessage("Brak czasów do usunięcia");
            return true;
        }

        return callTimesDeletion();
    }
}
