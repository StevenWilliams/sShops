package org.stevenw.shops.economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.stevenw.shops.sShops;

public class VaultEconomy implements Economy {
    private net.milkbowl.vault.economy.Economy econ;
    public VaultEconomy(sShops plugin) {
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        econ = rsp.getProvider();
    }
    @Override
    public boolean deposit(Player player, long amount) {
        return econ.depositPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean withdraw(Player player, long amount) {
        sShops.plugin.getLogger().info("withdraw amount " + amount);
        return econ.withdrawPlayer(player.getName(), amount).transactionSuccess();
    }

    @Override
    public boolean has(Player player, long amount) {
        return econ.has(player, amount);
    }

    @Override
    public boolean deposit(Player player, long amount, String message) {
        return deposit(player, amount);
    }

    @Override
    public boolean withdraw(Player player, long amount, String message) {
        return withdraw(player, amount);
    }

    @Override
    public boolean withdraw(Player player, String currency, long amount, String message) {
        return false;
    }

    @Override
    public boolean withdraw(Player player, String currency, long amount) {
        return false;
    }

    @Override
    public boolean has(Player player, String currency, long amount) {
        return false;
    }


}
