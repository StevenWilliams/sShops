package org.stevenw.shops;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Util {

    public static ItemStack getItem(sShops plugin, String itempath, Material item, Player player) {

        ItemStack loadedStack;

        Integer itemquantity = plugin.getConfig().getInt(itempath + "." +  "quantity", 1);

        loadedStack = new ItemStack(item, itemquantity);

        ItemMeta loadedStackMeta = loadedStack.getItemMeta();
        if( plugin.getConfig().contains(itempath + "." +  ".displayName")) {
            loadedStackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(itempath + "." + ".displayName")));
        }
        if(plugin.getConfig().contains(itempath + "." +  ".lore")) {
            List<String> loreItemConfig = plugin.getConfig().getStringList(itempath + "." + ".lore");
            List<String> lore = new ArrayList();
            for (String loreItemConfigLine : loreItemConfig) {
                lore.add(loreItemConfigLine);
            }
            loadedStackMeta.setLore(lore);

        }

        if (plugin.getConfig().getConfigurationSection(itempath + "." + ".enchantments") != null) {
            Set<String> enchantItemConfig = plugin.getConfig().getConfigurationSection(itempath + "." + ".enchantments").getKeys(false);
            for (String enchantment : enchantItemConfig) {
                loadedStackMeta.addEnchant(Enchantment.getByName(enchantment), plugin.getConfig().getInt(itempath + "." + ".enchantments." + enchantment), true);
            }
        }

        loadedStack.setItemMeta(loadedStackMeta);
        return loadedStack;
    }

}
