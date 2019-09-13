package org.stevenw.shops.economy;

import net.vulcanmc.vulcaneconomy.API;
import net.vulcanmc.vulcaneconomy.VulcanEconomy;
import net.vulcanmc.vulcaneconomy.rest.Account;
import net.vulcanmc.vulcaneconomy.rest.Currency;
import org.bukkit.entity.Player;
import org.stevenw.shops.sShops;

public class VulcanEconomyConnector implements Economy {
    private sShops plugin;
    private VulcanEconomy vulcanEconomy;
    private API vulcanAPI;

    public VulcanEconomyConnector(sShops plugin) {
        this.plugin = plugin;
         vulcanEconomy = (VulcanEconomy) plugin.getServer().getPluginManager().getPlugin("VulcanEconomy");
         vulcanAPI = new API(plugin);
    }

    @Override
    public boolean deposit(Player player, long amount) {
        return deposit(player, amount, "sShops deposit");
    }

    @Override
    public boolean withdraw(Player player, long amount) {
        return withdraw(player, amount, "sShops withdraw");
    }


    @Override
    public boolean deposit(Player player, long amount, String message) {
        return vulcanAPI.deposit(player, amount, message);
    }

    @Override
    public boolean withdraw(Player player, long amount, String message) {
        return vulcanAPI.withdraw(player, amount, message);
    }

    @Override
    public boolean withdraw(Player player, String currencyKey, long amount, String message) {
        if(currencyKey == null) {
            currencyKey = getDefaultCurrencyKey();
        }
        Currency currency = vulcanAPI.getCurrency(currencyKey);
        if( currency == null) {
            plugin.getLogger().info("Currency " + currencyKey + " does not exist!");
            return false;
        }
        return vulcanAPI.withdraw(player, currency, amount, message);
    }

    @Override
    public boolean withdraw(Player player, String currency, long amount) {
        return withdraw(player, currency, amount, "");
    }

    @Override
    public boolean has(Player player, String currencyKey, long amount) {
        if(currencyKey == null) {
            currencyKey = getDefaultCurrencyKey();
        }
        Currency currency = vulcanAPI.getCurrency(currencyKey);
        if( currency == null) {
            plugin.getLogger().info("Currency " + currencyKey + " does not exist!");
            return false;
        }
        return vulcanAPI.has(player, currency, amount);
    }

    @Override
    public boolean has(Player player, long amount) {
        return vulcanAPI.has(player, amount);
    }
    public String getCurrencyName(String key) {
        Currency currency = vulcanAPI.getCurrency(key);
        if(currency == null) {
            return vulcanAPI.getCurrency(getDefaultCurrencyKey()).getName();
        } else {
            return currency.getName();
        }
    }


    public String getDefaultCurrencyName() {
        return vulcanAPI.getCurrency(getDefaultCurrencyKey()).getName();
    }
    public String getDefaultCurrencyKey() {
        return plugin.getConfig().getString("default-currency");
    }
}
