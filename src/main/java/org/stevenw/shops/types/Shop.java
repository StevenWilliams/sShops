package org.stevenw.shops.types;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.stevenw.shops.ShopItem;
import org.stevenw.shops.economy.Economy;
import org.stevenw.shops.sShops;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//nothing yet, add it as an abstract class later.
public abstract class Shop{
    protected String name;
    protected Player player;
    protected sShops plugin;
    protected Economy economy;
    private String type;

    public Shop(sShops plugin, Economy economy, String type, String name, Player player) {
        this.plugin = plugin;
        this.name = name;
        this.player = player;
        this.economy = economy;
        this.type = type;
    }
    public abstract void open();
    public Long getPrice(MaterialData data) {
        Byte byte1 = data.getData();
        if(byte1 != null) {
            Long price = plugin.getConfig().getLong("shops." + type + "." + name + "." + data.getItemType().toString() + "." + data.getData(), -1L);
            if (price != -1L) {
                return price;
            }
        }
        return plugin.getConfig().getLong("shops." + type + "." + name + "." + data.getItemType().toString());
    }

    public String getName() {
        return this.name;
    }
    public Player getPlayer() {
        return this.player;
    }
    public boolean canTransact(MaterialData data) {
        return ((plugin.getConfig().getLong("shops." + type + "." + name + "." + data.getItemType().toString() + "." + data.getData(), -1L) != -1L) ||
                plugin.getConfig().getLong("shops." + type + "." + name + "." + data.getItemType().toString(), -1L) != -1L);
    }

    public abstract boolean transact(ItemStack item);
    public void transact(ItemStack[] items) {
        for(ItemStack item : items) {
            if(item != null) {
                if (canTransact(item.getData())) {
                    transact(item);
                } else {
                    player.getInventory().addItem(item);
                    player.updateInventory();
                }
            }
        }
    }

    public List<ShopItem> getItems() {
        ArrayList<ShopItem> items = new ArrayList();
        Set<String> materialNames = plugin.getConfig().getConfigurationSection("shops." + type + "." + name).getKeys(false);
        for(String materialName : materialNames) {
            if(plugin.getConfig().getLong("shops." + type + "." + name + "." + materialName, -1L) != -1L) {
                Long price = getPrice(new MaterialData(Material.valueOf(materialName)));
                items.add(new ShopItem(new ItemStack(Material.valueOf(materialName)), price));
            } else {
                Set<String> dataValues = plugin.getConfig().getConfigurationSection("shops." + type + "." + name + "." + materialName).getKeys(false);
                for(String dataValue : dataValues) {
                    Long price = getPrice(new MaterialData(Material.valueOf(materialName), Byte.valueOf(dataValue)));
                    ItemStack item = new ItemStack(Material.valueOf(materialName));
                    item.setDurability(Short.valueOf(dataValue));
                    items.add(new ShopItem(item, price));
                }
            }
        }
        return items;
    }

}
