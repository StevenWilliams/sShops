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
        return econ.withdrawPlayer(player, amount).transactionSuccess();
    }
}
