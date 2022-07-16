package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DescriptionCommand extends AdminParkourCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;
        if (!isPlayerOnParkour(player)) return true;
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        Parkour parkour = session.getParkour();
        boolean append = false;
        StringBuilder description = new StringBuilder();
        int startWordNumber = 0;

        if(args.length > 0)
            if(args[0].equals("append")) {
                append = true;
                startWordNumber = 1;
            }

        for (int wordNumber = startWordNumber; wordNumber < args.length; wordNumber++){
            description.append(args[wordNumber]).append(" ");
        }
        if (append)
            parkour.setDescription(parkour.getDescription() + " " + description);
        else
            parkour.setDescription(description.toString());
        player.sendMessage("Opis mapy został zaktualizowany!");
        return true;
    }
}
