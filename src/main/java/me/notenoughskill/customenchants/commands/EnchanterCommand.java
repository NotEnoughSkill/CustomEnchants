package me.notenoughskill.customenchants.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EnchanterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            openEnchanterGUI(p);
            return true;
        }
        return false;
    }

    private void openEnchanterGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 18, ChatColor.DARK_PURPLE + "Enchanter");

        // ENCHANT BOOKS
        ItemStack basicBook = new ItemStack(Material.BOOK);
        ItemMeta basicMeta = basicBook.getItemMeta();
        basicMeta.setDisplayName(ChatColor.GREEN + "Basic Book");
        basicMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: 70k XP"));
        basicBook.setItemMeta(basicMeta);

        ItemStack advancedBook = new ItemStack(Material.BOOK);
        ItemMeta advancedMeta = advancedBook.getItemMeta();
        advancedMeta.setDisplayName(ChatColor.BLUE + "Advanced Book");
        advancedMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: 150k XP"));
        advancedBook.setItemMeta(advancedMeta);

        ItemStack extremeBook = new ItemStack(Material.BOOK);
        ItemMeta extremeMeta = extremeBook.getItemMeta();
        extremeMeta.setDisplayName(ChatColor.RED + "Extreme Book");
        extremeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Cost: 250k XP"));
        extremeBook.setItemMeta(extremeMeta);

        //WHITE SCROLL
        ItemStack soulboundScroll = new ItemStack(Material.MAP);
        ItemMeta soulboundMeta = soulboundScroll.getItemMeta();
        soulboundMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Soulbound Scroll");
        soulboundMeta.setLore(Arrays.asList(
                "",
                ChatColor.WHITE + "When applied to gear:",
                ChatColor.GRAY + "You keep the item on death but",
                ChatColor.GRAY + "lose the scroll applied.",
                "",
                ChatColor.GRAY + "Hint: Drag-and-drop in your inventory",
                ChatColor.GRAY + "onto the item you wish to apply it to.",
                "",
                ChatColor.GRAY + "" + ChatColor.BOLD + "Cost: 350k XP"
        ));
        soulboundScroll.setItemMeta(soulboundMeta);

        gui.setItem(2, basicBook);
        gui.setItem(4, advancedBook);
        gui.setItem(6, extremeBook);
        gui.setItem(12, soulboundScroll);

        player.openInventory(gui);
    }
}
