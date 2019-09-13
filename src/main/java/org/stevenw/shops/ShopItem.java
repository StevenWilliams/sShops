package org.stevenw.shops;

import org.bukkit.inventory.ItemStack;
import org.stevenw.shops.economy.VulcanEconomyConnector;

public class ShopItem {
    private ItemStack item;
    private Double price;
    private boolean legacy;
    private int amount;
    private String currencyKey;
    private String currencyName;

    public ShopItem(ItemStack item, Double price, boolean useLegacy) {
        this(item, price, useLegacy, 1);
    }
    public ShopItem(ItemStack item, Double price, boolean useLegacy, int amount) {
        this(item, price, useLegacy, amount, null);
    }
    public ShopItem(ItemStack item, Double price, boolean useLegacy, int amount, String currencyKey) {
        this.item = item;
        this.price = price;
        this.legacy = useLegacy;
        this.amount = amount;
        if(currencyKey == null) {
            if(sShops.plugin.getEconomy() instanceof VulcanEconomyConnector) {
                this.currencyKey = ((VulcanEconomyConnector) sShops.plugin.getEconomy()).getDefaultCurrencyKey();
                this.currencyName = ((VulcanEconomyConnector) sShops.plugin.getEconomy()).getDefaultCurrencyName();
            } else {
                this.currencyKey = null;
                this.currencyName = "Dollars";
            }
        } else {
            this.currencyKey = currencyKey;
            if(sShops.plugin.getEconomy() instanceof VulcanEconomyConnector) {
                this.currencyName = ((VulcanEconomyConnector) sShops.plugin.getEconomy()).getCurrencyName(currencyKey);
            } else {
                this.currencyName = "Dollars";
            }
        }
    }
    public ShopItem(ItemStack item, Double price) {
        this(item, price, false);
    }
    public ItemStack getItem() {
        return this.item;
    }
    public Double getPrice() {
        return this.price;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrencyKey() {
        return currencyKey;
    }

    public String getCurrencyName() {
        return currencyName;
    }
}
