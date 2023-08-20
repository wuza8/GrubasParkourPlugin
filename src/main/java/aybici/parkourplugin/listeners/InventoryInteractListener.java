package aybici.parkourplugin.listeners;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.itembuilder.ItemBuilder;
import aybici.parkourplugin.parkours.Parkour;
import aybici.parkourplugin.parkours.ParkourCategory;
import aybici.parkourplugin.parkours.ParkourCategoryFacade;
import aybici.parkourplugin.parkours.ParkourSet;
import aybici.parkourplugin.sessions.ParkourSession;
import aybici.parkourplugin.sessions.PositionSaver;
import aybici.parkourplugin.usableblocks.UsableItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InventoryInteractListener implements Listener {

    @EventHandler
    public void onInteract(final PlayerInteractEvent event){
        final Player player = event.getPlayer();
        Material materialInHand = player.getInventory().getItemInMainHand().getType();
        if(materialInHand == Material.BOOK)
            onBookClick(player);
        else if(materialInHand == Material.BLAZE_ROD)
            onBlazeRodClick(event, player);
        else if(materialInHand == Material.RED_DYE)
            onDemoQuitItemClick(player);
        else if(materialInHand == Material.NETHER_STAR)
            onResetItemClick(player);
        else if(materialInHand == Material.BLUE_BED)
            onBedClick(player);
        else if(materialInHand == Material.SLIME_BALL)
            onSlimeballClick((Event)event, player);
    }
    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent event) {
        onInventoryInteract(event);
    }


    private void onBookClick(final Player player){
        player.openInventory(getMenuInventory());
    }
    private void onBlazeRodClick(final Event event,final Player player){
        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;

        if (player.hasCooldown(Material.BLAZE_ROD)){
            return;
        }
        player.setCooldown(Material.BLAZE_ROD, 60);
        Parkour parkour = ParkourPlugin.parkourSessionSet.getSession(player).getParkour();
        Parkour parkour2 = null;
        if (parkour == null) {
            player.sendMessage("Musisz dołączyć do parkour!");
            return;
        }
        if(playerInteractEvent.getAction() == Action.LEFT_CLICK_AIR || playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK){
            parkour2 = ParkourPlugin.parkourSet.getPreviousParkour(parkour);
        }
        if(playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK){
            parkour2 = ParkourPlugin.parkourSet.getNextParkour(parkour);
        }
        String command = "pk " + parkour2.getName();
        player.performCommand(command);
    }
    private void onDemoQuitItemClick(final Player player){
        if(PositionSaver.isPlayerWatching(player))
            PositionSaver.unsetPlayerWatching(player);
    }
    private void onResetItemClick(final Player player){
        ParkourSession session = ParkourPlugin.parkourSessionSet.getSession(player);
        if(session.isPlayerOnParkour()){
            session.teleportTo(session.getParkour());
        }
    }

    private void onBedClick(final Player player){
        player.chat("/cp");
    }
    private Inventory getMenuInventory(){
        Inventory inventory = Bukkit.getServer().createInventory(null, 9*6);
        int i = 0;
        for (ParkourCategory parkourCategory : ParkourCategoryFacade.getAllCategories()) {
            if(parkourCategory.getBookPosition() < 0) continue;

            final ItemStack item = new UsableItemBuilder(parkourCategory.getCategoryMaterial(), 1)
                    .sendCommand("pkcat "+parkourCategory.getName())
                    .toItemBuilder()
                    .setName("§b" + parkourCategory.getDisplayName().replace("_", " "))
                    .toItemStack();
            inventory.setItem(parkourCategory.getBookPosition(), item);
            i++;
        }
        return inventory;
    }


    public static Inventory getCategoryInventory(ParkourCategory category, int page){
        int SIZE = 54;
        Inventory inventory = Bukkit.getServer().createInventory(null, SIZE);
        Material parkourMaterial = category.getCategoryMaterial();
        ParkourSet parkourSet = ParkourPlugin.parkourSet;
        int maxIDOfCategory = parkourSet.getMaxIdentifierOfCategory(category);
        int shift = 1;
        int parkourID;
        boolean nextPageExists = true;
        boolean previousPageExists = true;
        if (page == 1) previousPageExists = false;
        for (int i = 0; i <= SIZE - 3 ;i ++){
            parkourID = i + shift + (page - 1) * (SIZE - 2);
            if (parkourID > maxIDOfCategory) {
                nextPageExists = false;
                break;
            }
            if (!parkourSet.categoryContainsIdentifier(category, parkourID)) continue;
            Parkour parkour = parkourSet.getParkourByCategoryAndID(category,parkourID);
            final ItemStack item = new UsableItemBuilder(parkourMaterial, 1).
                    sendCommand("pk " + category.getName() + " "+parkourID).
                    toItemBuilder().
                    setName("§b" + parkour.getName()).
                    addLoreLine("§b" + parkour.getExp() + " Exp").toItemStack();
            inventory.setItem(i, item);
        }
        if (nextPageExists) {
            final ItemStack arrow = new ItemBuilder(Material.ARROW, 1).setName("§bKolejna strona").addLoreLine(category.getName() + "#" + (page+1)).toItemStack();
            inventory.setItem(SIZE - 1,arrow);
        }
        if (previousPageExists) {
            final ItemStack arrow = new ItemBuilder(Material.ARROW, 1).setName("§bPoprzednia strona").addLoreLine(category.getName() + "#" + (page-1)).toItemStack();
            inventory.setItem(SIZE - 2, arrow);
        }
        return inventory;
    }

    private void onInventoryInteract(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) {
            return;
        }
        Material material = event.getCurrentItem().getType();
//
//        for (ParkourCategory parkourCategory : ParkourCategoryFacade.getAllCategories()){
//            if(parkourCategory.getCategoryMaterial() == material) {
//                player.openInventory(getCategoryInventory(parkourCategory, 1));
//                return;
//            }
//        }

        if (material == Material.ARROW){
            String loreLine = event.getCurrentItem().getItemMeta().getLore().get(0);
            List<String> convertedLine = Stream.of(loreLine.split("#", -1))
                    .collect(Collectors.toList());
            ParkourCategory category = ParkourCategoryFacade.get(convertedLine.get(0));
            int page = Integer.parseInt(convertedLine.get(1));
            player.openInventory(getCategoryInventory(category, page));
        }
    }

    private void onSlimeballClick(final Event event, final Player player){
        if(((PlayerInteractEvent)event).getAction() == Action.LEFT_CLICK_AIR){
            if(player.getItemInHand() == null){
                return;
            }
            if(player.getItemInHand().getType() == Material.SLIME_BALL){
                player.chat("/hide on");
            }
        }
        if(((PlayerInteractEvent)event).getAction() == Action.LEFT_CLICK_BLOCK){
            if(player.getItemInHand() == null){
                return;
            }
            if(player.getItemInHand().getType() == Material.SLIME_BALL){
                player.chat("/hide on");
            }
        }
        if(((PlayerInteractEvent)event).getAction() == Action.RIGHT_CLICK_AIR){
            if(player.getItemInHand() == null){
                return;
            }
            if(player.getItemInHand().getType() == Material.SLIME_BALL){
                player.chat("/hide off");
            }
        }
        if(((PlayerInteractEvent)event).getAction() == Action.RIGHT_CLICK_BLOCK){
            if(player.getItemInHand() == null){
                return;
            }
            if(player.getItemInHand().getType() == Material.SLIME_BALL){
                player.chat("/hide off");
            }
        }
    }
}