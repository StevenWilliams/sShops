package org.stevenw.shops.listeners;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.stevenw.shops.sShops;
import org.stevenw.shops.types.BuyShop;
import org.stevenw.shops.types.SellShop;

public class ShopUseListener implements Listener {
    private final sShops plugin;
    private final String sellSign;
    private final String buySign;
    public ShopUseListener(sShops plugin) {
        this.plugin = plugin;
        this.sellSign = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.sell-sign"));
        this.buySign = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("signs.buy-sign"));
    }

    //Used for sell shops
    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player player = (Player)e.getPlayer();
        final Inventory inv = e.getInventory();
        if(inv.getName().startsWith(SellShop.INV_NAME)) {
            String shopName = inv.getName().substring(SellShop.INV_NAME.length());
            final SellShop shop = new SellShop(plugin, plugin.getEconomy(), shopName, player);
            shop.transact(inv.getContents());
        }
    }


    @EventHandler
    public void onSignClick(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        Action action = e.getAction();
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            if ((block.getType().equals(Material.WALL_SIGN)) || (block.getType().equals(Material.SIGN_POST))) {
                Sign sign = (Sign)block.getState();
                String[] lines = sign.getLines();
                if (lines[0].equals(sellSign) && SellShop.exists(plugin, lines[1])) {
                    SellShop shop = new SellShop(plugin, plugin.getEconomy(), lines[1], player);
                    if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
                        e.setCancelled(true);
                        shop.open();
                    } else if(player.getGameMode() != GameMode.CREATIVE) {
                        e.setCancelled(true);
                        if(shop.canTransact(player.getItemInHand().getData())) {
                            player.sendMessage(plugin.getPrefix() + "Sell this for $" + shop.getPrice(player.getItemInHand().getData()));
                        } else {
                            player.sendMessage(plugin.getPrefix() + "You cannot sell this.");
                        }
                    }
                }
                else if (lines[0].equals(buySign) && BuyShop.exists(plugin, lines[1])) {
                    if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
                        e.setCancelled(true);
                        BuyShop shop = new BuyShop(plugin, plugin.getEconomy(), lines[1], player);
                        shop.open();
                    }
                }
            }
        }
    }
}
