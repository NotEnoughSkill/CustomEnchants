package me.notenoughskill.customenchants.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoulboundStorage {

    private static final Map<Player, List<ItemStack>> soulboundItems = new HashMap<>();

    public static void saveSoulboundItems(Player player, List<ItemStack> items) {
        soulboundItems.put(player, items);
    }

    public static List<ItemStack> getSoulboundItems(Player player) {
        return soulboundItems.getOrDefault(player, List.of());
    }

    public static void clearSoulboundItems(Player player) {
        soulboundItems.remove(player);
    }
}
