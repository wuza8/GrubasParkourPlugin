package aybici.parkourplugin.commands.times;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.DisplayingTimesState;
import aybici.parkourplugin.parkours.SortTimesType;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrintTimesCommand implements CommandExecutor {
    private DisplayingTimesState displayingTimesState;

    public PrintTimesCommand(DisplayingTimesState displayingTimesState){
        this.displayingTimesState = displayingTimesState;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        Player player = (Player) sender;
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        int page = 1;
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
        if (args.length == 2) page = Integer.parseInt(args[1]);

        return ParkourPlugin.topListDisplay.printTopList(player,
                session.getParkour().getTopListObject().getTopList(), displayingTimesState, sortTimesType, page);
    }
}
