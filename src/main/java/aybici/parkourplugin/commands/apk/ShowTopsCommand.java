package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.commands.arguments.ArgumentManager;
import aybici.parkourplugin.commands.arguments.BooleanArgument;
import aybici.parkourplugin.commands.arguments.DominantStringArgument;
import aybici.parkourplugin.parkours.TopList;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowTopsCommand extends AdminParkourCommand implements CommandExecutor {
    private BooleanArgument showAllHidden;
    private DominantStringArgument playerName;
    private boolean parseArgs(String[] args){
        showAllHidden = new BooleanArgument("all", false);
        playerName = new DominantStringArgument(false);
        ArgumentManager argumentManager = new ArgumentManager();
        argumentManager.addArgument(showAllHidden);
        argumentManager.addArgument(playerName);
        return argumentManager.parseAllArgs(args);
    }
    private void sendUsageMessage(Player player){
        player.sendMessage("/apk showtop <\"all\"/player>");
    }
    private boolean checkArgsCorrect(Player player){
        if(playerName.isSpecified() && showAllHidden.getValue()){
            sendUsageMessage(player);
            player.sendMessage("Cannot specify both player and \"all\"");
            return false;
        }
        if((!playerName.isSpecified()) && !showAllHidden.getValue()){
            sendUsageMessage(player);
            player.sendMessage("Please specify player or \"all\"");
            return false;
        }
        return true;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.deletePermission)) return true;
        if (!isPlayerOnParkour(player)) return true;

        TopList topListObject = session.getParkour().getTopListObject();

        boolean isArgsOk = parseArgs(args);
        if(!isArgsOk){
            sendUsageMessage(player);
            return true;
        }
        if(!checkArgsCorrect(player)) return true;

        int showedLines;
        if(playerName.isSpecified()) {
            OfflinePlayer playerToShow = Bukkit.getOfflinePlayer(playerName.getValue());
            showedLines = topListObject.showTopLinesOfPlayer(playerToShow);
            player.sendMessage("Pokazano wszystkie topki gracza " + playerToShow.getName() + ": " + showedLines);
            return true;
        }
        if(showAllHidden.getValue()){
            showedLines = topListObject.showAllTopLines();
            player.sendMessage("Pokazano wszystkie topki: " + showedLines);
            return true;
        }
        return false;
    }
}
