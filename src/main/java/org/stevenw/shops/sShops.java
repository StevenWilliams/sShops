package org.stevenw.shops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.stevenw.shops.citizens.ShopTrait;
import org.stevenw.shops.commands.BuyCommand;
import org.stevenw.shops.commands.SellCommand;
import org.stevenw.shops.economy.AsyncEconomy;
import org.stevenw.shops.economy.Economy;
import org.stevenw.shops.economy.VaultEconomy;
import org.stevenw.shops.economy.VulcanEconomyConnector;
import org.stevenw.shops.listeners.ShopCreateListener;
import org.stevenw.shops.listeners.ShopUseListener;

import java.util.logging.Level;

public class sShops extends JavaPlugin {
    private Economy economy;
    private AsyncEconomy asyncEconomy;
    public static boolean useNewMaterials = true;
    public static sShops plugin;
    private String customPrefix;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new ShopCreateListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ShopUseListener(this), this);
        this.getCommand("shopsreload").setExecutor(new ShopReload(this));
        this.getCommand("shopsbuy").setExecutor(new BuyCommand(this));
        this.getCommand("shopssell").setExecutor(new SellCommand(this));

        this.economy = new VulcanEconomyConnector(this); //new VaultEconomy(this);
        this.asyncEconomy = new AsyncEconomy(this);
        useNewMaterials = this.getConfig().getBoolean("use-new-api", true);
        plugin = this;
        customPrefix = this.getConfig().getString("custom-prefix", "VCUSTOM");



        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            //Register your trait with Citizens.
            net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(ShopTrait.class).withName("sShopsTrait"));
        }


    }
    @Override
    public void onDisable() {
        plugin=null;
    }
    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix"));
    }
    public Economy getEconomy() {
        return this.economy;
    }

    public String getCustomPrefix() {
        return customPrefix;
    }

    public AsyncEconomy getAsyncEconomy() {
        return asyncEconomy;
    }
}
