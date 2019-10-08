package org.stevenw.shops.gui

import ninja.amp.ampmenus.events.ItemClickEvent
import ninja.amp.ampmenus.items.MenuItem
import ninja.amp.ampmenus.menus.ItemMenu
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.stevenw.shops.items.ShopItem
import org.stevenw.shops.sShops
import org.stevenw.shops.types.BuyShop
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread
import kotlin.math.roundToLong

class BuyMenuItem(private val shop: BuyShop, displayName: String, icon: ItemStack, lore: String, private val menu: ItemMenu, private val price: Long?, private val shopItem: ShopItem, val item: ItemStack) : MenuItem(displayName, icon, lore) {
    override fun onItemClick(event: ItemClickEvent) {
        val player = event.player
        var pendingAmount : Long
        if(pendingAmountMap.containsKey(player)) {
            println("contains Key Player pendingAmountTable")
        } else {
            println("not contains Key Player pendingAmountTable")
        }
        pendingAmountMap.putIfAbsent(player, AtomicLong(0));

        pendingAmount = pendingAmountMap.get(player)!!.addAndGet(shopItem.price!!.roundToLong())


        //check if has pending amount to avoid going into negative and be thread safe
        var minBalance = pendingAmount
        if (sShops.plugin.economy.has(player, shopItem.currencyKey, minBalance)) {
            val item = getFinalIcon(player)
            player.sendMessage(sShops.plugin.prefix + "Buying " + item.itemMeta.displayName
                    + " for " + price + ". It may take a moment to receive the item.")
            //todo update pending amount for failures
            shopItem.execute(shop, player);
            // shop.transact(shopItem, shopItem.isLegacy )
            //shop.transact(item, shopItem.isLegacy());

        } else {
            player.sendMessage(sShops.plugin.prefix + "You do not have enough money to make this purchase.")
        }
        pendingAmountMap.get(player)!!.addAndGet((-1 * shopItem.price!!).roundToLong())


    }
    companion object {
        var pendingAmountMap : Hashtable<Player, AtomicLong> = Hashtable<Player, AtomicLong>();
    }
}
