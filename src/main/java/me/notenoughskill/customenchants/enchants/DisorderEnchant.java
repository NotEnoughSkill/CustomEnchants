package me.notenoughskill.customenchants.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class DisorderEnchant implements Listener {
    private static final Random RANDOM = new Random();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        if (!(damagedEntity instanceof Player)) {
            return;
        }

        Player player = (Player) damagedEntity;
        ItemStack chestplate = player.getInventory().getChestplate();

        if (hasDisorderEnchantment(chestplate)) {
            int level = getDisorderLevel(chestplate);
                double chance = 0.003 * level;

                if (RANDOM.nextDouble() < chance) {
                    if (event.getDamager() instanceof Player) {
                        Player attacker = (Player) event.getDamager();
                        scrambleInventory(attacker);

                        player.sendMessage(ChatColor.GREEN + "The Disorder enchantment scrambled your attacker's inventory!");
                    }
                }
            }
        }

    private boolean hasDisorderEnchantment(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Disorder")) {
                return true;
            }
        }
        return false;
    }

    private int getDisorderLevel(ItemStack chestplate) {
        if (chestplate == null || !chestplate.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }
        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.contains("Disorder")) {
                String[] parts = line.split("Lvl ");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].replace(")", ""));
                    } catch (NumberFormatException e) {
                        Bukkit.getLogger().severe("Failed to parse Disorder level: " + parts[1]);
                    }
                }
            }
        }
        return 0;
    }

    private void scrambleInventory(Player attacker) {
        if (attacker == null || attacker.getInventory() == null) {
            return;
        }
        ItemStack[] contents = attacker.getInventory().getContents();
        ItemStack[] armorContents = attacker.getInventory().getArmorContents();

        List<ItemStack> mainInventoryItems = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                mainInventoryItems.add(item);
            }
        }

        Collections.shuffle(mainInventoryItems);

        ItemStack[] newContents = new ItemStack[contents.length];

        int itemIndex = 0;
        for (int i = 0; i < 36; i++) {
            if (itemIndex < mainInventoryItems.size()) {
                newContents[i] = mainInventoryItems.get(itemIndex++);
            } else {
                newContents[i] = null;
            }
        }

        System.arraycopy(armorContents, 0, newContents, 36, armorContents.length);

        attacker.getInventory().setContents(newContents);
    }
}
