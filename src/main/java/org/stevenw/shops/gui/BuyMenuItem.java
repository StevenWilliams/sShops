package org.stevenw.shops.gui;

import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.stevenw.shops.types.BuyShop;

public class BuyMenuItem extends MenuItem {
    private ItemMenu menu;
    private BuyShop shop;
    public BuyMenuItem(BuyShop shop, String displayName, ItemStack icon, String lore, ItemMenu menu) {
        super(displayName, icon, lore);
        this.menu = menu;
        this.shop = shop;
    }
    @Override
    public void onItemClick(ItemClickEvent event) {
        final Player player = event.getPlayer();
            if(shop.canTransact(getFinalIcon(player).getData())) {
                ItemStack item = getFinalIcon(player);
                ItemMeta meta = item.getItemMeta();
                meta.setLore(null);
                item.setItemMeta(meta);
                shop.transact(item);
            }

    }
}
