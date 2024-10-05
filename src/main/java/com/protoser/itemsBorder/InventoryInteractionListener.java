package com.protoser.itemsBorder;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {

    private final ItemsBorder plugin;

    public CraftItemListener(ItemsBorder plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        // Check if the crafting involves a player
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ItemStack craftedItem = event.getRecipe().getResult();
            String itemType = craftedItem.getType().toString();

            // Check if the item type has already been crafted
            if (!plugin.hasItemBeenPickedUp(itemType)) {
                double size;
                World overworld = Bukkit.getWorlds().stream()
                        .filter(world -> world.getEnvironment() == World.Environment.NORMAL)
                        .findFirst()
                        .orElse(null);
                if (overworld != null) {
                    size = overworld.getWorldBorder().getSize();
                    // Execute custom code for newly crafted item types
                    for (World world : Bukkit.getWorlds()) {
                        world.getWorldBorder().setSize(size+5);
                    }
                }
                
                // Add the item type to the global list
                plugin.addPickedUpItem(itemType);
                // Custom action when a new item is crafted
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendActionBar(ChatColor.GREEN + "A new item has been discovered: " + itemType);
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
            }
        }
    }
}
