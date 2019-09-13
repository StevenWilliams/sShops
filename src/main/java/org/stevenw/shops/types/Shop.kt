package org.stevenw.shops.types


import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import org.stevenw.shops.ShopItem
import org.stevenw.shops.Util
import org.stevenw.shops.economy.Economy
import org.stevenw.shops.sShops

import java.util.ArrayList
import kotlin.concurrent.thread
import kotlin.math.floor
import kotlin.math.roundToInt
import org.bukkit.Bukkit



//nothing yet, add it as an abstract class later.
abstract class Shop(protected var plugin: sShops, protected var economy: Economy, private val type: String, name: String, player: Player) {
    var name: String
        protected set
    var player: Player
        protected set

    val items: List<ShopItem>
        get() {

            val items = ArrayList<ShopItem>()
            val materialNames = plugin.config.getConfigurationSection("shops.$type.$name").getKeys(false)
            for (materialName in materialNames) {
                if (plugin.config.getLong("shops.$type.$name.$materialName", -1L) != -1L || plugin.config.contains("shops.$type.$name.$materialName.price")) {
                   var price : Double;
                    var quantity : Int;
                    var currencyKey :String? = null
                    if(plugin.config.contains("shops.$type.$name.$materialName.price")) {
                        quantity = plugin.config.getInt("shops.$type.$name.$materialName.quantity", 1)
                         price =  plugin.config.getDouble("shops.$type.$name.$materialName.price")
                        currencyKey =  plugin.config.getString("shops.$type.$name.$materialName.currency")
                    } else {
                         price = getPrice(MaterialData(Material.valueOf(materialName)))!!
                         quantity = 1;
                    }
                    var itemStack = Util.getItem(plugin, "shops.$type.$name.$materialName", Material.valueOf(materialName), player)
                    items.add(ShopItem(itemStack, price, false, quantity, currencyKey))
                } else {
                    val dataValues = plugin.config.getConfigurationSection("shops.$type.$name.$materialName").getKeys(false)
                    for (dataValue in dataValues) {
                        var price : Double;
                        var quantity : Int = 1;
                        var currencyKey :String? = null

                        if(plugin.config.contains("shops.$type.$name.$materialName.$dataValue.quantity")) {
                            quantity = plugin.config.getInt("shops.$type.$name.$materialName.$dataValue.quantity", 1)
                            price =  plugin.config.getDouble("shops.$type.$name.$materialName.$dataValue.price")
                            currencyKey =  plugin.config.getString("shops.$type.$name.$materialName.$dataValue.currency")

                        } else {
                            price = getPrice(MaterialData(Material.valueOf(materialName), java.lang.Byte.valueOf(dataValue)!!))!!

                        }
                        var item = Util.getItem(plugin, "shops.$type.$name.$materialName.$dataValue", Material.valueOf(materialName), player)
                        item.durability = java.lang.Short.valueOf(dataValue)
                        items.add(ShopItem(item, price, true, quantity, currencyKey))
                    }
                }
            }
            return items
        }

    init {
        this.name = name
        this.player = player
    }

    abstract fun open()
    fun getPrice(data: MaterialData): Double? {

        val byte1 = data.data
        if (byte1 != null) {
            val price = plugin.config.getDouble("shops." + type + "." + name + "." + data.itemType.toString() + "." + data.data, -1.0)
            if (price != -1.0) {
                return price
            }
        }
        var material = Bukkit.getUnsafe().fromLegacy(data)

        return plugin.config.getDouble("shops." + type + "." + name + "." + material.toString().replace("LEGACY_", ""))
    }

    @JvmOverloads
    fun getPrice(item: ItemStack, useLegacy: Boolean = !sShops.useNewMaterials): Double? {
        return if (!useLegacy) {
            plugin.config.getDouble("shops." + type + "." + name + "." + item.type.toString())
        } else {
            getPrice(item.data)
        }
    }

