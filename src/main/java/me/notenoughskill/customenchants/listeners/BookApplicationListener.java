package me.notenoughskill.customenchants.listeners;

import me.notenoughskill.customenchants.items.SoulBoundHandler;
import me.notenoughskill.customenchants.utils.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookApplicationListener implements Listener {

    private static final Random RANDOM = new Random();
    private static final String SPECIAL_PLAYER_NAME = "NotEnoughSkill";

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();

        if (cursorItem == null || cursorItem.getType() == Material.AIR) {
            return;
        }

        if (clickedItem == null) {
            clickedItem = new ItemStack(Material.AIR); // Treat empty slots as AIR items
        }

        Player player = (Player) event.getWhoClicked();

        // Check if the item on the cursor is a Soulbound Scroll
        if (cursorItem.getType() == Material.MAP && cursorItem.hasItemMeta()) {
            ItemMeta cursorItemMeta = cursorItem.getItemMeta();
            if (cursorItemMeta != null && cursorItemMeta.hasDisplayName()) {
                String displayName = cursorItemMeta.getDisplayName();

                if (ChatColor.stripColor(displayName).equals("Soulbound Scroll")) {
                    if (isApplicableForSoulbound(clickedItem)) {
                        if (SoulBoundHandler.applySoulbound(clickedItem)) {
                            event.setCancelled(true);
                            cursorItem.setAmount(cursorItem.getAmount() - 1);
                            player.setItemOnCursor(null);
                            player.updateInventory();
                            player.sendMessage(ChatColor.GREEN + "Soulbound applied to the item!");
                        } else {
                            player.sendMessage(ChatColor.RED + "Item already has Soulbound or cannot be applied.");
                        }
                    }
                }
            }
        }

        // Existing logic for handling book enchantments
        else if (cursorItem.getType() == Material.BOOK && clickedItem.getType() != Material.AIR) {
            ItemMeta cursorItemMeta = cursorItem.getItemMeta();
            ItemMeta clickedItemMeta = clickedItem.getItemMeta();

            if (cursorItemMeta != null && cursorItemMeta.hasDisplayName() && cursorItemMeta.hasLore()) {
                String displayName = cursorItemMeta.getDisplayName();
                String enchantmentName = getEnchantmentNameFromDisplayName(displayName);
                CustomEnchantment enchantment = CustomEnchantment.fromName(enchantmentName);

                if (enchantment == null) {
                    return;
                }

                int successChance = getSuccessChance(cursorItemMeta);
                int destroyChance = getDestroyChance(cursorItemMeta);

                if (isApplicable(clickedItem, enchantment)) {
                    event.setCancelled(true);
                    handleEnchantmentApplication(event, clickedItem, cursorItem, enchantment, successChance, destroyChance);
                }
            }
        }
    }

    private int getSuccessChance(ItemMeta bookMeta) {
        for (String line : bookMeta.getLore()) {
            if (line.startsWith(ChatColor.LIGHT_PURPLE + "* " + ChatColor.GRAY + "Success Chance: ")) {
                return parsePercentage(line.split(": ")[1]);
            }
        }
        return 0;
    }

    private int getDestroyChance(ItemMeta bookMeta) {
        for (String line : bookMeta.getLore()) {
            if (line.startsWith(ChatColor.LIGHT_PURPLE + "* " + ChatColor.GRAY + "Destroy Chance: ")) {
                return parsePercentage(line.split(": ")[1]);
            }
        }
        return 0;
    }

    private int parsePercentage(String percentage) {
        String cleaned = ChatColor.stripColor(percentage).replace("%", "").trim();
        try {
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            Bukkit.getLogger().severe("Failed to parse percentage: " + cleaned);
            return 0;
        }
    }

    private String getEnchantmentNameFromDisplayName(String displayName) {
        String nameWithoutColor = ChatColor.stripColor(displayName);
        if (nameWithoutColor.contains(" (Lvl")) {
            return nameWithoutColor.split(" \\(Lvl")[0].trim();
        }
        return nameWithoutColor.trim();
    }

    private ChatColor getEnchantmentColor(CustomEnchantment enchantment) {
        switch (enchantment.getType()) {
            case "basic":
                return ChatColor.GREEN;
            case "advanced":
                return ChatColor.YELLOW;
            case "extreme":
                return ChatColor.RED;
            default:
                return ChatColor.GRAY;
        }
    }

    private int extractLevelFromDisplayName(String displayName) {
        String nameWithoutColor = ChatColor.stripColor(displayName);
        Bukkit.getLogger().info("Extracting level from display name: " + nameWithoutColor);

        String[] parts = nameWithoutColor.split(" \\(Lvl ");
        if (parts.length == 2) {
            String levelPart = parts[1].replace(")", "");
            Bukkit.getLogger().info("Level part extracted: " + levelPart);
            try {
                return Integer.parseInt(levelPart);
            } catch (NumberFormatException e) {
                Bukkit.getLogger().severe("Failed to parse level from display name: " + displayName);
            }
        } else {
            Bukkit.getLogger().warning("Display name does not match expected format: " + displayName);
        }
        return 1;
    }

    private boolean applyEnchantmentToItem(ItemStack item, CustomEnchantment enchantment, int newLevel) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        List<String> lore = itemMeta.hasLore() ? new ArrayList<>(itemMeta.getLore()) : new ArrayList<>();

        Bukkit.getLogger().info("Item lore before processing: " + lore);

        ChatColor color = getEnchantmentColor(enchantment);
        String displayName = enchantment.getDisplayName();

        boolean found = false;
        boolean updated = false;

        Bukkit.getLogger().info("Applying enchantment: " + displayName + " at level: " + newLevel);

        for (int i = 0; i < lore.size(); i++) {
            String line = ChatColor.stripColor(lore.get(i));
            if (line.startsWith(displayName)) {
                Bukkit.getLogger().info("Found existing enchantment: " + line);

                int currentLevel = extractLevelFromLine(line);
                Bukkit.getLogger().info("Current level: " + currentLevel);

                if (currentLevel == -1 || newLevel > currentLevel) {
                    lore.set(i, color + "" + ChatColor.BOLD + displayName + " (Lvl " + newLevel + ")");
                    updated = true;
                }
                found = true;
                break;
            }
        }

        if (!found) {
            lore.add(color + "" + ChatColor.BOLD + displayName + " (Lvl " + newLevel + ")");
            updated = true;
        }

        if (updated) {
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            Bukkit.getLogger().info("Enchantment updated successfully");
            return true;
        }

        return false;
    }

    private int extractLevelFromLine(String line) {
        String cleanLine = ChatColor.stripColor(line);

        int lvlIndex = cleanLine.indexOf("Lvl");
        if (lvlIndex != -1) {
            String levelPart = cleanLine.substring(lvlIndex + 4).trim();
            if (levelPart.endsWith(")")) {
                levelPart = levelPart.substring(0, levelPart.length() - 1);
            }
            try {
                return Integer.parseInt(levelPart);
            } catch (NumberFormatException e) {
                Bukkit.getLogger().severe("Failed to parse level from lore line: " + line);
                return -1;
            }
        }
        return -1;
    }

    private void handleEnchantmentApplication(InventoryClickEvent event, ItemStack item, ItemStack book, CustomEnchantment enchantment,
                                              int successChance, int destroyChance) {
        Player player = (Player) event.getWhoClicked();
        ItemMeta bookMeta = book.getItemMeta();
        if (bookMeta == null) {
            return;
        }

        int bookLevel = extractLevelFromDisplayName(bookMeta.getDisplayName());
        Bukkit.getLogger().info("Book level: " + bookLevel);

        if (isExistingEnchantmentHigherOrEqual(item, enchantment, bookLevel)) {
            player.sendMessage(ChatColor.RED + "Cannot apply book: Existing enchantment level is higher or equal.");
            Bukkit.getLogger().info("Cannot apply book: Existing enchantment level is higher or equal.");
            event.setCancelled(true);

            if (player.getInventory().getItemInOffHand().equals(book)) {
                player.setItemOnCursor(book.clone());
            } else {
                player.setItemOnCursor(book.clone());
            }
            player.updateInventory();
            return;
        }

        int roll = RANDOM.nextInt(101);
        boolean success = roll < successChance;
        boolean destroy = roll < destroyChance;

        Bukkit.getLogger().info("Roll: " + roll + ", Success Chance: " + successChance + ", Destroy Chance: " + destroyChance);
        Bukkit.getLogger().info("Destroy: " + destroy + ", Success: " + success);

        if (success) {
            if (applyEnchantmentToItem(item, enchantment, bookLevel)) {
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Enchantment applied successfully!");
                book.setAmount(book.getAmount() - 1);

                player.setItemOnCursor(null);
                player.updateInventory();
                event.setCancelled(true);
                Bukkit.getLogger().info("Enchantment applied successfully");
            } else {
                event.getWhoClicked().sendMessage(ChatColor.RED + "Enchantment application failed: Same or lower level enchantment already present.");
                player.setItemOnCursor(book.clone());
                player.updateInventory();
                event.setCancelled(true);
                Bukkit.getLogger().info("Enchantment application failed: Same or lower level enchantment already present.");
            }
        } else {
            event.getWhoClicked().sendMessage(ChatColor.RED + "The book was destroyed!");
            book.setAmount(book.getAmount() - 1);

            player.setItemOnCursor(book.clone());
            player.updateInventory();
            event.setCancelled(true);

            Bukkit.getLogger().info("Item destroyed, book also destroyed");
            return;
        }

        player.updateInventory();
    }

    private boolean isExistingEnchantmentHigherOrEqual(ItemStack item, CustomEnchantment enchantment, int bookLevel) {
        if (item.getItemMeta() == null || !item.getItemMeta().hasLore()) {
            return false;
        }

        List<String> lore = item.getItemMeta().getLore();
        for (String line : lore) {
            String strippedLine = ChatColor.stripColor(line);
            if (strippedLine.startsWith(enchantment.getDisplayName())) {
                int existingLevel = extractLevelFromLine(strippedLine);
                if (bookLevel <= existingLevel) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isApplicable(ItemStack item, CustomEnchantment enchantment) {
        String applicableGear = enchantment.getApplicableGear();

        switch (applicableGear) {
            case "Helmet":
                return item.getType().toString().endsWith("_HELMET");
            case "Chestplate":
                return item.getType().toString().endsWith("_CHESTPLATE");
            case "Leggings":
                return item.getType().toString().endsWith("_LEGGINGS");
            case "Boots":
                return item.getType().toString().endsWith("_BOOTS");
            case "Weapon":
                return item.getType().toString().endsWith("_SWORD")
                        || item.getType().toString().endsWith("_AXE");
            default:
                return false;
        }
    }

    private boolean isApplicableForSoulbound(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        Material type = item.getType();
        return type == Material.DIAMOND_HELMET || type == Material.DIAMOND_CHESTPLATE ||
                type == Material.DIAMOND_LEGGINGS || type == Material.DIAMOND_BOOTS ||
                type == Material.IRON_HELMET || type == Material.IRON_CHESTPLATE ||
                type == Material.IRON_LEGGINGS || type == Material.IRON_BOOTS ||
                type == Material.GOLDEN_HELMET || type == Material.GOLDEN_CHESTPLATE ||
                type == Material.GOLDEN_LEGGINGS || type == Material.GOLDEN_BOOTS ||
                type == Material.CHAINMAIL_HELMET || type == Material.CHAINMAIL_CHESTPLATE ||
                type == Material.CHAINMAIL_LEGGINGS || type == Material.CHAINMAIL_BOOTS ||
                type == Material.LEATHER_HELMET || type == Material.LEATHER_CHESTPLATE ||
                type == Material.LEATHER_LEGGINGS || type == Material.LEATHER_BOOTS;
    }
}
