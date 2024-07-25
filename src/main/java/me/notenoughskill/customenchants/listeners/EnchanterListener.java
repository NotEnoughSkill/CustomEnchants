package me.notenoughskill.customenchants.listeners;

import me.notenoughskill.customenchants.items.SoulBoundHandler;
import me.notenoughskill.customenchants.utils.EnchanterUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EnchanterListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!ChatColor.stripColor(inventory.getViewers().get(0).getOpenInventory().getTitle()).equalsIgnoreCase("Enchanter")) {
            return;
        }

        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int playerXP = player.getTotalExperience();
        String itemName = clickedItem.getItemMeta().getDisplayName();

        if (itemName.equals(ChatColor.GREEN + "Basic Book")) {
            if (playerXP >= 70000) {
                player.giveExp(-70000);
                player.getInventory().addItem(EnchanterUtils.createBookGenerator(ChatColor.GREEN + "Basic Book Generator",
                        "Generates a random Basic Book"));
                player.sendMessage(ChatColor.GREEN + "You have purchased a Basic Book!");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have enough XP!");
            }
        } else if (itemName.equals(ChatColor.BLUE + "Advanced Book")) {
            if (playerXP >= 150000) {
                player.giveExp(-150000);
                player.getInventory().addItem(EnchanterUtils.createBookGenerator(ChatColor.BLUE + "Advanced Book Generator",
                        "Generates a random Advanced Book"));
                player.sendMessage(ChatColor.GREEN + "You have purchased a Advanced Book!");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have enough XP!");
            }
        } else if (itemName.equals(ChatColor.RED + "Extreme Book")) {
            if (playerXP >= 250000) {
                player.giveExp(-250000);
                player.getInventory().addItem(EnchanterUtils.createBookGenerator(ChatColor.RED + "Extreme Book Generator",
                        "Generates a random Extreme Book"));
                player.sendMessage(ChatColor.GREEN + "You have purchased an Extreme Book!");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have enough XP!");
            }
        } else if (itemName.equals(ChatColor.WHITE + "" + ChatColor.BOLD + "Soulbound Scroll")) {
            if (playerXP >= 350000) {
                player.giveExp(-350000);
                player.getInventory().addItem(EnchanterUtils.createSoulboundScroll());
                player.sendMessage(ChatColor.GREEN + "You have purchased a Soulbound Scroll!");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have enough XP!");
            }
        }
    }

    private int getPlayerTotalExperience(Player player) {
        int level = player.getLevel();
        int totalExperience = (int)(player.getExp() * player.getExpToLevel());

        for (int i = 0; i < level; i++) {
            totalExperience += getExperienceAtLevel(i);
        }
        return totalExperience;
    }

    private int getExperienceAtLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        } else if (level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }
}
