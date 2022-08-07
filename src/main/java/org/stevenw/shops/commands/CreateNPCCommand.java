package org.stevenw.shops.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.stevenw.shops.citizens.ShopTrait;
import org.stevenw.shops.sShops;
import org.stevenw.shops.types.BuyShop;
import org.stevenw.shops.types.SellShop;

public class CreateNPCCommand implements CommandExecutor {
    private sShops plugin;

    public CreateNPCCommand(sShops plugin) {

        this.plugin = plugin;
    }
    // /createshopnpc (sell/buy) (shop name)
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length >= 1 && commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            String shopType = args[0];
            String shopName = args[1];

            if(!(BuyShop.Companion.exists(plugin, shopName) || SellShop.exists(plugin, shopName))){
                commandSender.sendMessage(plugin.getPrefix() + "Invalid shop name.");
                return false;
            }
            if(!commandSender.hasPermission("shops.command.createnpc")) {
                commandSender.sendMessage(plugin.getPrefix() + "You are not allowed you use this shop.");
                return false;
            }

            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, shopType + " " + shopName);
            ShopTrait trait = new ShopTrait();
            trait.setShopType(shopType);
            trait.setShopName(shopName);
            trait.setShopActive(true);
            npc.addTrait(trait);
            npc.spawn(player.getLocation());

            return true;
        }
        return false;
    }
}
