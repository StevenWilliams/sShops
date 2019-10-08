package org.stevenw.shops.economy

import net.vulcanmc.vulcaneconomy.API
import net.vulcanmc.vulcaneconomy.APIAsync
import net.vulcanmc.vulcaneconomy.rest.Account
import net.vulcanmc.vulcaneconomy.rest.Currency
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.stevenw.shops.sShops

class AsyncEconomy (var plugin : sShops){
    lateinit private var  vulcanAPIConnector: VulcanEconomyConnector
    lateinit private var vulcanAPI: API
    lateinit private var api: APIAsync

    init {
        this.api = APIAsync( plugin as JavaPlugin)
        vulcanAPIConnector = plugin.economy as VulcanEconomyConnector
        vulcanAPI = API(plugin as Plugin)
    }


    fun withdraw(player: Player, amount: Long, message: String, success: Runnable, failure: Runnable) {
        withdraw(player, amount, null, message, success, failure)
    }

    fun deposit(player: Player, amount: Long, message: String, success: Runnable, failure: Runnable) {
        deposit(player, amount, null, message, success, failure)
    }

    fun withdraw(player: Player, amount: Long, currencyKey: String?, message: String, success: Runnable, failure: Runnable) {
        var currencyKey = currencyKey
        if (currencyKey == null) {
            currencyKey = vulcanAPIConnector.defaultCurrencyKey
        }

        val currency = vulcanAPI.getCurrency(currencyKey!!)!!

        val account = vulcanAPI.getPlayerAccount(player, currency)!!
        api.withdraw(account, amount, message, success, failure)
    }

    fun deposit(player: Player, amount: Long, currencyKey: String?, message: String, success: Runnable, failure: Runnable) {
        var currencyKey = currencyKey
        if (currencyKey == null) {
            currencyKey = vulcanAPIConnector.defaultCurrencyKey
        }
        val currency = vulcanAPI.getCurrency(currencyKey!!)!!

        val account = vulcanAPI.getPlayerAccount(player, currency)!!
        api.deposit(account, amount, message, success, failure)
    }

    fun has(player: Player, amount: Long, currencyKey: String?, message: String, success: Runnable, failure: Runnable): Boolean {
        var currencyKey = currencyKey
        if (currencyKey == null) {
            currencyKey = vulcanAPIConnector.defaultCurrencyKey
        }
        val currency = vulcanAPI.getCurrency(currencyKey!!)!!

        return vulcanAPI.has(player, currency, amount)
    }
}
