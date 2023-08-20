package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RemoveCategoryCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;

        try{
            String name = args[0];
            ParkourCategoryFacade.removeCategory(name);
            ParkourCategoryFacade.saveCategories();
        }
        catch (Exception ex){
            sender.sendMessage("Coś zrobiłeś nie tak, popraw komendę!");
        }

        return true;
    }
}