    fun canTransact(data: MaterialData): Boolean {
        println("canTransact bool: " + data.itemType.toString())
        return plugin.config.getLong("shops." + type + "." + name + "." + data.itemType.toString() + "." + data.data, -1L) != -1L || plugin.config.getLong("shops." + type + "." + name + "." + data.itemType.toString(), -1L) != -1L
    }

    fun canTransact(item: ItemStack): Boolean? {

        return if (sShops.useNewMaterials) {
            canTransact(item.type)
        } else {
            canTransact(item.data)
        }
    }

    //use to maintain bakcwards compatibility with config /legacy items
    fun canTransactSell(item: ItemStack): Boolean? {
        val data = item.data
        //plugin.getLogger().info(data.getItemType().toString());
        //plugin.getLogger().info(String.valueOf(data.getData()));

        return if (plugin.config.getLong("shops." + type + "." + name + "." + data.itemType.toString() + "." + data.data, -1L) != -1L || canTransact(item, false)!!) {
            true
        } else false
    }

    fun getPriceSell(item: ItemStack): Double {
        val data = item.data
        return if (plugin.config.getDouble("shops." + type + "." + name + "." + data.itemType.toString() + "." + data.data, -1.0) != -1.0) {
            plugin.config.getDouble("shops." + type + "." + name + "." + data.itemType.toString() + "." + data.data, -1.0)
        } else {
            getPrice(item, false)!!
        }
    }

    fun canTransact(item: ItemStack, useLegacy: Boolean): Boolean? {
        return if (!useLegacy) {
            canTransact(item.type)
        } else {
            canTransact(item.data)
        }
    }

    fun canTransact(material: Material): Boolean {
        return plugin.config.getLong("shops.$type.$name.$material", -1L) != -1L
    }

    abstract fun transact(item: ItemStack): Boolean


    fun transact2(items: Array<ItemStack>): Boolean {
        var floatingBalance: Double
        var itemStacks: ArrayList<ItemStack> = ArrayList<ItemStack>()
        var itemsSelling: HashMap<MaterialData, Int> = HashMap()
        for (item in items) {
            if (item != null) {
                if (this!!.canTransactSell(item)!!) {
                    itemStacks.add(item)
                    itemsSelling.put(item.data, itemsSelling.getOrDefault(item.data, 0) + item.amount)
                } else {
                    player.inventory.addItem(item)
                    player.updateInventory()
                }
            }

        }
        var totalSales : Double = 0.0
        for (material in itemsSelling.keys) {
            var price = getPrice(material)!!;
            totalSales += price * itemsSelling.get(material)!!
            println("${material.itemType.name}  $price")
        }
        val deposit : Long = floor(totalSales).toLong()
        val message = "Sold at $name for $$deposit"
        if(deposit <= 0.0) {
            player.sendMessage(plugin.prefix + "Nothing to sell.")
            return true;
        }
        if (economy.deposit(player, deposit, message)) {
            player.sendMessage(plugin.prefix + message)
           // item.setType(Material.AIR)
            return true
        } else {
            return false
        }

    }
    /**
     * Used for sell shosp only
     * @param items
     */
    fun transact(items: Array<ItemStack>) {
        //  plugin.getLogger().info("transact ItemStack");
        for (item in items) {
            //    plugin.getLogger().info("transact ItemStack 1");

            if (item != null) {
                //      plugin.getLogger().info("transact ItemStack 2");
                //todo: put each of these in own threads
                thread(start = true) {
                    println(item.type)
                    println("${Thread.currentThread()} has run.")
                    if (canTransactSell(item)!!) {
                        //        plugin.getLogger().info("transact ItemStack 3");
                        if (!transact(item)) {
                            player.sendMessage("Failed to transact for " + item.type + item.amount);
                            player.inventory.addItem(item)
                            player.updateInventory()
                        } else {

                        }

                    } else {
                        //      plugin.getLogger().info("transact ItemStack 4");

                        player.inventory.addItem(item)
                        player.updateInventory()
                    }
                }

            }
        }
    }

}
