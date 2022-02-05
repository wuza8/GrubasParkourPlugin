package aybici.parkourplugin.commands.pklist;

import aybici.parkourplugin.parkours.ParkourCategory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArgsHandler {
    public int pageNumber = 1;
    public ParkourCategory category;

    public boolean handleArgs(String[] args, Player player){
        if (args.length > 2) {
            player.sendMessage(ChatColor.RED + "Zbyt duzo argumentów!");
            return false;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Brak argumentów!" + ChatColor.AQUA + " Wyświetlam wszystkie kategorie:");
            category = null;
            return true;
        }
        if (args[0].equals("all")) {
            category = null;
            player.sendMessage(ChatColor.AQUA + "Wyświetlam wszystkie kategorie:");
        }
        else {
            boolean invalidCategoryName = true;
            for (ParkourCategory parkourCategory : ParkourCategory.values()){
                if (parkourCategory.name().equalsIgnoreCase(args[0])) {
                    invalidCategoryName = false;
                    break;
                }
            }
            if(invalidCategoryName) {
                player.sendMessage("Nie ma kategorii \"" + args[0] + "\"");
                return false;
            }
            category = ParkourCategory.valueOf(args[0].toUpperCase());
            player.sendMessage(ChatColor.AQUA + "Wyświetlam kategorię " + category.toString().toLowerCase() + ":");
        }

        if (args.length == 2){
                pageNumber = Integer.parseInt(args[1]);
        }
        return true;
    }
}
