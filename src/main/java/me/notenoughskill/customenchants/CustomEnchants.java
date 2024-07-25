package me.notenoughskill.customenchants;

import me.notenoughskill.customenchants.commands.EnchanterCommand;
import me.notenoughskill.customenchants.enchants.*;
import me.notenoughskill.customenchants.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEnchants extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("enchanter").setExecutor(new EnchanterCommand());
        getServer().getPluginManager().registerEvents(new EnchanterListener(), this);
        getServer().getPluginManager().registerEvents(new BookGeneratorListener(), this);
        getServer().getPluginManager().registerEvents(new BookApplicationListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new MedicateEnchant(), this);
        getServer().getPluginManager().registerEvents(new InfernalEnchant(), this);
        getServer().getPluginManager().registerEvents(new EnrageEnchant(), this);
        getServer().getPluginManager().registerEvents(new DeflectEnchant(), this);
        getServer().getPluginManager().registerEvents(new ValiantEnchant(), this);
        getServer().getPluginManager().registerEvents(new ReplicateEnchant(), this);
        getServer().getPluginManager().registerEvents(new ReflectEnchant(), this);
        getServer().getPluginManager().registerEvents(new DisorderEnchant(), this);
        getServer().getPluginManager().registerEvents(new ExtinguishEnchant(), this);

        new SaturationEnchant(this).runTaskTimer(this, 0L, 400L);
    }

    @Override
    public void onDisable() {
    }
}
