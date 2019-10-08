package org.stevenw.shops.types;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.stevenw.shops.economy.Economy;
import org.stevenw.shops.sShops;

import java.util.ArrayList;
import java.util.Set;

public class SellShop extends Shop {
    public final static String INV_NAME = "Sell Shop: ";

    public SellShop(sShops plugin, Economy economy, String name, Player player) {
        super(plugin, economy, "sell", name, player);
    }

    public boolean transact(ItemStack item) {
  //      plugin.getLogger().info("transact sell Item");
        if( canTransactSell(item) ) {
//            plugin.getLogger().info("canTransact Item");
            double money = getPriceSell(item) * item.getAmount();
            String message = "Sold " + item.getAmount() + " " + item.getType().toString().toLowerCase().replace("_", " ") + " for $" + money;
            if (getEconomy().deposit(getPlayer(), (long) money, message)) {
                getPlayer().sendMessage(getPlugin().getPrefix() + message);
                item.setType(Material.AIR);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public void open() {
        Inventory inventory = Bukkit.createInventory(this.getPlayer(), 27, INV_NAME + getName());
        this.getPlayer().openInventory(inventory);
    }

    public static boolean exists(sShops plugin, String shopName) {
        if(plugin.getConfig().getConfigurationSection("shops.sell." + shopName) == null) {
            return false;
        } else {
            return true;
        }
    }
}