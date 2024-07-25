package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InfernalEnchant implements Listener {

    private static final Random RANDOM = new Random();
    private static final long COOLDOWN_TIME = 60000;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack helmet = player.getInventory().getHelmet();

            if (hasInfernalEnchantment(helmet)) {
                UUID playerUUID = player.getUniqueId();
                long currentTime = System.currentTimeMillis();
                if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < COOLDOWN_TIME) {
                    return;
                }

                int level = getInfernalLevel(helmet);
                double chance = level * 0.05;

                if (Math.random() < chance) {
                    spawnBlazes(player, 4);
                    player.sendMessage(ChatColor.RED + "The Infernal enchantment has summoned blazes to aid you!");

                    cooldowns.put(playerUUID, currentTime);
                }
            }
        }
    }

    private boolean hasInfernalEnchantment(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Infernal")) {
                return true;
            }
        }
        return false;
    }

    private int getInfernalLevel(ItemStack helmet) {
        if (helmet == null || !helmet.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = helmet.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Infernal")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Infernal level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }

    private void spawnBlazes(Player player, int count) {
        for (int i = 0; i < count; i++) {
            Blaze blaze = (Blaze) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLAZE);
            blaze.setCustomName(ChatColor.RED + player.getName() + "'s Blaze");
            blaze.setCustomNameVisible(true);

            blaze.setTarget(null);

            for (Entity entity : blaze.getNearbyEntities(15, 15, 15)) {
                if (entity instanceof LivingEntity && !entity.equals(player)) {
                    blaze.setTarget((LivingEntity) entity);
                    break;
                }
            }
        }
    }
}
