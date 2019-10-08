package org.stevenw.shops.items

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.stevenw.shops.economy.VulcanEconomyConnector
import org.stevenw.shops.sShops
import org.stevenw.shops.types.BuyShop

abstract class ShopItem(val config : ConfigurationSection, val item: ItemStack, val price: Double?, val
amount: Int, currencyKey: String?) {
    var currencyKey: String? = null
        private set
    var currencyName: String? = null
        private set

    init {
        addCurrencies()
        //this.lore = "Price: " + price!!.toString() + " " + currencyName + if (amount == 1) "" else " " + " for $"

    }


    abstract fun execute(buyShop:BuyShop, player: Player);
    abstract fun getLore() : String;


    private fun addCurrencies() {
        if (currencyKey == null) {
            if (sShops.plugin.economy is VulcanEconomyConnector) {
                this.currencyKey = (sShops.plugin.economy as VulcanEconomyConnector).defaultCurrencyKey
                this.currencyName = (sShops.plugin.economy as VulcanEconomyConnector).defaultCurrencyName
            } else {
                this.currencyKey = null
                this.currencyName = "Dollars"
            }
        } else {
            this.currencyKey = currencyKey
            if (sShops.plugin.economy is VulcanEconomyConnector) {
                this.currencyName = (sShops.plugin.economy as VulcanEconomyConnector).getCurrencyName(currencyKey)
            } else {
                this.currencyName = "Dollars"
            }
        }
    }
}
