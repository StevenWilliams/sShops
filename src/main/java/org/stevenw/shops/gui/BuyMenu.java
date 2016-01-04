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
        List<ShopItem> items = shop.getItems();
        for(int pos = 0; pos < items.size(); pos++) {
            ShopItem shopItem = items.get(pos);
            ItemStack item = shopItem.getItem();
            Long price = shopItem.getPrice();
            setItem(pos, new BuyMenuItem(shop, item.getItemMeta().getDisplayName(), item, "Price: $" + price.toString(), this));
        }
    }
}