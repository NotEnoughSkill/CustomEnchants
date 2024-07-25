package me.notenoughskill.customenchants.listeners;

import me.notenoughskill.customenchants.CustomEnchants; // Import your main plugin class
import me.notenoughskill.customenchants.utils.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerEquipListener implements Listener {

    private final CustomEnchants plugin;
    private final Map<Player, Boolean> playerHasRadiateEffect = new HashMap<>();
    private static final int CHECK_INTERVAL_TICKS = 20 * 1;

    public PlayerEquipListener(CustomEnchants plugin) {
        this.plugin = plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateNightVisionForPlayer(player);
                }
            }
        }.runTaskTimer(plugin, 0L, CHECK_INTERVAL_TICKS);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == 5 || event.getSlot() == 6 || event.getSlot() == 7 || event.getSlot() == 8) {
            handleHelmetChange(player, event.getCurrentItem(), event.getCursor());
        }
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newHelmet = player.getInventory().getHelmet();
        handleHelmetChange(player, newHelmet, null);
    }

    private void handleHelmetChange(Player player, ItemStack newItem, ItemStack oldItem) {
        boolean hadRadiate = hasRadiateEnchantment(player.getInventory().getHelmet());

        if (oldItem != null && oldItem.getType() == Material.DIAMOND_HELMET) {
            ItemMeta oldMeta = oldItem.getItemMeta();
            if (oldMeta != null && oldMeta.hasLore() && hasCustomEnchantment(oldMeta, "Radiate")) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                playerHasRadiateEffect.put(player, false);
            }
        }

        if (newItem != null && newItem.getType() == Material.DIAMOND_HELMET) {
            ItemMeta newMeta = newItem.getItemMeta();
            if (newMeta != null && newMeta.hasLore() && hasCustomEnchantment(newMeta, "Radiate")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
                playerHasRadiateEffect.put(player, true);
            } else {
                if (playerHasRadiateEffect.getOrDefault(player, false)) {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    playerHasRadiateEffect.put(player, false);
                }
            }
            if (playerHasRadiateEffect.getOrDefault(player, false)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                playerHasRadiateEffect.put(player, false);
            }
        }
    }

    private void updateNightVisionForPlayer(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        boolean hasRadiate = hasRadiateEnchantment(helmet);

        if (hasRadiate) {
            if (!playerHasRadiateEffect.getOrDefault(player, false)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
                playerHasRadiateEffect.put(player, true);
            }
        } else {
            if (playerHasRadiateEffect.getOrDefault(player, false)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                playerHasRadiateEffect.put(player, false);
            }
        }
    }

    private boolean hasCustomEnchantment(ItemMeta meta, String enchantmentName) {
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                if (ChatColor.stripColor(line).startsWith(enchantmentName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasRadiateEnchantment(ItemStack item) {
        if (item != null && item.getType() == Material.DIAMOND_HELMET) {
            ItemMeta meta = item.getItemMeta();
            return meta != null && meta.hasLore() && hasCustomEnchantment(meta, "Radiate");
        }
        return false;
    }
}
