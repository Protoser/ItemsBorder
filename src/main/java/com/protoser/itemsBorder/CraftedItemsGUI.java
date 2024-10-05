package com.protoser.itemsBorder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CraftedItemsGUI implements CommandExecutor, Listener {

    private final ItemsBorder plugin;
    private final int ITEMS_PER_PAGE = 45;

    public CraftedItemsGUI(ItemsBorder plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendActionBar(ChatColor.GREEN + "" + plugin.getPickedUpInt() + " Items | " + (plugin.getPickedUpInt()*2+1) + " Worldborder");
            openCraftedItemsGUI(player, 0);
            return true;
        }
        return false;
    }

    public void openCraftedItemsGUI(Player player, int page) {
        Set<String> craftedItemsSet = plugin.getPickedUpItems();
        List<String> craftedItems = new ArrayList<>(craftedItemsSet);
        craftedItems.sort(String::compareToIgnoreCase);
        int totalPages = (int) Math.ceil((double) craftedItems.size() / ITEMS_PER_PAGE);

        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + "Crafted Items - Page " + (page + 1) + "/" + totalPages);

        int startIndex = page * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, craftedItems.size());
        for (int i = startIndex; i < endIndex; i++) {
            String itemType = craftedItems.get(i);
            Material material = Material.getMaterial(itemType);
            if (material != null) {
                ItemStack itemStack = new ItemStack(material);
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.GOLD + material.name());
                    itemStack.setItemMeta(meta);
                }
                inventory.addItem(itemStack);
            }
        }

        if (page > 0) {
            inventory.setItem(45, createNavigationItem(ChatColor.YELLOW + "Previous Page", Material.RED_STAINED_GLASS_PANE));
        }
        if (page < totalPages - 1) {
            inventory.setItem(53, createNavigationItem(ChatColor.YELLOW + "Next Page", Material.GREEN_STAINED_GLASS_PANE));
        }
        player.openInventory(inventory);
    }

    private ItemStack createNavigationItem(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatColor.DARK_GREEN + "Crafted Items")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();

            String title = ChatColor.stripColor(event.getView().getTitle());
            int currentPage = 0;

            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Page (\\d+)/(\\d+)");
            java.util.regex.Matcher matcher = pattern.matcher(title);

            if (matcher.find()) {
                try {
                    currentPage = Integer.parseInt(matcher.group(1)) - 1;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "An error occurred while navigating pages. Please try again.");
                    return;
                }
            } else {
                player.sendMessage(ChatColor.RED + "Could not determine the current page.");
                return;
            }

            if (slot == 45 && currentPage > 0) {
                openCraftedItemsGUI(player, currentPage - 1);
            } else if (slot == 53 && currentPage < (plugin.getPickedUpItems().size() / ITEMS_PER_PAGE)) {
                openCraftedItemsGUI(player, currentPage + 1);
            }
        }
    }
}
