package org.stevenw.shops.economy;

import org.bukkit.entity.Player;

public interface Economy {
    public boolean deposit(Player player, long amount);
    public boolean withdraw(Player player, long amount);
}
