package com.protoser.itemsBorder;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryInteractionListener implements Listener {

    private final ItemsBorder plugin;

    public InventoryInteractionListener(ItemsBorder plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(ChatColor.DARK_GREEN + "Crafted Items")) {
            if (event.getWhoClicked() instanceof Player) {
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null && clickedItem.getType() != org.bukkit.Material.AIR) {
                    String itemType = clickedItem.getType().toString();
                    if (!plugin.hasItemBeenPickedUp(itemType)) {
                        plugin.updateWorldborder(itemType);
                    }
                }
            }
        }
    }
}
