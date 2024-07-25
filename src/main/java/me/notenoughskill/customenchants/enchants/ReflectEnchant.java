package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class ReflectEnchant implements Listener {
    private static final Random RANDOM = new Random();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        if (!(damagedEntity instanceof Player)) {
            return;
        }

        Player player = (Player) damagedEntity;
        ItemStack chestplate = player.getInventory().getChestplate();

        if (hasReflectEnchantment(chestplate)) {
            int level = getReflectLevel(chestplate);
            double chance = 0.03 * level;
            double damage = event.getDamage();

            if (RANDOM.nextDouble() < chance) {
                if (event.getDamager() instanceof LivingEntity) {
                    LivingEntity attacker = (LivingEntity) event.getDamager();
                    double reflectDamage = damage * (0.08 * level);

                    attacker.damage(reflectDamage);

                    player.sendMessage(ChatColor.GREEN + "The Reflect enchantment caused " + reflectDamage + " damage to your attacker!");
                }
            }
        }
    }

    private boolean hasReflectEnchantment(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Reflect")) {
                return true;
            }
        }
        return false;
    }

    private int getReflectLevel(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Reflect")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Reflect level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }
}
