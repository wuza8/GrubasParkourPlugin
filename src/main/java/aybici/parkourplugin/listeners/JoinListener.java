package aybici.parkourplugin.listeners;

import aybici.parkourplugin.builders.ItemBuilder;
import aybici.parkourplugin.parkourevents.ParkourEventsFacade;
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

        ParkourEventsFacade.displayBossBarToPlayer(player);
    }

    public static void setItems(Player player){
        final ItemStack slimeball = new ItemBuilder(Material.SLIME_BALL, 1).setName(ChatUtil.fixColor("&cUkryj Graczy &4| &aPokaż Graczy")).addLoreLine(ChatUtil.fixColor("&2LPM &fUkrywanie graczy &4| &2PPM &fPokazanie graczy")).toItemStack();
        final ItemStack book = new ItemBuilder(Material.BOOK, 1).setName(ChatUtil.fixColor("&bMenu &aParkourów")).addLoreLine(ChatUtil.fixColor("&2PPM &fMenu Parkourów")).toItemStack();
        final ItemStack blazeRod = new ItemBuilder(Material.BLAZE_ROD, 1).setName(ChatUtil.fixColor("&bPoprzednia mapa &4| &aNastępna mapa")).addLoreLine(ChatUtil.fixColor("&2LPM &fPoprzednia mapa &4| &2PPM &fNastępna mapa")).toItemStack();
        final ItemStack resetItem = new ItemBuilder(Material.NETHER_STAR, 1).setName(ChatUtil.fixColor("&bZacznij &aod Początku")).addLoreLine(ChatUtil.fixColor("&2PPM &fRozpoczęcie mapy od nowa")).toItemStack();
        final ItemStack bedItem = new ItemBuilder(Material.ORANGE_BED, 1).setName(ChatUtil.fixColor("&bUstaw &aCheckpoint")).addLoreLine(ChatUtil.fixColor("&2PPM &fUstawienie checkpointa na mapie")).toItemStack();
        final ItemStack clockItem = new ItemBuilder(Material.CLOCK, 1).setName(ChatUtil.fixColor("&bTeleportuj na &aEvent")).addLoreLine(ChatUtil.fixColor("&2PPM &fTeleportacja na aktywny Event")).toItemStack();
        player.getInventory().setItem(0, slimeball);
        player.getInventory().setItem(3, book);
        player.getInventory().setItem(2, blazeRod);
        player.getInventory().setItem(8, resetItem);
        player.getInventory().setItem(4, bedItem);
        player.getInventory().setItem(7, clockItem);
        player.updateInventory();
    }

}