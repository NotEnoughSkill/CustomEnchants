package me.notenoughskill.customenchants.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SoulBoundHandler {

    private static final NamespacedKey SOULBOUND_KEY = new NamespacedKey("customenchants", "soulbound");

    public static boolean applySoulbound(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        if (meta.getLore() != null && meta.getLore().contains(ChatColor.WHITE + "" + ChatColor.BOLD + "SOULBOUND")) {
            return false;
        }

        meta.getPersistentDataContainer().set(SOULBOUND_KEY, PersistentDataType.BYTE, (byte) 1);

        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "SOULBOUND");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return true;
    }

    public static boolean hasSoulbound(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(SOULBOUND_KEY, PersistentDataType.BYTE);
    }

    public static void removeSoulbound(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        List<String> lore = meta.getLore();
        if (lore != null) {
            lore.remove(ChatColor.WHITE + "" + ChatColor.BOLD + "SOULBOUND");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        meta.getPersistentDataContainer().remove(SOULBOUND_KEY);
        item.setItemMeta(meta);
    }
}
