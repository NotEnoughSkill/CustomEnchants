package me.notenoughskill.customenchants.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchanterUtils {
    private static final Random RANDOM = new Random();

    public static ItemStack createBookGenerator(String name, String description) {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(ChatColor.GRAY + description, ChatColor.YELLOW + "Right-click to use"));
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack generateBasicBook() {
        CustomEnchantment[] enchants = {CustomEnchantment.RADIATE, CustomEnchantment.FINAL_WISH, CustomEnchantment.MEDICATE,
        CustomEnchantment.EXTINGUISH};
        CustomEnchantment enchantment = enchants[RANDOM.nextInt(enchants.length)];
        int level;
        switch (enchantment) {
            case MEDICATE:
                level = RANDOM.nextInt(2) + 1;
                break;
            case EXTINGUISH:
                level = RANDOM.nextInt(3) + 1;
                break;
            default:
                level = 1;
        }
        return createEnchantedBook(enchantment, level, ChatColor.GREEN);
    }

    public static ItemStack generateAdvancedBook() {
        CustomEnchantment[] enchants = {CustomEnchantment.SATURATION, CustomEnchantment.INFERNAL, CustomEnchantment.ENRAGE,
        CustomEnchantment.DEFLECT};
        CustomEnchantment enchantment = enchants[RANDOM.nextInt(enchants.length)];
        int level;
        switch (enchantment) {
            case SATURATION:
                level = RANDOM.nextInt(6) + 1;
                break;
            case INFERNAL:
                level = RANDOM.nextInt(8) + 1;
                break;
            case ENRAGE:
                level = RANDOM.nextInt(3) + 1;
                break;
            case DEFLECT:
                level = RANDOM.nextInt(4) + 1;
                break;
            default:
                level = 1;
        }
        return createEnchantedBook(enchantment, level, ChatColor.YELLOW);
    }

    public static ItemStack generateExtremeBook() {
        CustomEnchantment[] enchants = {CustomEnchantment.VALIANT, CustomEnchantment.REPLICATE, CustomEnchantment.REFLECT,
                CustomEnchantment.DISORDER};
        CustomEnchantment enchantment = enchants[RANDOM.nextInt(enchants.length)];
        int level = RANDOM.nextInt(5) + 1;
        return createEnchantedBook(enchantment, level, ChatColor.RED);
    }

    public static ItemStack createEnchantedBook(CustomEnchantment enchantment, int level, ChatColor color) {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        String displayName = enchantment.getDisplayName();
        if (meta == null) {
            return book;
        }

        meta.setDisplayName(color + "" + ChatColor.BOLD + displayName + " (Lvl " + level + ")");

        int successChance = RANDOM.nextInt(101);
        int destroyChance = RANDOM.nextInt(101);

        meta.setLore(Arrays.asList(
                ChatColor.GRAY + enchantment.getDescription(),
                "",
                ChatColor.LIGHT_PURPLE + "* " + ChatColor.GRAY + "Success Chance: " + ChatColor.GREEN + successChance + "%",
                ChatColor.LIGHT_PURPLE + "* " + ChatColor.GRAY + "Destroy Chance: " + ChatColor.DARK_RED + destroyChance + "%",
                "",
                ChatColor.LIGHT_PURPLE + "* " + ChatColor.GRAY + "Max Enchant Level: " + ChatColor.AQUA + enchantment.getMaxLevel(),
                "",
                ChatColor.LIGHT_PURPLE + "(!) " + ChatColor.GRAY + enchantment.getApplicableGear() + " Enchant"
        ));

        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack createSoulboundScroll() {
        ItemStack map = new ItemStack(Material.MAP);
        ItemMeta meta = map.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Soulbound Scroll");
        meta.setLore(Arrays.asList(
                "",
                ChatColor.WHITE + "When applied to gear:",
                ChatColor.GRAY + "You keep the item on death but",
                ChatColor.GRAY + "lose the scroll applied.",
                "",
                ChatColor.GRAY + "Hint: Drag-and-drop in your inventory",
                ChatColor.GRAY + "onto the item you wish to apply it to."
        ));
        map.setItemMeta(meta);
        return map;
    }

}
