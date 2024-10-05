package com.protoser.itemsBorder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class ItemsBorder extends JavaPlugin {

    private File itemsFile;
    private FileConfiguration itemsConfig;
    private Set<String> pickedUpItems;

    @Override
    public void onEnable() {
        loadItemList();

        CraftedItemsGUI craftedItemsGUI = new CraftedItemsGUI(this);
        this.getCommand("items").setExecutor(craftedItemsGUI);
        getServer().getPluginManager().registerEvents(craftedItemsGUI, this);

        getServer().getPluginManager().registerEvents(new ItemPickupListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryInteractionListener(this), this);

        for (World world : Bukkit.getWorlds()) {
            world.getWorldBorder().setSize(getPickedUpInt()*4 + 2, 1);
            world.getWorldBorder().setCenter(0.0, 0.0);
        }
    }

    @Override
    public void onDisable() {
        saveItemList();
    }

    private void loadItemList() {
        itemsFile = new File(getDataFolder(), "picked-up-items.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try {
                itemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);
        pickedUpItems = new HashSet<>(itemsConfig.getStringList("picked-items"));
    }

    public void saveItemList() {
        itemsConfig.set("picked-items", new ArrayList<>(pickedUpItems));
        try {
            itemsConfig.save(itemsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasItemBeenPickedUp(String itemType) {
        return pickedUpItems.contains(itemType);
    }

    public void addPickedUpItem(String itemType) {
        pickedUpItems.add(itemType);
        saveItemList();
    }

    public Set<String> getPickedUpItems() {
        return pickedUpItems;
    }

    public int getPickedUpInt() {
        return pickedUpItems.size();
    }

    public void updateWorldborder(String itemType) {
        for (World world : Bukkit.getWorlds()) {
            world.getWorldBorder().setSize(getPickedUpInt()*4 + 2, 1);
        }
        addPickedUpItem(itemType);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendActionBar(ChatColor.GREEN + "A new item has been discovered: " + itemType);
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
}
