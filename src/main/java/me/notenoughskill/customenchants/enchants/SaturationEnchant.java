package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SaturationEnchant extends BukkitRunnable {
    private final JavaPlugin plugin;

    public SaturationEnchant(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack helmet = player.getInventory().getHelmet();

            if (hasSaturationEnchantment(helmet)) {
                int level = getSaturationLevel(helmet);
                double chance = level * 0.05;
                if (Math.random() < chance) {
                    player.setFoodLevel(Math.min(player.getFoodLevel() + 1, 20));
                }
            }
        }
    }

    private boolean hasSaturationEnchantment(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Saturation")) {
                return true;
            }
        }
        return false;
    }

    private int getSaturationLevel(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Saturation")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse saturation level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }
}
