package aybici.parkourplugin.listeners;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.itembuilder.ItemBuilder;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import com.connorlinfoot.titleapi.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event){
        Player player = event.getPlayer();
        setItems(player);
        if (!UserManager.containsUser(player.getName()))
            UserManager.createUser(player.getName());
        TitleAPI.sendTabTitle(player, "Header", "Footer");
    }

    public static void setItems(Player player){
        final ItemStack slimeball = new ItemBuilder(Material.SLIME_BALL, 1).setName(ChatUtil.fixColor("&fUkryj Graczy &c| &fPokaż Graczy")).toItemStack();
        final ItemStack book = new ItemBuilder(Material.BOOK, 1).setName("§bMenu Parkourow - /pk kategoria numer").addLoreLine("developing").toItemStack();
        final ItemStack blazeRod = new ItemBuilder(Material.BLAZE_ROD, 1).setName("§bPoprzedni/Następny parkour").addLoreLine("left/right click").toItemStack();
        final ItemStack resetItem = new ItemBuilder(Material.NETHER_STAR, 1).setName("§bZacznij od początku").addLoreLine("reset parkoura").toItemStack();
        final ItemStack bedItem = new ItemBuilder(Material.BLUE_BED, 1).setName(ChatUtil.fixColor("&bUstaw checkpoint")).addLoreLine("Jezeli chcesz ustawić checkpoint użyj &aRPM").toItemStack();
        player.getInventory().setItem(0, slimeball);
        player.getInventory().setItem(3, book);
        player.getInventory().setItem(2, blazeRod);
        player.getInventory().setItem(8, resetItem);
        player.getInventory().setItem(4, bedItem);
        player.updateInventory();
    }
}