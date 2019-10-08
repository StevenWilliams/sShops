package org.stevenw.shops.items

import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.stevenw.shops.types.BuyShop

class CommandShopItem(config: ConfigurationSection, item: ItemStack, price: Double?, amount: Int,
                      currencyKey: String) : ShopItem(config, item, price, amount, currencyKey) {
    override fun getLore(): String {
        return config.getString("shop-lore", "shop-lore needs to be set in config")
    }

    override fun execute(shop: BuyShop, player: Player) {
        val commands : MutableList<String> = config.getStringList("commands");
        for(command in commands) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), replacePlaceholders(command, player));
        }
    }
    fun replacePlaceholders(original : String, player: Player) : String {
        return original.replace("<USERNAME>",player.name)
    }
}
