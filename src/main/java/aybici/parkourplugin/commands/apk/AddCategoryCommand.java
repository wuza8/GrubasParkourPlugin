package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.ParkourCategory;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddCategoryCommand  extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;

        try{
            String name = args[0];
            String displayName = args[1];
            int xpMultiplier = Integer.parseInt(args[2]);
            Material materialName = Material.matchMaterial(args[3]);
            int bookNumber = Integer.parseInt(args[4]);
            int minLevel = Integer.parseInt(args[5]);

            if(materialName == null) {
                sender.sendMessage("Materiał "+args[3]+" nie istnieje!");
                return false;
            }

            ParkourCategoryFacade.addCategory(new ParkourCategory(name, displayName, xpMultiplier, materialName, bookNumber, minLevel));
            ParkourCategoryFacade.saveCategories();
        }
        catch (Exception ex){
            sender.sendMessage("Coś zrobiłeś nie tak, popraw komendę!");
            ex.printStackTrace();
        }

        return true;
    }
}
