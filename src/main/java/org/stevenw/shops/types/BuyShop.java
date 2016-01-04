package org.stevenw.shops.types;

import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stevenw.shops.economy.Economy;
import org.stevenw.shops.gui.BuyMenu;
import org.stevenw.shops.sShops;

import java.util.ArrayList;
import java.util.Set;

public class BuyShop extends Shop{
    public final static String INV_NAME = "Buy Shop: ";
    public BuyShop(sShops plugin, Economy economy, String name, Player player) {
        super(plugin, economy, "buy", name, player);
    }

    public void open() {
        BuyMenu menu =  new BuyMenu(INV_NAME + name, ItemMenu.Size.fit(getItems().size()), plugin, this);
        menu.open(player);
    }

    public boolean transact(ItemStack item) {
        if(canTransact(item.getData())) {
            Long price = getPrice(item.getData());
            if(economy.withdraw(player, price)) {
                player.sendMessage(plugin.getPrefix() + "Bought "  + item.getType().toString().toLowerCase().replace("_", " ") + " for $" + price);
                player.getInventory().addItem(item);
                return true;
            }
        }
        return false;
    }

    public static boolean exists(sShops plugin, String shopName) {
        if(plugin.getConfig().getConfigurationSection("shops.buy." + shopName) == null) {
            return false;
        } else {
            return true;
        }
    }
}