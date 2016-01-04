package org.stevenw.shops;

import org.bukkit.inventory.ItemStack;

public class ShopItem {
    private ItemStack item;
    private Long price;

    public ShopItem(ItemStack item, Long price) {
        this.item = item;
        this.price = price;
    }
    public ItemStack getItem() {
        return this.item;
    }
    public Long getPrice() {
        return this.price;
    }
}
