package org.stevenw.shops;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.stevenw.shops.economy.Economy;
import org.stevenw.shops.economy.VaultEconomy;
import org.stevenw.shops.listeners.ShopCreateListener;
import org.stevenw.shops.listeners.ShopUseListener;

public class sShops extends JavaPlugin {
    private Economy economy;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new ShopCreateListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ShopUseListener(this), this);
        this.economy = new VaultEconomy(this);
    }
    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix"));
    }
    public Economy getEconomy() {
        return this.economy;
    }
}
