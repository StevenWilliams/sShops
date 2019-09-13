package org.stevenw.shops.gui;

import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.stevenw.shops.ShopItem;
import org.stevenw.shops.types.BuyShop;

import java.util.ArrayList;
import java.util.List;

public class BuyMenu extends ItemMenu {

    public BuyMenu(String name, Size size, JavaPlugin plugin, BuyShop shop) {
        super(name, size, plugin);
        try{
            List<ShopItem> items = shop.getItems();
            for(int pos = 0; pos < items.size(); pos++) {
                ShopItem shopItem = items.get(pos);
                ItemStack item = shopItem.getItem();
                ItemStack item1 = item.clone();
                Long price = shopItem.getPrice().longValue();
                int amount = shopItem.getAmount();
                item.setAmount(amount);

                String lore;
                if(amount == 1) {
                 lore=   "Price: " + price.toString() + " " + shopItem.getCurrencyName();
                } else {
                    lore =  "Price: " + price.toString() + " " + shopItem.getCurrencyName() + " for " + amount;
                }

                setItem(pos, new BuyMenuItem(shop, item.getItemMeta().getDisplayName(), item, lore, this, price, shopItem, item1));
            }
        } catch (Exception e) {
            plugin.getLogger().info(e.getMessage() + e.getCause() + e.getStackTrace());
        }

    }
}