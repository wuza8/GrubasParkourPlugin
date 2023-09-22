package aybici.parkourplugin.listeners;

import aybici.parkourplugin.builders.ItemBuilder;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import aybici.parkourplugin.utils.TabUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event){
        Player player = event.getPlayer();
        setItems(player);
        if (!UserManager.containsUser(player.getName()))
            UserManager.createUser(player.getName());
        TabUtil.refreshAllPlayersTab();
    }

    public static void setItems(Player player){
        final ItemStack slimeball = new ItemBuilder(Material.SLIME_BALL, 1).setName(ChatUtil.fixColor("&fUkryj Graczy &c| &fPokaż Graczy")).addLoreLine(ChatUtil.fixColor("&2LPM &fPoprzednia mapa &c| &2PPM &fNastępna mapa")).toItemStack();
        final ItemStack book = new ItemBuilder(Material.BOOK, 1).setName(ChatUtil.fixColor("&bMenu &aParkourów")).addLoreLine(ChatUtil.fixColor("&2PPM &fMenu Parkourów")).toItemStack();
        final ItemStack blazeRod = new ItemBuilder(Material.BLAZE_ROD, 1).setName(ChatUtil.fixColor("&fPoprzednia mapa &c| &2PPM &fNastępna mapa")).addLoreLine(ChatUtil.fixColor("&2LPM &fPoprzednia mapa  &c|  &2PPM &fNastępna mapa")).toItemStack();
        final ItemStack resetItem = new ItemBuilder(Material.NETHER_STAR, 1).setName(ChatUtil.fixColor("&bZacznij &aod Początku")).addLoreLine(ChatUtil.fixColor("&2PPM &fRozpoczęcie mapy od nowa")).toItemStack();
        final ItemStack bedItem = new ItemBuilder(Material.ORANGE_BED, 1).setName(ChatUtil.fixColor("&bUstaw &aCheckpoint")).addLoreLine(ChatUtil.fixColor("&2PPM &fUstawienie checkpointa na mapie")).toItemStack();
        player.getInventory().setItem(0, slimeball);
        player.getInventory().setItem(3, book);
        player.getInventory().setItem(2, blazeRod);
        player.getInventory().setItem(8, resetItem);
        player.getInventory().setItem(4, bedItem);
        player.updateInventory();
    }

}