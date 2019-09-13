package org.stevenw.shops.types

import ninja.amp.ampmenus.menus.ItemMenu
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.stevenw.shops.ShopItem
import org.stevenw.shops.economy.Economy
import org.stevenw.shops.gui.BuyMenu
import org.stevenw.shops.sShops
import kotlin.math.floor

class BuyShop(plugin: sShops, economy: Economy, name: String, player: Player) : Shop(plugin, economy, "buy", name, player) {

    override fun open() {
        val menu = BuyMenu(INV_NAME + name, ItemMenu.Size.fit(items.size), plugin, this)
        menu.open(player)
    }

    override fun transact(item: ItemStack): Boolean {
        return transact(item, !sShops.useNewMaterials)
    }

    @JvmOverloads
    fun transact(shopItem: ShopItem, useLegacy: Boolean = false): Boolean {
        val price = floor(shopItem.price).toLong()
        val amount = shopItem.amount
        val message = "Bought " + amount + " " + shopItem.item.type.toString().toLowerCase().replace("_", " ") + " for $" + price!!.toString()
        val currencyKey = shopItem.currencyKey;
        if(currencyKey == null) {
            if (economy.withdraw(player, price, message)) {
                player.sendMessage(plugin.prefix + message)
                player.inventory.addItem(shopItem.item)
                return true
            } else {
                return false
            }
        } else {
            if (economy.withdraw(player, currencyKey, price, message)) {
                player.sendMessage(plugin.prefix + message)
                player.inventory.addItem(shopItem.item)
                return true
            } else {
                return false
            }
        }

    }

    fun transact(item: ItemStack, useLegacy: Boolean): Boolean {
        //   plugin.getLogger().info("transact1123");
        val amount = item.amount

        if (canTransact(item, useLegacy)!!) {
            //     plugin.getLogger().info("can transact");
            val price = floor(getPrice(item, useLegacy)!!).toLong()
            val message = "Bought " + item.type.toString().toLowerCase().replace("_", " ") + " for $" + price

            if (economy.withdraw(player, price!!, message)) {
                //       plugin.getLogger().info("withdraw success");
                // player.sendMessage(plugin.getPrefix() + "Bought "  + item.getType().toString().toLowerCase().replace("_", " ") + " for $" + price);
                player.sendMessage(plugin.prefix + message)
                player.inventory.addItem(item)
                return true
            } else {
                //     plugin.getLogger().info("can withdraw failed");

            }
        } else {
            plugin.logger.info("cant transact")
        }
        return false
    }

    companion object {
        val INV_NAME = "Buy Shop: "

        fun exists(plugin: sShops, shopName: String): Boolean {
            return if (plugin.config.getConfigurationSection("shops.buy.$shopName") == null) {
                false
            } else {
                true
            }
        }
    }
}