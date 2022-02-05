package aybici.parkourplugin.commands.pklist;

import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Displayer {
    public void display(Player player, List<Parkour> parkourListToDisplay, int pageNumber){
        if (pageNumber <= 0) {
            player.sendMessage(ChatColor.RED + "Numer strony musi być dodatni!");
            return;
        }
        final int LINES_PER_PAGE = 10;
        int numberOfPages = parkourListToDisplay.size()/LINES_PER_PAGE;
        int lastPageLength = parkourListToDisplay.size() % LINES_PER_PAGE;
        if (lastPageLength != 0) numberOfPages++;
        if (pageNumber > numberOfPages) {
            player.sendMessage(ChatColor.RED + "Maksymalna strona to " + numberOfPages + "!");
            return;
        }

        int startIndex = LINES_PER_PAGE*(pageNumber - 1);
        StringBuilder pageToDisplay = new StringBuilder();
        if (pageNumber != numberOfPages || lastPageLength == 0) {
            for (int index = startIndex; index < startIndex + LINES_PER_PAGE; index++) {
                pageToDisplay.append(parkourListToDisplay.get(index).getName());
                if (index < startIndex + LINES_PER_PAGE - 1) pageToDisplay.append(", ");
            }
        }
        else {
            for (int index = startIndex; index < startIndex + lastPageLength; index++) {
                pageToDisplay.append(parkourListToDisplay.get(index).getName());
                if (index < startIndex + lastPageLength - 1) pageToDisplay.append(", ");
            }
        }
        player.sendMessage(ChatColor.AQUA + "strona nr " + pageNumber + " z " + numberOfPages);
        player.sendMessage(pageToDisplay.toString());
    }
}
