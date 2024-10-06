package com.protoser.itemsBorder;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage(ChatColor.DARK_GREEN + "=== ItemsBorder Help ===");
            player.sendMessage(ChatColor.YELLOW + "/items -" + ChatColor.WHITE + " - Just show progress, no GUI.");
            player.sendMessage(ChatColor.YELLOW + "/items" + ChatColor.WHITE + " - Just open GUI.");
            player.sendMessage(ChatColor.YELLOW + "/items [page_number]" + ChatColor.WHITE + " - Go to a specific page of items.");
            player.sendMessage(ChatColor.YELLOW + "/items <letter>" + ChatColor.WHITE + " - Filter items starting with the specified characters.");
            player.sendMessage(ChatColor.YELLOW + "/items <page_number> <letter>" + ChatColor.WHITE + " - Go to a specific page and filter items by the specified letter.");
            player.sendMessage(ChatColor.YELLOW + "/help" + ChatColor.WHITE + " - Show this help message.");
            player.sendMessage(ChatColor.DARK_GREEN + "=========================");

            return true;
        }
        return false;
    }
}