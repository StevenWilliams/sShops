package org.stevenw.shops.items

import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.stevenw.shops.sShops
import org.stevenw.shops.sShops.plugin
import org.stevenw.shops.types.BuyShop
import kotlin.math.floor

class ItemStackBuyItem(config: ConfigurationSection, item: ItemStack, price: Double?, amount: Int,
                       currencyKey: String?) : ShopItem(config, item, price, amount,
        currencyKey) {
    override fun getLore() : String {
        var lore : String;
        if (amount == 1) {
            lore = "Price: " + price.toString() + " " + currencyName
        } else {
            lore = "Price: " + price.toString() + " " + currencyName + " for " + amount
        }
        return lore;
    }


    override fun execute(buyShop:BuyShop, player: Player) {
        player.sendMessage(sShops.plugin.prefix + "Buying " + item.type.toString().toLowerCase().replace("_", " ") + " for " + price + ". It may take a moment to receive the item.")

        //val meta = item.itemMeta
        //meta.lore = null
        //item.itemMeta = meta
        //buyShop.transact(this, this.isLegacy )


        val price = floor(this.price!!).toLong()
        val amount = amount
        val message = "Bought " + amount + " " + item.type.toString().toLowerCase().replace("_", " ") + " for $" + price!!.toString()
        val currencyKey = currencyKey;
        var economy = sShops.plugin.asyncEconomy;
            economy.withdraw(player, price, currencyKey, message, Runnable {

                player.sendMessage(plugin.prefix + message)
                player.inventory.addItem(item)
            }, Runnable {
                player.sendMessage(" Failed to buy.")
            })

    }


}