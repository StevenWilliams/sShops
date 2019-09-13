package org.stevenw.shops.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.stevenw.shops.sShops;
import org.stevenw.shops.types.BuyShop;
import org.stevenw.shops.types.SellShop;


public class ShopCreateListener implements Listener {
    private final sShops plugin;
    private final String sellSign;
    private final String buySign;
    public ShopCreateListener(sShops plugin) {
        this.plugin = plugin;
        this.sellSign = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.sell-sign"));
        this.buySign = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.buy-sign"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        String[] lines = e.getLines();
        String signType = ChatColor.stripColor(lines[0]);
        if(signType.equals(ChatColor.stripColor(buySign))) {
            if(player.hasPermission("vulcan.signshops.create.buy")) {
                if (BuyShop.Companion.exists(plugin, lines[1])) {
                    e.setLine(0, buySign);
                    player.sendMessage(ChatColor.GOLD + "Successfully created a buy shop!");
                } else {
                    player.sendMessage(ChatColor.RED + "Can't find shop: " + lines[1] + " as a buy-shop in the config!");
                }
            } else {
                e.setLine(0, "Buy");
                player.sendMessage(ChatColor.RED + "You do not have permission to create a shop!");
            }
        } else if(signType.equals(ChatColor.stripColor(sellSign))) {
            if(player.hasPermission("vulcan.signshops.create.sell")) {
                if (SellShop.exists(plugin, lines[1])) {
                    e.setLine(0, sellSign);
                    player.sendMessage(ChatColor.GOLD + "Successfully created a sell shop!");
                } else {
                    player.sendMessage(ChatColor.RED + "Can't find shop: " + lines[1] + " as a sell-shop in the config!");
                }
            }else {
                e.setLine(0, "Sell");
                player.sendMessage(ChatColor.RED + "You do not have permission to create a shop!");
            }
        }
    }
}
