package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class ReplicateEnchant implements Listener {
    private static final Random RANDOM = new Random();

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack chestplate = player.getInventory().getChestplate();

        if (hasReplicateEnchantment(chestplate)) {
            int level = getReplicateLevel(chestplate);
            double chance = 0.01 * level;

            if (RANDOM.nextDouble() < chance) {
                event.setCancelled(true);
                ItemStack consumedItem = event.getItem();

                player.getInventory().addItem(consumedItem);
                player.sendMessage(ChatColor.GREEN + "Your Replicate enchantment prevented the consumption!");
            }
        }
    }

    private boolean hasReplicateEnchantment(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Replicate")) {
                return true;
            }
        }
        return false;
    }

    private int getReplicateLevel(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Replicate")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Replicate level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }
}
