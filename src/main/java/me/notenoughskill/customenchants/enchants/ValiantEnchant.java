package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ValiantEnchant implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack chestplate = player.getInventory().getChestplate();

            if (hasValiantEnchantment(chestplate)) {
                int level = getValiantLevel(chestplate);
                double damageReduction = 0.05 * level;

                double reducedDamage = event.getDamage() * (1 - damageReduction);

                event.setDamage(reducedDamage);
            }
        }
    }

    private boolean hasValiantEnchantment(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Valiant")) {
                return true;
            }
        }
        return false;
    }

    private int getValiantLevel(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Valiant")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Valiant level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }
}
