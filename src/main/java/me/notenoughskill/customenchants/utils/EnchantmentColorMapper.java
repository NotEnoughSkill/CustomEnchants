package me.notenoughskill.customenchants.utils;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentColorMapper {
    private static final Map<CustomEnchantment, ChatColor> enchantmentColors = new HashMap<>();

    static {
        enchantmentColors.put(CustomEnchantment.FINAL_WISH, ChatColor.GREEN);
        enchantmentColors.put(CustomEnchantment.MEDICATE, ChatColor.GREEN);
        enchantmentColors.put(CustomEnchantment.RADIATE, ChatColor.GREEN);
        enchantmentColors.put(CustomEnchantment.EXTINGUISH, ChatColor.GREEN);
        enchantmentColors.put(CustomEnchantment.INFERNAL, ChatColor.YELLOW);
        enchantmentColors.put(CustomEnchantment.SATURATION, ChatColor.YELLOW);
        enchantmentColors.put(CustomEnchantment.DEFLECT, ChatColor.YELLOW);
        enchantmentColors.put(CustomEnchantment.ENRAGE, ChatColor.YELLOW);
        enchantmentColors.put(CustomEnchantment.REFLECT, ChatColor.RED);
        enchantmentColors.put(CustomEnchantment.VALIANT, ChatColor.RED);
        enchantmentColors.put(CustomEnchantment.DISORDER, ChatColor.RED);
        enchantmentColors.put(CustomEnchantment.REPLICATE, ChatColor.RED);
    }

    public static ChatColor getColor(CustomEnchantment enchantment) {
        return enchantmentColors.getOrDefault(enchantment, ChatColor.GRAY);
    }
}
