package aybici.parkourplugin.commands.times;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.commands.OnParkourCommand;
import aybici.parkourplugin.parkours.*;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PrintTimesCommand extends OnParkourCommand implements CommandExecutor {
    private DisplayingTimesState displayingTimesState;

    public PrintTimesCommand(DisplayingTimesState displayingTimesState){
        this.displayingTimesState = displayingTimesState;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        Player player = (Player) sender;
        if (!isPlayerOnParkour(player)) return true;
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        int page = 1;
        boolean showHiddenTimes = false;
        SortTimesType sortTimesType;
        if (args.length > 0)
        switch (args[0]){
            case "time":
                sortTimesType = SortTimesType.TIME;
                break;
            case "players":
                sortTimesType = SortTimesType.PLAYERS;
                break;
            case "date":
                sortTimesType = SortTimesType.DATE;
                break;
            default:
                sender.sendMessage("Niepoprawny argument (time, date, players), sortowanie domyślne wg daty");
                sortTimesType = SortTimesType.DATE;
        } else sortTimesType = SortTimesType.DATE;
        if (args.length > 1) page = Integer.parseInt(args[1]);
        if (args.length > 2) if(args[2].equals("showHidden")) showHiddenTimes = true;

        List<TopLine> topListWithHidden = session.getParkour().getTopListObject().getTopList();
        List<TopLine> topList;
        if(showHiddenTimes) topList = topListWithHidden;
        else topList = TopListDisplay.getNotHiddenTimes(topListWithHidden);

        return TopListDisplay.printTopList(player,topList
                , displayingTimesState, sortTimesType, page);
    }
}
