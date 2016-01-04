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
        if(canTransact(item.getData())) {
            Long money = getPrice(item.getData()) * item.getAmount();
            if (economy.deposit(player, money)) {
                player.sendMessage(plugin.getPrefix() + "Sold " + item.getAmount() + " " + item.getType().toString().toLowerCase().replace("_", " ") + " for $" + money);
                item.setType(Material.AIR);
                return true;
            }
        }
        return false;
    }


    public void open() {
        Inventory inventory = Bukkit.createInventory(this.player, 27, INV_NAME + name);
        this.player.openInventory(inventory);
    }

    public static boolean exists(sShops plugin, String shopName) {
        if(plugin.getConfig().getConfigurationSection("shops.sell." + shopName) == null) {
            return false;
        } else {
            return true;
        }
    }
}