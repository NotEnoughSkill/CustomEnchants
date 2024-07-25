package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ExtinguishEnchant implements Listener {
    private static final Random RANDOM = new Random();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        ItemStack boots = player.getInventory().getBoots();

        if (hasExtinguishEnchantment(boots)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE
            || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
            || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                if (boots != null && boots.getType() != Material.AIR) {
                    ItemMeta meta = boots.getItemMeta();
                    if (meta != null && meta.hasLore()) {
                        for (String line : meta.getLore()) {
                            if (ChatColor.stripColor(line).startsWith("Extinguish")) {
                                int level = getExtinguishLevel(boots);
                                if (level > 0) {
                                    double damageReduction = 0.2 * level;
                                    double finalDamage = event.getDamage() * (1 - damageReduction);
                                    event.setDamage(finalDamage);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean hasExtinguishEnchantment(ItemStack boots) {
        if (boots == null || !boots.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = boots.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Extinguish")) {
                return true;
            }
        }
        return false;
    }

    private int getExtinguishLevel(ItemStack boots) {
        if (boots == null || !boots.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = boots.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Extinguish")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Extinguish level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }
}
