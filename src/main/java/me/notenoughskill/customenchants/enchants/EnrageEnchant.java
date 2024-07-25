package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class EnrageEnchant implements Listener {
    private static final Random RANDOM = new Random();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack helmet = player.getInventory().getHelmet();

            if (hasEnrageEnchantment(helmet)) {
                int level = getEnrageLevel(helmet);
                double chance = level * 0.02;

                if (Math.random() < chance) {
                    applyEnrageEffects(player);
                    player.sendMessage(ChatColor.RED + "You are enraged!");
                }
            }
        }
    }

    private boolean hasEnrageEnchantment(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Enrage")) {
                return true;
            }
        }
        return false;
    }

    private int getEnrageLevel(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Enrage")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Enrage level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }

    private void applyEnrageEffects(Player player) {
        int duration = 3 * 20;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 2));
    }
}
