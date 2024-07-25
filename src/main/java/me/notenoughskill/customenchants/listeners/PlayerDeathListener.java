package me.notenoughskill.customenchants.listeners;

import me.notenoughskill.customenchants.items.SoulBoundHandler;
import me.notenoughskill.customenchants.utils.SoulboundStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Player player = (Player) entity;
        List<ItemStack> drops = new ArrayList<>(event.getDrops());

        if (entity instanceof Player) {
            ItemStack helmet = player.getInventory().getHelmet();

            if (hasFinalWishEnchantment(helmet)) {
                LivingEntity killer = (LivingEntity) player.getKiller();
                if (killer != null) {
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1));
                }
            }
        }

        List<ItemStack> soulboundItems = new ArrayList<>();
        List<ItemStack> nonSoulboundItems = new ArrayList<>();

        for (ItemStack item : drops) {
            if (SoulBoundHandler.hasSoulbound(item)) {
                SoulBoundHandler.removeSoulbound(item);
                soulboundItems.add(item);
            } else {
                nonSoulboundItems.add(item);
            }
        }

        SoulboundStorage.saveSoulboundItems(player, soulboundItems);
        event.getDrops().clear();
        event.getDrops().addAll(nonSoulboundItems);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> soulboundItems = SoulboundStorage.getSoulboundItems(player);
        SoulboundStorage.clearSoulboundItems(player);

        for (ItemStack item : soulboundItems) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
    }

    private boolean hasFinalWishEnchantment(ItemStack item) {
        if (item != null && item.getType() == Material.DIAMOND_HELMET) {
            ItemMeta meta = item.getItemMeta();
            return meta != null && meta.hasLore() && hasCustomEnchantment(meta, "Final Wish");
        }
        return false;
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
}