package aybici.parkourplugin.usableblocks;

import aybici.parkourplugin.itembuilder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UsableItemBuilder {

    private List<String> loreData;

    private ItemBuilder itemBuilder;

    public UsableItemBuilder(Material m) {
        itemBuilder = new ItemBuilder(m, 1);
    }

    public UsableItemBuilder(ItemStack itemStack) {
        itemBuilder = new ItemBuilder(itemStack);
    }

    public UsableItemBuilder(Material m, int amount) {
        itemBuilder = new ItemBuilder(m, amount);
    }

    public ItemBuilder toItemBuilder(){
        return itemBuilder;
    }

    public UsableItemBuilder sendCommand(String signal) {
        itemBuilder.addLoreLine("/" + signal);
        return this;
    }
    public UsableItemBuilder sendEvent(String signal) {
        itemBuilder.addLoreLine("$" + signal);
        return this;
    }
}
