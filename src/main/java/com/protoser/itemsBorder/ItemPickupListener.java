package com.protoser.itemsBorder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.entity.Player;

public class ItemPickupListener implements Listener {

    private final ItemsBorder plugin;

    public ItemPickupListener(ItemsBorder plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            String itemType = event.getItem().getItemStack().getType().toString();
            if (!plugin.hasItemBeenPickedUp(itemType)) {
                plugin.updateWorldborder(itemType);
            }
        }
    }
}
