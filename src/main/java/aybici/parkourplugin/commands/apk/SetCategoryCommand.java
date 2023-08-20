package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.ParkourCategory;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import aybici.parkourplugin.sessions.ParkourSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCategoryCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);

        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;
        if(!isPlayerOnParkour(player)) return true;

        ParkourCategory category = null;
        try {
            category = ParkourCategoryFacade.get(args[0].toUpperCase());
            session.getParkour().setCategory(category);
            player.sendMessage("Dodano parkour " + session.getParkour().getName() + " do kategorii "+category.getName()+".");
        }
        catch(Exception ex){
            player.sendMessage("Nie ma takiej kategorii!");
            return false;
        }

        player.sendMessage("Id w tej kategorii to: " + session.getParkour().getIdentifier());
        return true;
    }
}
