package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MedicateEnchant implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack helmet = player.getInventory().getHelmet();

            if (hasMedicateEnchantment(helmet) && isEnvironmentalDamage(event.getCause())) {
                double originalDamage = event.getDamage();
                int medicateLevel = getMedicateLevel(helmet);
                double damageReduction = getDamageReduction(medicateLevel);
                double newDamage = originalDamage * (1 - damageReduction);

                event.setDamage(newDamage);

                Bukkit.getLogger().info("Original Damage: " + originalDamage + ", Reduced Damage: " + newDamage);
            }
        }
    }

    private boolean isEnvironmentalDamage(EntityDamageEvent.DamageCause cause) {
        return cause == EntityDamageEvent.DamageCause.LAVA || cause == EntityDamageEvent.DamageCause.DROWNING || cause == EntityDamageEvent.DamageCause.FALL;
    }



    private boolean hasMedicateEnchantment(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Medicate")) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCustomEnchantment(ItemMeta meta, String enchantmentName) {
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                if (line.startsWith(enchantmentName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getMedicateLevel(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Medicate")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Medicate level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }


    private double getDamageReduction(int level) {
        switch (level) {
            case 1:
                return 0.2; // 20%
            case 2:
                return 0.4; // 40%
            default:
                return 0.0;
        }
    }
}
