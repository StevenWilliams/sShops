package org.stevenw.shops.listeners


import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.scheduler.BukkitRunnable
import org.stevenw.shops.sShops
import org.stevenw.shops.types.BuyShop
import org.stevenw.shops.types.SellShop

class ShopUseListener(private val plugin: sShops) : Listener {
    private val sellSign: String
    private val buySign: String

    init {
        this.sellSign = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("signs.sell-sign"))
        this.buySign = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("signs.buy-sign"))
    }

    //Used for sell shops
    @EventHandler
    fun onInvClose(e: InventoryCloseEvent) {
        val player = e.player as Player
        val inv = e.inventory
        plugin.logger.info("inventory close")
        if (inv.name.startsWith(SellShop.INV_NAME)) {
            plugin.logger.info("inventory close1")

            val shopName = inv.name.substring(SellShop.INV_NAME.length)
            val shop = SellShop(plugin, plugin.economy, shopName, player)
            if(!shop.transact2(inv.contents)) {
                player.sendMessage("Failed to sell. Please report this to a staff member.")
                for(content in inv.contents) {
                    if(content != null) {
                        player.inventory.addItem(content)
                    }
                }
                player.updateInventory()
            }
        }
    }

    @EventHandler
    fun onInventoryMoveEvent(e: InventoryMoveItemEvent) {
        val inv = e.source

        if (inv.name.startsWith(BuyShop.INV_NAME)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onSignClick(e: PlayerInteractEvent) {
        val player = e.player
        val action = e.action
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            val block = e.clickedBlock
            if (block.type == Material.WALL_SIGN|| block.type == Material.LEGACY_SIGN_POST || block.type ==Material.SIGN || block.type == Material.LEGACY_SIGN || block.type==Material.LEGACY_WALL_SIGN) {
                val sign = block.state as Sign
                val lines = sign.lines
                if (lines[0] == sellSign && SellShop.exists(plugin, lines[1])) {
                    val shop = SellShop(plugin, plugin.economy, lines[1], player)
                    if (action == Action.RIGHT_CLICK_BLOCK) {
                        e.isCancelled = true
                        shop.open()
                    } else if (player.gameMode != GameMode.CREATIVE) {
                        e.isCancelled = true
                        shop.handlePriceCheck();
                    }
                } else if (lines[0] == buySign && BuyShop.exists(plugin, lines[1])) {
                    if (action == Action.RIGHT_CLICK_BLOCK) {
                        e.isCancelled = true
                        val shop = BuyShop(plugin, plugin.economy, lines[1], player)
                        shop.open()
                    }
                }
            }
        }
    }
}
