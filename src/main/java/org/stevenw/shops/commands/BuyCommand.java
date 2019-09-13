package org.stevenw.shops.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stevenw.shops.sShops;
import org.stevenw.shops.types.BuyShop;

public class BuyCommand  implements CommandExecutor {
    private sShops plugin;

    public BuyCommand(sShops plugin) {

        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length >= 1 && commandSender instanceof Player) {
            String shopName = args[0];

            if(!BuyShop.Companion.exists(plugin, shopName)){
                commandSender.sendMessage(plugin.getPrefix() + "Invalid shop name.");
                return false;
            }
            if(!commandSender.hasPermission("shops.command.buy." + shopName)) {
                commandSender.sendMessage(plugin.getPrefix() + "You are not allowed you use this shop.");
                return false;
            }
            BuyShop shop = new BuyShop(plugin, plugin.getEconomy(), shopName, (Player) commandSender);
            shop.open();
            return true;
        }
        return false;
    }
}
