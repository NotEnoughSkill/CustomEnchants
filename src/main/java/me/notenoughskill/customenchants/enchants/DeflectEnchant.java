package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DeflectEnchant implements Listener {
    private static final Random RANDOM = new Random();
    private static final long COOLDOWN_TIME = 10000;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player || !(event.getDamager() instanceof LivingEntity))) {
            return;
        }

        Player player = (Player) event.getEntity();
        LivingEntity attacker = (LivingEntity) event.getDamager();
        ItemStack helmet = player.getInventory().getHelmet();

        if (hasDeflectEnchantment(helmet)) {
            UUID playerUUID = player.getUniqueId();
            long currentTime = System.currentTimeMillis();
            if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < COOLDOWN_TIME) {
                return;
            }

            int level = getDeflectLevel(helmet);
            double chance = level * 0.005;

            if (Math.random() < chance) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if (isNegativeEffect(effect.getType())) {
                        attacker.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()));
                        player.removePotionEffect(effect.getType());
                        player.sendMessage(ChatColor.GREEN + "Deflected " + effect.getType().getName() + " to " + attacker.getName());
                        break;
                    }
                }
                cooldowns.put(playerUUID, currentTime);
            }
        }
    }

    private boolean hasDeflectEnchantment(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Deflect")) {
                return true;
            }
        }
        return false;
    }

    private int getDeflectLevel(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Deflect")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Deflect level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }

    private boolean isNegativeEffect(PotionEffectType type) {
        return type == PotionEffectType.BLINDNESS || type == PotionEffectType.CONFUSION ||
                type == PotionEffectType.HUNGER || type == PotionEffectType.WITHER ||
                type == PotionEffectType.POISON || type == PotionEffectType.SLOW ||
                type == PotionEffectType.SLOW_DIGGING || type == PotionEffectType.WEAKNESS;
    }
}
