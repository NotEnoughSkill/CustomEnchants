package me.notenoughskill.customenchants.listeners;

import me.notenoughskill.customenchants.utils.EnchanterUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BookGeneratorListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.BOOK) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        //if (meta != null || !meta.hasDisplayName()) {
        //    return;
        //}

        String displayName = meta.getDisplayName();
        if (displayName.equals(ChatColor.GREEN + "Basic Book Generator")) {
            event.getPlayer().getInventory().addItem(EnchanterUtils.generateBasicBook());
            event.getPlayer().sendMessage(ChatColor.GREEN + "You received a Basic Book!");
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        } else if (displayName.equals(ChatColor.BLUE + "Advanced Book Generator")) {
            event.getPlayer().getInventory().addItem(EnchanterUtils.generateAdvancedBook());
            event.getPlayer().sendMessage(ChatColor.BLUE + "You received an Advanced Book!");
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        } else if (displayName.equals(ChatColor.RED + "Extreme Book Generator")) {
            event.getPlayer().getInventory().addItem(EnchanterUtils.generateExtremeBook());
            event.getPlayer().sendMessage(ChatColor.RED + "You received an Extreme Book!");
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
    }
}
