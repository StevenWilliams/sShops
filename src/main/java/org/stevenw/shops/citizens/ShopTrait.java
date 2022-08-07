package org.stevenw.shops.citizens;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.stevenw.shops.sShops;
import org.stevenw.shops.types.BuyShop;
import org.stevenw.shops.types.SellShop;

import java.util.Objects;
import java.util.logging.Level;

//This is your trait that will be applied to a npc using the /trait mytraitname command. Each NPC gets its own instance of this class.
//the Trait class has a reference to the attached NPC class through the protected field 'npc' or getNPC().
//The Trait class also implements Listener so you can add EventHandlers directly to your trait.
public class ShopTrait extends Trait {
    public ShopTrait() {
        super("sShopsTrait");
        plugin = JavaPlugin.getPlugin(sShops.class);
    }

    sShops plugin;

    // see the 'Persistence API' section
    @Persist("shop-active") boolean shopActive = true;

    public boolean isShopActive() {
        return shopActive;
    }

    public void setShopActive(boolean shopActive) {
        this.shopActive = shopActive;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    @Persist("shop-name") String shopName;
    @Persist("shop-type") String shopType; //todo: make it an enum


    // Here you should load up any values you have previously saved (optional).
    // This does NOT get called when applying the trait for the first time, only loading onto an existing npc at server start.
    // This is called AFTER onAttach so you can load defaults in onAttach and they will be overridden here.
    // This is called BEFORE onSpawn, npc.getEntity() will return null.
    public void load(DataKey key) {
       // shopActive = key.getBoolean("shop-active", false);
        //shopName = key.getString("shop-name");
    //    shopType = key.getString("shop-type");
    }

    // Save settings for this NPC (optional). These values will be persisted to the Citizens saves file
    public void save(DataKey key) {
      //  key.setBoolean("shop-active",shopActive);
        //key.setString("shop-name",shopName);
        //key.setString("shop-type", shopType);

    }

    // An example event handler. All traits will be registered automatically as Bukkit Listeners.
    @EventHandler
    public void click(NPCRightClickEvent event){
        if(event.getNPC()!=this.getNPC()) {
            return;
        }
        Player player = event.getClicker();
        if(Objects.equals(shopType, "sell")) {
            SellShop shop = new SellShop(plugin, plugin.getEconomy(), shopName, player);
            shop.open();
        } else if ("buy".equals(shopType)) {
            BuyShop shop = new BuyShop(plugin, plugin.getEconomy(), shopName, player);
            shop.open();
        }

        //Handle a click on a NPC. The event has a getNPC() method.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!

    }
    @EventHandler
    public void leftClick(NPCLeftClickEvent event) {
        if(event.getNPC()!=this.getNPC()) {
            return;
        }
        Player player = event.getClicker();
        if(Objects.equals(shopType, "sell")) {
            SellShop shop = new SellShop(plugin, plugin.getEconomy(), shopName, player);
            shop.handlePriceCheck();
        }
    }

    // Called every tick
    @Override
    public void run() {
    }

    //Run code when your trait is attached to a NPC.
    //This is called BEFORE onSpawn, so npc.getEntity() will return null
    //This would be a good place to load configurable defaults for new NPCs.
    @Override
    public void onAttach() {
        plugin.getServer().getLogger().info(npc.getName() + "has been assigned SShopsTrait!");
    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {
    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
    }

}

