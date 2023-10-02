package aybici.parkourplugin.commands;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.listeners.InventoryInteractListener;
import aybici.parkourplugin.parkours.ParkourCategory;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static aybici.parkourplugin.commands.apk.AdminParkourCommand.SenderHasPermission;

public class PkCatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if(args.length < 1){
            player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + "Uźycie: pkcat <nazwa_kategorii>");
            return false;
        }

        ParkourCategory category = ParkourCategoryFacade.get(args[0]);
      
        if(!category.getName().equals("UNKNOWN") || SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)){
            player.openInventory(InventoryInteractListener.getCategoryInventory(category, player , 1));
            return true;
        }

        player.sendMessage(ChatColor.AQUA + ">" + ChatColor.GREEN+ "> " + "Kategoria o nazwie "+ args[0] + " nie istnieje!");
        return false;
    }
}
